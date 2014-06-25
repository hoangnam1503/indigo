package com.android.indigo.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import com.android.indigo.database.IndigoDatabase;
import com.android.indigo.entity.Note;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
	
	public static final String[] COLUMNS = {COLUMN_ID, COLUMN_TITLE, COLUMN_CONTENT, COLUMN_DATE};
	
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

	public void saveNote(Note note) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		Date now = new Date();
		ContentValues values = new ContentValues();
		values.put(COLUMN_TITLE, note.getTitle());
		values.put(COLUMN_CONTENT, note.getContent());
		values.put(COLUMN_DATE, now.toString());
		
	    db.insertOrThrow(TABLE_NOTES, null, values);
	    db.close();
	}
	
	public Note getNote(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		Cursor cursor = db.query(TABLE_NOTES, COLUMNS, " id = ?", new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		
		Note note = new Note();
		note.setTitle(cursor.getString(1));
		note.setContent(cursor.getString(2));
		note.setDate(cursor.getString(3));
		
		return note;
	}
	
	public ArrayList<Note> getAllNotes() {
		ArrayList<Note> notes = new ArrayList<Note>();
		
		String query = "SELECT * FROM " + TABLE_NOTES;
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		
		Note note = null;
		if (cursor.moveToFirst()) {
			do {
				note = new Note();
				note.setTitle(cursor.getString(1));
				note.setContent(cursor.getString(2));
				note.setDate(cursor.getString(3));
				
				notes.add(note);
			} while (cursor.moveToNext());
		}
		
		return notes;
	}
}