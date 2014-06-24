package com.android.indigo.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class IndigoSQLiteHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "db_notes";
	public static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_NOTES = "notes";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_CONTENT = "content";
	public static final String COLUMN_DATE = "date_created";
	
	public static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NOTES + "(" + 
			COLUMN_ID + "integer PRIMARY KEY AUTOINCREMENT, " + 
			COLUMN_TITLE + " text NOT NULL, " + 
			COLUMN_CONTENT + " text NOT NULL, " + 
			COLUMN_DATE + " date NOT NULL);";
	
	public IndigoSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase sqliteDB) {
		sqliteDB.execSQL(DATABASE_CREATE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase sqliteDB, int oldVer, int newVer) {
		sqliteDB.execSQL("DROP TABLE IF EXITS " + TABLE_NOTES);
		onCreate(sqliteDB);
	}
}