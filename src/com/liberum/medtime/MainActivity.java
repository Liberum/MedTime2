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
	  
	  String setCourse = "�� ������ :(";
	  	  
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // ������� 
        vievCourse();
        
        // ������ ������
        mySpiner();
                
    }
    
 // ������ ��� �����
    public void allItem() {
        // ��������� ����������� � ��
        db = new DB(this);
        db.open();

        // �������� ������
        cursor = db.getAllData();
        startManagingCursor(cursor);
        
        // ��������� ������� �������������
        String[] from = new String[] { DB.COLUMN_IMG, DB.COLUMN_TXT, DB.COLUMN_NUM, DB.COLUMN_NAME_COURSE };
        int[] to = new int[] { R.id.ivImg, R.id.tvText, R.id.tvNum, R.id.nCour };

        // �������� ������� � ����������� ������
        scAdapter = new SimpleCursorAdapter(this, R.layout.item, cursor, from, to);
        lvData = (ListView) findViewById(R.id.lvData);
        lvData.setAdapter(scAdapter);

        // ��������� ����������� ���� � ������
        registerForContextMenu(lvData);  
    }
    
    // ������ ��� ��������� ������ �����
    public void CourseItem() {
        // ��������� ����������� � ��
        db = new DB(this);
        db.open();
        
        String mCourse = Integer.toString(idCourse);
        // �������� ������
        cursor = db.viewCourse(mCourse);
        startManagingCursor(cursor);
        
        // ��������� ������� �������������
        String[] from = new String[] {DB.ID_COURSE, DB.ID_MED};
        int[] to = new int[] {R.id.nCour, R.id.tvText};

        // �������� ������� � ����������� ������
        scAdapter = new SimpleCursorAdapter(this, R.layout.item, cursor, from, to);
        lvData = (ListView) findViewById(R.id.lvData);
        lvData.setAdapter(scAdapter);

        // ��������� ����������� ���� � ������
        registerForContextMenu(lvData);  
    }
    
    
    // ����������� ��������� ����� 
    public void vievCourse() {
        // ��������� ����������� � ��
        db = new DB(this);
        db.open();
        
        // ������������ ������� �������
        // �������� ������ ������
        cursor = db.allCourse();
        // ������ ������ �� ������ �������
    	cursor.moveToFirst();    	
    	ArrayList<String> dbCourse = new ArrayList<String>();
    	int nameCourse = cursor.getColumnIndex(DB.COLUMN_NAME_COURSE);
        do {
            // �������� �������� �� ������� �������� � ���������� � ���������
            dbCourse.add(cursor.getString(nameCourse));
            // ������� �� ��������� ������ 
            // � ���� ��������� ��� (������� - ���������), �� false - ������� �� �����
          } while (cursor.moveToNext());              
        	// �������� � ArrayList ���� ������� �� ��������, ��������� idCourse, ������� ��� ��������� ���������� �������
        	setCourse = dbCourse.get(idCourse);        
        // ������� id ������ � ������� � � ��� ��������..
        Toast toast = Toast.makeText(getApplicationContext(), 
      		  "ID ����� = " + idCourse + " �������� = " + setCourse, Toast.LENGTH_SHORT); 
      		toast.show(); 
      		
      		// ���������� ��� ��� ����� ��� ������ ��������
      		if (idCourse == 0) {
      			allItem();
      		} else {
      			// ��������� ����� � ������ ������� 
      			CourseItem();

      		}

        // ��������� ����������� ���� � ������
//        registerForContextMenu(lvData);  
    }
    
    
    public void mySpiner() {
    	
    	//��� ����� � DB.java ������� �������� ������ ���� ���� )
    	cursor = db.allCourse();
    	
        // �������� ������ ������
    	cursor.moveToFirst();
        ArrayList<String> dbCourse = new ArrayList<String>();
        int nameCourse = cursor.getColumnIndex(DB.COLUMN_NAME_COURSE);
        do {
            // �������� �������� �� ������� �������� � ���������� � ���������
            dbCourse.add(cursor.getString(nameCourse));
            // ������� �� ��������� ������ 
            // � ���� ��������� ��� (������� - ���������), �� false - ������� �� �����
          } while (cursor.moveToNext());
        
        // ��������� ������� � ����� ����
        ActionBar bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbCourse);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bar.setListNavigationCallbacks(adapter, this);
        
        //��� �� ������ ��������� ������ ����� ������ ��, �������� � �����
        adapter.notifyDataSetChanged();        

    }
    
    // ��������� ������� ������ ���������� �����
    public void onButtonClick(View view) {
        // �������� ������
        showDialog(DIALOG_EXIT);    

    }
    
    public void onCreateContextMenu(ContextMenu menu, View v,
        ContextMenuInfo menuInfo) {
      super.onCreateContextMenu(menu, v, menuInfo);
      menu.add(0, CM_DELETE_ID, 0, R.string.delCourse);
      cursor.requery();
    }
    
    
    // �������� ����� � ���������� ����
    public boolean onContextItemSelected(MenuItem item) {
      if (item.getItemId() == CM_DELETE_ID) {
        // �������� �� ������ ������������ ���� ������ �� ������ ������ 
        AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
        // ��������� id ������ � ������� ��������������� ������ � ��
        db.delRec(acmi.id);
        // ��������� ������
        cursor.requery();
        reload();
        return true;
      }

      return super.onContextItemSelected(item);
      
    }
    
    protected void onDestroy() {
      super.onDestroy();
      // ��������� ����������� ��� ������
      db.close();
    }
    
    // ���������� ������� � ����� ����
    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {    	
      idCourse = (int)itemId;
      vievCourse(); // ��� ������� ���������� ������ ����, id �������� ����� ����� id � �������    		
      return false;
    }

    // ���� ����
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);       
        return true;
    }
    
    // ��������� ������ ����
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch (item.getItemId()) {
        case R.id.pharmacy:
          Toast.makeText(this, "������ ������ � ������", Toast.LENGTH_SHORT).show();
          Intent intent = new Intent(MainActivity.this, Pharmacy.class);
          startActivity(intent);          
          break;
          
        case R.id.add_med:
            Toast.makeText(this, "����� ��������", Toast.LENGTH_SHORT).show();
            add_med();
            break;
          
        case R.id.refresh:
          Toast.makeText(this, "��������", Toast.LENGTH_SHORT).show();
          reload();
          break;
      }
      return(super.onOptionsItemSelected(item));
    }
    
    private void add_med() {
		// TODO Auto-generated method stub
		
	}

	//������ ���������� �����
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_EXIT) {

          AlertDialog.Builder adb = new AlertDialog.Builder(this);
          // ���������
          adb.setTitle(R.string.addCourse);
          // ���������
          adb.setMessage(R.string.course_data);
          // ������
          adb.setIcon(android.R.drawable.ic_dialog_info);
          // ������ �������������� ������
          adb.setPositiveButton(R.string.yes, myClickListener);
          // ������ �������������� ������
          adb.setNegativeButton(R.string.no, myClickListener);
          // ������� view �� dialog.xml
          view = (LinearLayout) getLayoutInflater().inflate(R.layout.add_course, null);
          // ������������� ��, ��� ���������� ���� �������
          adb.setView(view);
          // ������� ������
          return adb.create();
              	
        }
        return super.onCreateDialog(id);
      }
    
    OnClickListener myClickListener = new OnClickListener() {
    public void onClick(DialogInterface dialog, int which) {
      switch (which) {
      // ������������� ������
      case Dialog.BUTTON_POSITIVE:
    	  EditText text = (EditText)((AlertDialog)dialog).findViewById(R.id.new_course); 	// �������� ��������
    	  saveData(text.getText().toString());												// ��������� � ������
        break;
      // ���������� ������
      case Dialog.BUTTON_NEGATIVE:
        break;

      }
    }
  };
  
  // ��������� ���������� ������ �����
  void saveData(String text) {
	  
	  // ��������� �������� ����� � ������� � ���������� ������
  	db.addCourse(text, R.drawable.ic_launcher);
  	// ��������� ������
    cursor.requery();
    	
    // ����������� ���� ������������, ��� ������ ���������
    Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
    
    findViewById(R.id.root).invalidate();
    
    // ������������� ��������, ��� �� ��������� ������
    reload();    
  }
  
  
  //���������� ��������
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
