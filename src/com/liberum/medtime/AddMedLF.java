package com.liberum.medtime;

import java.util.ArrayList;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;




public class AddMedLF extends ListFragment implements OnClickListener {
	
	DB db;
	Cursor cursor;
	
	CheckBox cbMed;
	ListView lvPharmacy;
	SimpleCursorAdapter scPharmacy;
	
	  @Override
	  public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    
	    db = new DB(getActivity());
	    db.open();
	    
        // получаем курсор
        cursor = db.getAllMed();
                
        // получаем список лекарств
    	cursor.moveToFirst();
        ArrayList<String> data = new ArrayList<String>();
        int nameCourse = cursor.getColumnIndex(DB.COLUMN_NAME_MED);
        // все лекарства в массив
        do {
            // получаем значения по номерам столбцов и засовываем в аррейлист
            data.add(cursor.getString(nameCourse));
            // переход на следующую строку 
            // а если следующей нет (текущая - последняя), то false - выходим из цикла
          } while (cursor.moveToNext());
        // размещаем в фрагмент листе в встроенном списке с множественным выбором        
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_multiple_choice, data);
	    setListAdapter(adapter);
	  }

	@Override
	public void onClick(View v) {
		Toast.makeText(getActivity(), "Нажато", Toast.LENGTH_SHORT).show();
		
	}

}
