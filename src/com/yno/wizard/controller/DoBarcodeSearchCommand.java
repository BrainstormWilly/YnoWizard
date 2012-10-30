package com.yno.wizard.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Messenger;
import android.util.Log;

import com.yno.wizard.model.SearchWineParcel;
import com.yno.wizard.model.service.WineBarcodeSearchService;

public class DoBarcodeSearchCommand {

	//public final static String ACTION = "com.ynotasting.finder.intent.DO_BARCODE_SEARCH";
	
	public Bundle payload = new Bundle();
	public Messenger messenger;
	
	private Context _context;
	
	public DoBarcodeSearchCommand( Context $context ){
		_context = $context;
	}
	
	public void execute(){
		try{
			Intent intent = new Intent( _context, WineBarcodeSearchService.class );
			intent.putExtra( "android.os.Messenger", messenger );
			intent.putExtra( SearchWineParcel.NAME, payload.getParcelable( SearchWineParcel.NAME ) );
			_context.startService(intent);
		}catch( Exception $e ){
			$e.printStackTrace();
		}
	}
}
