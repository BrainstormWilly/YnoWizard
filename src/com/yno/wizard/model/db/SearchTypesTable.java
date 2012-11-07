package com.yno.wizard.model.db;

import android.database.sqlite.SQLiteDatabase;

public class SearchTypesTable {

	public static final String TAG = SearchTypesTable.class.getSimpleName();
	public static final String TABLE_SEARCH_TYPES = "search_types";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_WINECOM_ID = "wc_id";
	
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_SEARCH_TYPES
			+ "("
			+ COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_NAME + " text not null,"
			+ COLUMN_WINECOM_ID + " integer not null"
			+ ");";
	
	public static void onCreate(SQLiteDatabase $db){
		$db.execSQL( DATABASE_CREATE );
	}
	
	public static void onUpgrade( SQLiteDatabase $db, int $old, int $new ){
		//Log.w(TAG, "Upgrading database from " + $old + " to " + $new + ". All data will be lost." );
		$db.execSQL( "DROP TABLE IF EXISTS " + TABLE_SEARCH_TYPES );
		onCreate( $db );
	}
}
