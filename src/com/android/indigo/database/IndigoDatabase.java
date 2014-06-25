package com.android.indigo.database;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.android.indigo.entity.Note;
import com.android.indigo.helper.IndigoSQLiteHelper;

public class IndigoDatabase {
	public Context mContext;
	public IndigoSQLiteHelper indigoSQLiteHelper;
	public SQLiteDatabase sqLiteDatabase;
	
	public IndigoDatabase (Context context) {
		this.mContext = context;
		indigoSQLiteHelper = new IndigoSQLiteHelper(context);
	}
	
	public IndigoDatabase open() {
		sqLiteDatabase = indigoSQLiteHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		indigoSQLiteHelper.close();
	}
	
	public boolean deleteNote(int id) {
		return sqLiteDatabase.delete(indigoSQLiteHelper.TABLE_NOTES, indigoSQLiteHelper.COLUMN_ID + "=" + id, null) > 0;
	}

	public void saveNote(Note note) {
		Date now = new Date();
		ContentValues values = new ContentValues();
		values.put(indigoSQLiteHelper.COLUMN_TITLE, note.getTitle());
		values.put(indigoSQLiteHelper.COLUMN_CONTENT, note.getContent());
		values.put(indigoSQLiteHelper.COLUMN_DATE, now.toString());
	    sqLiteDatabase.insertOrThrow(indigoSQLiteHelper.TABLE_NOTES, null, values);
	}
}
