package com.liberum.medtime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DB {
  
  private static final String DB_NAME = "mydb";
  private static final int DB_VERSION = 1;
  private static final String DB_TABLE = "mytab";
  
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_IMG = "img";
  public static final String COLUMN_NUM = "num";
  public static final String COLUMN_TXT = "txt";
//  public static final String COLUMN_NAME_COURSE = "nCourse";
  
  public static final String DB_TABLE_MED = "tabMed";
  public static final String COLUMN_IMG_MED = "imgMed";
  public static final String COLUMN_NUM_MED = "numMed";
  public static final String COLUMN_NAME_MED = "nameMed";

  public static final String DB_TABLE_COURSE = "tabCourse";
  public static final String COLUMN_IMG_COURSE = "imgCourse";
  public static final String COLUMN_NUM_COURSE = "numCourse";
  public static final String COLUMN_NAME_COURSE = "nameCourse";
  
  public static final String DB_TABLE_COURSEMED = "courseMed";
  public static final String ID_COURSE = "idCourse";
  public static final String ID_MED = "idMed";
  
  private static final String CREATE_COURSE_MED = 
		"create table " + DB_TABLE_COURSEMED + "(" + 
		COLUMN_ID + " integer primary key autoincrement, " + 
		ID_COURSE  + " integer references " + DB_TABLE_COURSE + " ON DELETE CASCADE, " +
		ID_MED + " integer references " + DB_TABLE_MED + " ON DELETE CASCADE" +
		")";
  
  private static final String CREATE_MED = 
		"create table " + DB_TABLE_MED + "(" + 
		COLUMN_ID + " integer primary key autoincrement, " + 
		COLUMN_IMG_MED  + " integer, " + 
		COLUMN_NAME_MED + " text, " +
		COLUMN_NUM_MED + " integer " + 
		")";
  
  private static final String CREATE_COURSE = 
		"create table " + DB_TABLE_COURSE + "(" + 
		COLUMN_ID + " integer primary key autoincrement, " + 
		COLUMN_IMG_COURSE  + " integer, " + 
		COLUMN_NAME_COURSE + " text, " +
		COLUMN_NUM_COURSE + " integer " + 
		")";
  
  
  String[] columns = null;
  String selection = null;
  String[] selectionArgs = null;
  String groupBy = null;
  String having = null;
  String orderBy = null;
  String getNameCourse = null;
  
  private static final String DB_CREATE = 
    "create table " + DB_TABLE + "(" +
      COLUMN_ID + " integer primary key autoincrement, " +
      COLUMN_IMG + " integer, " +
      COLUMN_TXT + " text, " +
      COLUMN_NUM + " integer, " +
      COLUMN_NAME_COURSE + " text " +
    ");";
  

  
  private final Context mCtx;
  
  
  private DBHelper mDBHelper;
  private SQLiteDatabase mDB;
  
  public DB(Context ctx) {
    mCtx = ctx;
  }
  
  // ������� �����������
  public void open() {
    mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
    mDB = mDBHelper.getWritableDatabase();
  }
  
  // ������� �����������
  public void close() {
    if (mDBHelper!=null) mDBHelper.close();
  }
  
  // �������� ��� ������ �� ������� DB_TABLE
  public Cursor getAllData() {
    return mDB.query(DB_TABLE, null, null, null, null, null, null);
  }
  
  // ������ ������� ��������
  public Cursor getAllMed() {
	    return mDB.query(DB_TABLE_MED, null, null, null, null, null, null);
	  }
  
  
  //��� ����� �������� ������ ������
  public Cursor allCourse(){
	  return mDB.query(DB_TABLE_COURSE, null, null, null, null, null, null);
  }
  
  public Cursor allCourseMed(){
	  return mDB.query(DB_TABLE_COURSEMED, null, null, null, null, null, null);
  }
  
  public Cursor viewCourse(String idView) {
	  String[] courseId = {ID_MED};
	  String idView1 = idView;
	  return mDB.query(DB_TABLE_COURSEMED, null, idView1, null, null, null, null);
  }
  
  
  // �������� ������ � DB_TABLE
  public void addRec(String txt, int img, int num, String nCourse) {
    ContentValues cv = new ContentValues();
    cv.put(COLUMN_TXT, txt);
    cv.put(COLUMN_IMG, img);
    cv.put(COLUMN_NUM, num);
    cv.put(COLUMN_NAME_COURSE, nCourse);
    mDB.insert(DB_TABLE, null, cv);
  }
  
  // �������� �������� ����� � ������� ������
  public void addCourse(String txt, int img) {
	  ContentValues cv = new ContentValues();
	  cv.put(COLUMN_NAME_COURSE, txt);
	  cv.put(COLUMN_IMG_COURSE, img);
	  mDB.insert(DB_TABLE_COURSE, null, cv);
  }
  
  // �������� ���������
  public void addMed(String txt, int img, int num) {
	  ContentValues cv = new ContentValues();
	  cv.put(COLUMN_NAME_MED, txt);
	  cv.put(COLUMN_IMG_MED, img);
	  cv.put(COLUMN_NUM_MED, num);
	  mDB.insert(DB_TABLE_MED, null, cv);
  }
  
  // �������� ��������� ������������� �����
  public void addMedourse(int idCourse, int idMed) {
	  ContentValues cv = new ContentValues();
	  cv.put(ID_COURSE, idCourse);
	  cv.put(ID_MED, idMed);
	  mDB.insert(DB_TABLE_COURSEMED, null, cv);
  }
  
  // ������� ������ �� DB_TABLE
  public void delRec(long id) {
    mDB.delete(DB_TABLE, COLUMN_ID + " = " + id, null);
  }
  
  // ������� ���������
  public void delMed(long id) {
    mDB.delete(DB_TABLE_MED, COLUMN_ID + " = " + id, null);
  }
  
  // ������� ����
  public void delCourse(long id) {
	  mDB.delete(DB_TABLE_COURSE, COLUMN_ID + "=" + id, null);
  }
  
  // ����� �� �������� � ���������� ��
  private class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name, CursorFactory factory,
        int version) {
      super(context, name, factory, version);
    }

    // ������� � ��������� ��
    @Override
    public void onCreate(SQLiteDatabase db) {
//    	db.execSQL("PRAGMA foreign_keys=ON");
      db.execSQL(DB_CREATE);
      db.execSQL(CREATE_MED);
      db.execSQL(CREATE_COURSE);
      db.execSQL(CREATE_COURSE_MED);
      
      ContentValues cv = new ContentValues();
      for (int i = 0; i < 5; i++) {  	  
        cv.put(COLUMN_TXT, "��������� ");
        cv.put(COLUMN_IMG, R.drawable.ic_launcher);
        cv.put(COLUMN_NUM, i);
        cv.put(COLUMN_NAME_COURSE, "���� ����");
        db.insert(DB_TABLE, null, cv);
      }
      
      ContentValues cvMed = new ContentValues();
      for (int i = 0; i < 5; i++) {  	  
        cvMed.put(COLUMN_NAME_MED, "�����-�� ��������� ");
        cvMed.put(COLUMN_IMG_MED, R.drawable.ic_launcher);
        cvMed.put(COLUMN_NUM_MED, i);
        db.insert(DB_TABLE_MED, null, cvMed);
      }
      
      ContentValues cvCourse = new ContentValues();
	  
        cvCourse.put(COLUMN_NAME_COURSE, "��� �����");
        cvCourse.put(COLUMN_IMG_COURSE, R.drawable.ic_launcher);
        cvCourse.put(COLUMN_NUM_COURSE, "0");
        db.insert(DB_TABLE_COURSE, null, cvCourse);
      
      
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
  }
}
