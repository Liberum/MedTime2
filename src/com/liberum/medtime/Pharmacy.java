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
	
	// ����������� ����������
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
		  
	        // ��������� ����������� � ��
	        db = new DB(this);
	        db.open();	        
	        
	        // �������� ������
	        cursor = db.getAllMed();
	        startManagingCursor(cursor);
	                
	        // ��������� ������� �������������
	        String[] from = new String[] { DB.COLUMN_IMG_MED, DB.COLUMN_NAME_MED, DB.COLUMN_NUM_MED};
	        int[] to = new int[] { R.id.ivImg, R.id.tvText, R.id.tvNum};
	        
	        // �������� ������� � ����������� ������
	        scAdapter = new SimpleCursorAdapter(this, R.layout.item, cursor, from, to);
	        lvPharmacy = (ListView) findViewById(R.id.lvPharmacy);
	        lvPharmacy.setAdapter(scAdapter);
	        
	        // ��������� ����������� ���� � ������
	        registerForContextMenu(lvPharmacy);  
	  }
	  
	  // ������� ���������� ������ ��������� 
	  
	  
	  
	    // ��������� ������� ������ ���������� �����
	    public void onButtonClickMed(View view) {
	        // �������� ������
	        showDialog(DIALOG_EXIT);    

	    }	    
	    
	    //������ ���������� �����
	    protected Dialog onCreateDialog(int id) {
	        if (id == DIALOG_EXIT) {

	          AlertDialog.Builder adb = new AlertDialog.Builder(this);
	          // ���������
	          adb.setTitle(R.string.addMed);
	          // ���������
//	          adb.setMessage(R.string.med_data);
	          // ������
	          adb.setIcon(android.R.drawable.ic_dialog_info);
	          // ������ �������������� ������
	          adb.setPositiveButton(R.string.yes, myClickListener);
	          // ������ �������������� ������
	          adb.setNegativeButton(R.string.no, myClickListener);
	          // ������� view �� dialog.xml
	          view = (LinearLayout) getLayoutInflater().inflate(R.layout.add_med, null);
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
	        	  int num2 = 0;
	        	  EditText text = (EditText)((AlertDialog)dialog).findViewById(R.id.new_med);		// �������� �������� ��������
	        	  EditText num = (EditText)((AlertDialog)dialog).findViewById(R.id.numMed);		// �������� �������� ����������
	        	  String num1 = num.getText().toString();
	        	  
	        	  // ���� ������ �� ������� ����� �������� ����, ���� ������� ��������������� � ���
	              try {
	            	  num2 = Integer.parseInt(num1);
	              } catch (Exception e) {
	                  num2 = 0;
	              }
	        	  
	        	  saveData(text.getText().toString(), num2);				// ����� ���� ����� ������ � ������ � ���
	        	  
	            break;
	            	// ���������� ������
	          	case Dialog.BUTTON_NEGATIVE:
	            break;

	          }
	        }
	      };
	      
	      // ��������� ���������� ������ �����
	      void saveData(String text, int num) {
	    	  
	    	  // ��������� �������� ����� � ������� � ���������� ������
	      	db.addMed(text, R.drawable.ic_launcher, num);
	      	// ��������� ������
	        cursor.requery();
	        	
	        // ����������� ���� ������������, ��� ������ ���������
	        Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
	        
//	        findViewById(R.id.pharmacy).invalidate();
	        
	        // ������������� ��������, ��� �� ��������� ������
	        reload();    
	      }	    
	    
	    public void onCreateContextMenu(ContextMenu menu, View v,
	            ContextMenuInfo menuInfo) {
	          super.onCreateContextMenu(menu, v, menuInfo);
	          menu.add(0, CM_DELETE_ID, 0, R.string.delMed);
	          cursor.requery();
	        }
	    	    
	    // �������� ���������  � ���������� ����
	    public boolean onContextItemSelected(MenuItem item) {
	        if (item.getItemId() == CM_DELETE_ID) {
	          // �������� �� ������ ������������ ���� ������ �� ������ ������ 
	          AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
	          // ��������� id ������ � ������� ��������������� ������ � ��
	          db.delMed(acmi.id);
	          // ��������� ������
	          cursor.requery();
	          return true;
	        }
	        return super.onContextItemSelected(item);
	      }	    
	    
	    // ���� ����, ��� �� ���������, ����� �������� ������ ���� � ��������
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.pharm_menu, menu);       
	        return true;
	    }
	    
	    // ��������� ������ ����
	    public boolean onOptionsItemSelected(MenuItem item) {
	    	
	    	switch (item.getItemId()) {
	        case R.id.pharmacy:
	          Toast.makeText(this, "������ ������ � ������", Toast.LENGTH_SHORT).show();
	          Intent intent = new Intent(Pharmacy.this, Pharmacy.class);
	          startActivity(intent);

	          
	          break;
	        case R.id.refresh:
	          Toast.makeText(this, "��������", Toast.LENGTH_SHORT).show();
	          reload();
	          break;
	      }
	      return(super.onOptionsItemSelected(item));
	    }	       
	    
	    protected void onDestroy() {
	        super.onDestroy();
	        // ��������� ����������� ��� ������
	        db.close();
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
