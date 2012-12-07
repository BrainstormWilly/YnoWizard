package com.yno.wizard.controller;

import java.lang.ref.WeakReference;

import android.app.Activity;
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
	
	private WeakReference<Activity> _context;
	
	public DoBarcodeSearchCommand( Activity $context ){
		_context = new WeakReference<Activity>($context);
	}
	
	public void execute(){
		try{
			Activity ctx = _context.get();
			Intent intent = new Intent( ctx, WineBarcodeSearchService.class );
			intent.putExtra( "android.os.Messenger", messenger );
			intent.putExtra( SearchWineParcel.NAME, payload.getParcelable( SearchWineParcel.NAME ) );
			ctx.startService(intent);
		}catch( Exception $e ){
			$e.printStackTrace();
		}
	}
}
