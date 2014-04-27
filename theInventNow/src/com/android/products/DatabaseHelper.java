package com.android.products;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	public static final String TABLE_ITEMS = "Items";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_UPC = "UPC";
	public static final String COLUMN_NAME = "Name";
	public static final String COLUMN_AMOUNT = "Amount";
	//public static final String COLUMN_USER = "User";
	public static final String COLUMN_DESC = "Description";
	public static final String COLUMN_DEL = "DeleteFlag";
	
	private static final String DATABASE_NAME = "items.db";
	private static final int DATABASE_VERSION = 1;
	
	//string to create the database
	private static final String DATABASE_CREATE = "CREATE TABLE "
			+ TABLE_ITEMS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_UPC
			+ " TEXT NOT NULL, " + COLUMN_NAME + " TEXT NOT NULL, " + COLUMN_AMOUNT + " INTEGER NOT NULL, "
			+ /*COLUMN_USER + " TEXT UNIQUE, " + */ COLUMN_DESC + " TEXT, " + COLUMN_DEL + " INTEGER DEFAULT 0);";
	
	public DatabaseHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db){
		db.execSQL(DATABASE_CREATE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		Log.w(DatabaseHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
		onCreate(db);
	}
}
