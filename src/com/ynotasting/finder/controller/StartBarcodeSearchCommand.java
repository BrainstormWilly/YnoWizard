package com.ynotasting.finder.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Messenger;
import android.util.Log;

import com.ynotasting.finder.model.SearchWineParcel;
import com.ynotasting.finder.model.service.SearchData;

public class StartBarcodeSearchCommand {
	public final static String TAG = StartBarcodeSearchCommand.class.getSimpleName();
	public final static String ACTION = "com.ynotasting.finder.intent.START_BARCODE_SEARCH";
	
	private Context _context;
	
	public Bundle payload = new Bundle();
	public Messenger messenger;
	
	
	public StartBarcodeSearchCommand( Context $context ){
		_context = $context;
	}
	
	public void execute(){
		try{
			
			SearchWineParcel parcel = new SearchWineParcel();
			parcel.type = SearchData.SEARCH_TYPE_BARCODE;
			parcel.query = payload.getString("barcode");
			Intent intent = new Intent( ACTION );
			intent.putExtra(SearchWineParcel.NAME, parcel);
			_context.startActivity(intent);
		}catch( Exception $e ){
			$e.printStackTrace();
		}
	}
	
	
}
