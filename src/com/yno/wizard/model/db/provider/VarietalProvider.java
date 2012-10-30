package com.yno.wizard.model.db.provider;

import java.util.Arrays;
import java.util.HashSet;

import com.yno.wizard.model.db.VarietalTable;
import com.yno.wizard.model.db.YnoDbOpenHelper;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class VarietalProvider extends ContentProvider {
	
	private static final String _AUTHORITY = "com.ynotasting.finder.model.db.provider";
	private static final String _BASE = "yww_varietals";
	private static final int _VARIETALS = 10;
	private static final int _VARIETALS_ID = 20;
	
	public static final Uri CONTENT_URI = Uri.parse("content://" + _AUTHORITY + "/" + _BASE);
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/yww_varietals";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/yww_varietal";
	
	private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
	
	static {
		URI_MATCHER.addURI(_AUTHORITY, _BASE, _VARIETALS);
		URI_MATCHER.addURI(_AUTHORITY, _BASE + "/#", _VARIETALS_ID);
	}
	
	private YnoDbOpenHelper _db;
	
	

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = URI_MATCHER.match(uri);
		SQLiteDatabase db = _db.getWritableDatabase();
		int rowsDeleted = 0;
		switch(uriType){
		case _VARIETALS :
			rowsDeleted = db.delete(VarietalTable.TABLE_VARIETALS, selection, selectionArgs);
			break;
		case _VARIETALS_ID :
			String id = uri.getLastPathSegment();
			if( TextUtils.isEmpty(selection)){
				rowsDeleted = db.delete(VarietalTable.TABLE_VARIETALS, VarietalTable.COLUMN_ID + "=" + id, null);
			}else{
				rowsDeleted = db.delete(VarietalTable.TABLE_VARIETALS, VarietalTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
			}
			break;
		default :
			throw new IllegalArgumentException( "Unknown URI: " + uri );
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = URI_MATCHER.match(uri);
		SQLiteDatabase db = _db.getWritableDatabase();
		int rowsDeleted = 0;
		long id = 0;
		switch(uriType){
		case _VARIETALS :
			id = db.insert(VarietalTable.TABLE_VARIETALS, null, values);
			break;
		default :
			throw new IllegalArgumentException( "Unknown URI: " + uri );
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(_BASE + "/" + id);
	}

	@Override
	public boolean onCreate() {
		_db = new YnoDbOpenHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		checkColumns( projection );
		qb.setTables(VarietalTable.TABLE_VARIETALS);
		
		int uriType = URI_MATCHER.match(uri);
		switch(uriType){
		case _VARIETALS :
			break;
		case _VARIETALS_ID :
			qb.appendWhere( VarietalTable.COLUMN_ID + "=" + uri.getLastPathSegment() );
			break;
		default :
			throw new IllegalArgumentException( "Unknown URI: " + uri );
		}
		
		SQLiteDatabase db = _db.getWritableDatabase();
		Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int uriType = URI_MATCHER.match(uri);
		SQLiteDatabase db = _db.getWritableDatabase();
		int rowsUpdated = 0;
		switch(uriType){
		case _VARIETALS :
			rowsUpdated = db.update(VarietalTable.TABLE_VARIETALS, values, selection, selectionArgs);
			break;
		case _VARIETALS_ID :
			String id = uri.getLastPathSegment();
			if( TextUtils.isEmpty(selection)){
				rowsUpdated = db.update(VarietalTable.TABLE_VARIETALS, values, VarietalTable.COLUMN_ID + "=" + id, null);
			}else{
				rowsUpdated = db.update(VarietalTable.TABLE_VARIETALS, values, VarietalTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
			}
			break;
		default :
			throw new IllegalArgumentException( "Unknown URI: " + uri );
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}
	
	private void checkColumns(String[] projection) {
		String[] available = { VarietalTable.COLUMN_ID, VarietalTable.COLUMN_VAR_NAME, VarietalTable.COLUMN_VAR_TYPE };
		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException("Unknown columns in projection");
			}
		}
	}

}
