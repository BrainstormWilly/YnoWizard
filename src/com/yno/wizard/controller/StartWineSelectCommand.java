package com.yno.wizard.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Messenger;

import com.yno.wizard.model.SearchWineParcel;
import com.yno.wizard.model.service.SearchData;
import com.yno.wizard.model.service.WineSelectSearchService;

public class StartWineSelectCommand {

	public final static String TAG = StartWineSelectCommand.class.getSimpleName();
	
	public Bundle payload = new Bundle();
	public Messenger messenger;
	private Context _context;
	
	public StartWineSelectCommand( Context $context ){
		_context = $context;
	}
	
	public void execute(){
		try{
			Intent intent = new Intent( _context, WineSelectSearchService.class );
			intent.putExtra("android.os.Messenger", messenger);
			intent.putExtra(SearchWineParcel.NAME, payload.getParcelable(SearchWineParcel.NAME));
			_context.startService(intent);
		}catch( Exception $e ){
			$e.printStackTrace();
		}
	}
}
