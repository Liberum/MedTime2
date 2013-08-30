package com.liberum.medtime;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class Pharmacy extends Activity {
	
	// прописываем переменные
	ListView lvPharmacy;
	DB db;
	SimpleCursorAdapter scAdapter;
	Cursor cursor;
	private static final int CM_DELETE_ID = 1;
	final int DIALOG_EXIT = 1;
	LinearLayout view;
	
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.pharmacy);	    
	    pharmacy();
	    
	  }
	  
	  
	  public void pharmacy() {
		  
	        // открываем подключение к БД
	        db = new DB(this);
	        db.open();	        
	        
	        // получаем курсор
	        cursor = db.getAllMed();
	        startManagingCursor(cursor);
	                
	        // формируем столбцы сопоставления
	        String[] from = new String[] { DB.COLUMN_IMG_MED, DB.COLUMN_NAME_MED, DB.COLUMN_NUM_MED};
	        int[] to = new int[] { R.id.ivImg, R.id.tvText, R.id.tvNum};
	        
	        // создааем адаптер и настраиваем список
	        scAdapter = new SimpleCursorAdapter(this, R.layout.item, cursor, from, to);
	        lvPharmacy = (ListView) findViewById(R.id.lvPharmacy);
	        lvPharmacy.setAdapter(scAdapter);
	        
	        // добавляем контекстное меню к списку
	        registerForContextMenu(lvPharmacy);  
	  }
	  
	  // сделать добавление нового лекарства 
	  
	  
	  
	    // обработка нажатия кнопки добавления курса
	    public void onButtonClickMed(View view) {
	        // вызываем диалог
	        showDialog(DIALOG_EXIT);    

	    }	    
	    
	    //Диалог добавления курса
	    protected Dialog onCreateDialog(int id) {
	        if (id == DIALOG_EXIT) {

	          AlertDialog.Builder adb = new AlertDialog.Builder(this);
	          // заголовок
	          adb.setTitle(R.string.addMed);
	          // сообщение
//	          adb.setMessage(R.string.med_data);
	          // иконка
	          adb.setIcon(android.R.drawable.ic_dialog_info);
	          // кнопка положительного ответа
	          adb.setPositiveButton(R.string.yes, myClickListener);
	          // кнопка отрицательного ответа
	          adb.setNegativeButton(R.string.no, myClickListener);
	          // создаем view из dialog.xml
	          view = (LinearLayout) getLayoutInflater().inflate(R.layout.add_med, null);
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
	        	  int num2 = 0;
	        	  EditText text = (EditText)((AlertDialog)dialog).findViewById(R.id.new_med);		// получаем значение названия
	        	  EditText num = (EditText)((AlertDialog)dialog).findViewById(R.id.numMed);		// получаем значение количества
	        	  String num1 = num.getText().toString();
	        	  
	        	  // если ничего не введено пишет значение ноль, если введено преобразовывает в инт
	              try {
	            	  num2 = Integer.parseInt(num1);
	              } catch (Exception e) {
	                  num2 = 0;
	              }
	        	  
	        	  saveData(text.getText().toString(), num2);				// через жопу вроде делает и стринг и инт
	        	  
	            break;
	            	// негативная кнопка
	          	case Dialog.BUTTON_NEGATIVE:
	            break;

	          }
	        }
	      };
	      
	      // обработка сохранения нового курса
	      void saveData(String text, int num) {
	    	  
	    	  // Добавляем название курса в таблицу с названиями курсов
	      	db.addMed(text, R.drawable.ic_launcher, num);
	      	// обновляем курсор
	        cursor.requery();
	        	
	        // всплывающее окно покахывающее, что данные сохранены
	        Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
	        
//	        findViewById(R.id.pharmacy).invalidate();
	        
	        // перезапускаем активити, что бы обновился спинер
	        reload();    
	      }	    
	    
	    public void onCreateContextMenu(ContextMenu menu, View v,
	            ContextMenuInfo menuInfo) {
	          super.onCreateContextMenu(menu, v, menuInfo);
	          menu.add(0, CM_DELETE_ID, 0, R.string.delMed);
	          cursor.requery();
	        }
	    	    
	    // удаление лекарства  в контестном меню
	    public boolean onContextItemSelected(MenuItem item) {
	        if (item.getItemId() == CM_DELETE_ID) {
	          // получаем из пункта контекстного меню данные по пункту списка 
	          AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
	          // извлекаем id записи и удаляем соответствующую запись в БД
	          db.delMed(acmi.id);
	          // обновляем курсор
	          cursor.requery();
	          return true;
	        }
	        return super.onContextItemSelected(item);
	      }	    
	    
	    // наше меню, еще не сделанное, может удаление курсов сюда и засунуть
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.pharm_menu, menu);       
	        return true;
	    }
	    
	    // обработка нашего меню
	    public boolean onOptionsItemSelected(MenuItem item) {
	    	
	    	switch (item.getItemId()) {
	        case R.id.pharmacy:
	          Toast.makeText(this, "сейчас пойдем в аптеку", Toast.LENGTH_SHORT).show();
	          Intent intent = new Intent(Pharmacy.this, Pharmacy.class);
	          startActivity(intent);

	          
	          break;
	        case R.id.refresh:
	          Toast.makeText(this, "Обновили", Toast.LENGTH_SHORT).show();
	          reload();
	          break;
	      }
	      return(super.onOptionsItemSelected(item));
	    }	       
	    
	    protected void onDestroy() {
	        super.onDestroy();
	        // закрываем подключение при выходе
	        db.close();
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
