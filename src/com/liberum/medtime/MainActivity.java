package com.liberum.medtime;

import java.util.ArrayList;
import java.util.Random;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;


public class MainActivity extends Activity implements
ActionBar.OnNavigationListener {
		
	  final int DIALOG_EXIT = 1;
	  LinearLayout view;
	  EditText mNew_course;
	  
	  private static final int CM_DELETE_ID = 1;
	  ListView lvData;
	  DB db;
	  SimpleCursorAdapter scAdapter;
	  Cursor cursor;
	  
	  int idCourse;
	  
	  String setCourse = "не пришло :(";
	  	  
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // пробуем 
        vievCourse();
        
        // рисуем спинер
        mySpiner();
                
    }
    
 // рисуем все курсы
    public void allItem() {
        // открываем подключение к БД
        db = new DB(this);
        db.open();

        // получаем курсор
        cursor = db.getAllData();
        startManagingCursor(cursor);
        
        // формируем столбцы сопоставления
        String[] from = new String[] { DB.COLUMN_IMG, DB.COLUMN_TXT, DB.COLUMN_NUM, DB.COLUMN_NAME_COURSE };
        int[] to = new int[] { R.id.ivImg, R.id.tvText, R.id.tvNum, R.id.nCour };

        // создааем адаптер и настраиваем список
        scAdapter = new SimpleCursorAdapter(this, R.layout.item, cursor, from, to);
        lvData = (ListView) findViewById(R.id.lvData);
        lvData.setAdapter(scAdapter);

        // добавляем контекстное меню к списку
        registerForContextMenu(lvData);  
    }
    
    // рисуем все лекарства одного курса
    public void CourseItem() {
        // открываем подключение к БД
        db = new DB(this);
        db.open();
        
        String mCourse = Integer.toString(idCourse);
        // получаем курсор
        cursor = db.viewCourse(mCourse);
        startManagingCursor(cursor);
        
        // формируем столбцы сопоставления
        String[] from = new String[] {DB.ID_COURSE, DB.ID_MED};
        int[] to = new int[] {R.id.nCour, R.id.tvText};

        // создааем адаптер и настраиваем список
        scAdapter = new SimpleCursorAdapter(this, R.layout.item, cursor, from, to);
        lvData = (ListView) findViewById(R.id.lvData);
        lvData.setAdapter(scAdapter);

        // добавляем контекстное меню к списку
        registerForContextMenu(lvData);  
    }
    
    
    // отображение выбраного курса 
    public void vievCourse() {
        // открываем подключение к БД
        db = new DB(this);
        db.open();
        
        // обрабатываем нажатие спинера
        // получаем список курсов
        cursor = db.allCourse();
        // ставим курсор на первую позицию
    	cursor.moveToFirst();    	
    	ArrayList<String> dbCourse = new ArrayList<String>();
    	int nameCourse = cursor.getColumnIndex(DB.COLUMN_NAME_COURSE);
        do {
            // получаем значения по номерам столбцов и засовываем в аррейлист
            dbCourse.add(cursor.getString(nameCourse));
            // переход на следующую строку 
            // а если следующей нет (текущая - последняя), то false - выходим из цикла
          } while (cursor.moveToNext());              
        	// забираем с ArrayList курс который мы кликнули, используя idCourse, которую нам возращает обработчик нажатия
        	setCourse = dbCourse.get(idCourse);        
        // смотрим id выбора в спинере и с ним работаем..
        Toast toast = Toast.makeText(getApplicationContext(), 
      		  "ID курса = " + idCourse + " Название = " + setCourse, Toast.LENGTH_SHORT); 
      		toast.show(); 
      		
      		// отображаем или все курсы или только выбраный
      		if (idCourse == 0) {
      			allItem();
      		} else {
      			// запустить метод с новыми данными 
      			CourseItem();

      		}

        // добавляем контекстное меню к списку
//        registerForContextMenu(lvData);  
    }
    
    
    public void mySpiner() {
    	
    	//тут метод с DB.java который поставит курсор куда надо )
    	cursor = db.allCourse();
    	
        // получаем список курсов
    	cursor.moveToFirst();
        ArrayList<String> dbCourse = new ArrayList<String>();
        int nameCourse = cursor.getColumnIndex(DB.COLUMN_NAME_COURSE);
        do {
            // получаем значения по номерам столбцов и засовываем в аррейлист
            dbCourse.add(cursor.getString(nameCourse));
            // переход на следующую строку 
            // а если следующей нет (текущая - последняя), то false - выходим из цикла
          } while (cursor.moveToNext());
        
        // заполняем спинера в экшен баре
        ActionBar bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbCourse);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bar.setListNavigationCallbacks(adapter, this);
        
        //как то должно обновлять спинер когда меняем бд, поискать в инете
        adapter.notifyDataSetChanged();        

    }
    
    // обработка нажатия кнопки добавления курса
    public void onButtonClick(View view) {
        // вызываем диалог
        showDialog(DIALOG_EXIT);    

    }
    
    public void onCreateContextMenu(ContextMenu menu, View v,
        ContextMenuInfo menuInfo) {
      super.onCreateContextMenu(menu, v, menuInfo);
      menu.add(0, CM_DELETE_ID, 0, R.string.delCourse);
      cursor.requery();
    }
    
    
    // удаление курса в контестном меню
    public boolean onContextItemSelected(MenuItem item) {
      if (item.getItemId() == CM_DELETE_ID) {
        // получаем из пункта контекстного меню данные по пункту списка 
        AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
        // извлекаем id записи и удаляем соответствующую запись в БД
        db.delRec(acmi.id);
        // обновляем курсор
        cursor.requery();
        reload();
        return true;
      }

      return super.onContextItemSelected(item);
      
    }
    
    protected void onDestroy() {
      super.onDestroy();
      // закрываем подключение при выходе
      db.close();
    }
    
    // обработчик спинера в экшен баре
    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {    	
      idCourse = (int)itemId;
      vievCourse(); // при нажатии показываем нужный курс, id которого будет равен id в таблице    		
      return false;
    }

    // наше меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);       
        return true;
    }
    
    // обработка нашего меню
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch (item.getItemId()) {
        case R.id.pharmacy:
          Toast.makeText(this, "сейчас пойдем в аптеку", Toast.LENGTH_SHORT).show();
          Intent intent = new Intent(MainActivity.this, Pharmacy.class);
          startActivity(intent);          
          break;
          
        case R.id.add_med:
            Toast.makeText(this, "Выбор лекарств", Toast.LENGTH_SHORT).show();
            add_med();
            break;
          
        case R.id.refresh:
          Toast.makeText(this, "Обновили", Toast.LENGTH_SHORT).show();
          reload();
          break;
      }
      return(super.onOptionsItemSelected(item));
    }
    
    private void add_med() {
		// TODO Auto-generated method stub
		
	}

	//Диалог добавления курса
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_EXIT) {

          AlertDialog.Builder adb = new AlertDialog.Builder(this);
          // заголовок
          adb.setTitle(R.string.addCourse);
          // сообщение
          adb.setMessage(R.string.course_data);
          // иконка
          adb.setIcon(android.R.drawable.ic_dialog_info);
          // кнопка положительного ответа
          adb.setPositiveButton(R.string.yes, myClickListener);
          // кнопка отрицательного ответа
          adb.setNegativeButton(R.string.no, myClickListener);
          // создаем view из dialog.xml
          view = (LinearLayout) getLayoutInflater().inflate(R.layout.add_course, null);
          // устанавливаем ее, как содержимое тела диалога
          adb.setView(view);
          // создаем диалог
          return adb.create();
              	
        }
        return super.onCreateDialog(id);
      }
    
    OnClickListener myClickListener = new OnClickListener() {
    public void onClick(DialogInterface dialog, int which) {
      switch (which) {
      // положительная кнопка
      case Dialog.BUTTON_POSITIVE:
    	  EditText text = (EditText)((AlertDialog)dialog).findViewById(R.id.new_course); 	// получаем значение
    	  saveData(text.getText().toString());												// переводим в стринг
        break;
      // негативная кнопка
      case Dialog.BUTTON_NEGATIVE:
        break;

      }
    }
  };
  
  // обработка сохранения нового курса
  void saveData(String text) {
	  
	  // Добавляем название курса в таблицу с названиями курсов
  	db.addCourse(text, R.drawable.ic_launcher);
  	// обновляем курсор
    cursor.requery();
    	
    // всплывающее окно покахывающее, что данные сохранены
    Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
    
    findViewById(R.id.root).invalidate();
    
    // перезапускаем активити, что бы обновился спинер
    reload();    
  }
  
  
  //перезапуск активити
  private void reload()
  {
      Intent intent = getIntent();
      overridePendingTransition(0, 0);
      intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
      finish();
      overridePendingTransition(0, 0);
      startActivity(intent);
  }
    
}
