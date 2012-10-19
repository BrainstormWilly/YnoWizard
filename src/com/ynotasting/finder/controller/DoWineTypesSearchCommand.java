package com.ynotasting.finder.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Messenger;

import com.ynotasting.finder.model.service.WineTypesSearchService;

public class DoWineTypesSearchCommand {

public final static String TAG = DoWineTypesSearchCommand.class.getSimpleName();
	
	public Bundle payload = new Bundle();
	public Messenger messenger;
	private Context _context;
	
	public DoWineTypesSearchCommand( Context $context ){
		_context = $context;
	}
	
	public void execute(){
		try{
			Intent intent = new Intent( _context, WineTypesSearchService.class );
			intent.putExtra("android.os.Messenger", messenger);
			_context.startService(intent);
		}catch(Exception $e){
			$e.printStackTrace();
		}
	}
}
