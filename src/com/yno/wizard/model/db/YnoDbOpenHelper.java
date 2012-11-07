package com.yno.wizard.model.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yno.wizard.model.SearchTypeParcel;

public class YnoDbOpenHelper extends SQLiteOpenHelper {
	
	public static final String TAG = YnoDbOpenHelper.class.getSimpleName();
	private static final String DB_NAME = "yno_shared.db";
	private static final int DB_VERSION = 3;
	
	private SQLiteDatabase _db;
	
	
	public YnoDbOpenHelper( Context $context ){
		super( $context, DB_NAME, null, DB_VERSION );
	}

	@Override
	public void onCreate(SQLiteDatabase $db) {
		SearchTypesTable.onCreate($db);
		//VarietalTable.onCreate($db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase $db, int $old, int $new) {
		//Log.d(TAG, "onUpgrade version " + $old + " to " + $new);
		SearchTypesTable.onUpgrade($db, $old, $new);
		//VarietalTable.onUpgrade($db, $old, $new);
	}
	
	public Cursor getAllFromSearchTypes(){
		closeDB();
		_db = getWritableDatabase();
		Cursor cursor = _db.query(SearchTypesTable.TABLE_SEARCH_TYPES, new String[]{SearchTypesTable.COLUMN_ID, SearchTypesTable.COLUMN_NAME, SearchTypesTable.COLUMN_WINECOM_ID}, null, null, null, null, SearchTypesTable.COLUMN_NAME);
		//db.close();
		return cursor;
	}
	
	public void insertSearchTypes(ArrayList<SearchTypeParcel> $types){
		closeDB();
		_db = getWritableDatabase();
		ContentValues vals;
		for( int a=0, l=$types.size(); a<l; a++ ){
			vals = new ContentValues();
			vals.put(SearchTypesTable.COLUMN_NAME, $types.get(a).name);
			vals.put(SearchTypesTable.COLUMN_WINECOM_ID, $types.get(a).id);
			_db.insert(SearchTypesTable.TABLE_SEARCH_TYPES, null, vals);
		}
	}
	
	public Cursor insertAndReturnSearchTypes(ArrayList<SearchTypeParcel> $types){
		closeDB();
		_db = getWritableDatabase();
		ContentValues vals;
		for( int a=0, l=$types.size(); a<l; a++ ){
			vals = new ContentValues();
			vals.put(SearchTypesTable.COLUMN_NAME, $types.get(a).name);
			vals.put(SearchTypesTable.COLUMN_WINECOM_ID, $types.get(a).id);
			_db.insert(SearchTypesTable.TABLE_SEARCH_TYPES, null, vals);
		}
		return getAllFromSearchTypes();
	}
	
	public void closeDB(){
		if( _db!=null && _db.isOpen() ){
			_db.close();
		}
	}
	
	

}
