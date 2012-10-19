package com.ynotasting.finder.model.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class VarietalTable {

	public static final String TAG = VarietalTable.class.getSimpleName();
	public static final String TABLE_VARIETALS = "varietals";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_VAR_NAME = "var_name";
	public static final String COLUMN_VAR_TYPE = "var_type";
	public static final String COLUMN_VAR_ID = "var_id";
	
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_VARIETALS
			+ "("
			+ COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_VAR_NAME + " text not null,"
			+ COLUMN_VAR_TYPE + " text not null,"
			+ COLUMN_VAR_ID + " text not null"
			+ ");";
	
	public static void onCreate(SQLiteDatabase $db){
		$db.execSQL( DATABASE_CREATE );
	}
	
	public static void onUpgrade( SQLiteDatabase $db, int $old, int $new ){
		Log.w(TAG, "Upgrading database from " + $old + " to " + $new + ". All data will be lost." );
		$db.execSQL( "DROP TABLE IF EXISTS " + TABLE_VARIETALS );
		onCreate( $db );
	}
}
