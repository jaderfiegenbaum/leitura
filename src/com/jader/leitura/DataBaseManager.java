package com.jader.leitura;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
 
public class DataBaseManager extends SQLiteOpenHelper {
 
	private String createTableSQL = null;
	private String dropTableSQL = null;
 
	public DataBaseManager(Context context, String dataBaseName, int version, String createTableSQL, String dropTableSQL) {
		super(context, dataBaseName, null, version);
 
		this.createTableSQL = createTableSQL;
		this.dropTableSQL = dropTableSQL;
	}
 
	public DataBaseManager(Context context, String name, CursorFactory factory,	int version) {
		super(context, name, factory, version);
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(createTableSQL);
 
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(dropTableSQL);
        onCreate(db);
	}
}

