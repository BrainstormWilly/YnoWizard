package com.yno.wizard.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Messenger;

import com.yno.wizard.model.SearchWinesParcel;
import com.yno.wizard.model.service.WinesPhraseSearchService;

public class DoPhraseSearchCommand {
	
	public final static String TAG = DoPhraseSearchCommand.class.getSimpleName();
	
	public Bundle payload = new Bundle();
	public Messenger messenger;
	private Context _context;
	
	public DoPhraseSearchCommand( Context $context ){
		_context = $context;
	}
	
	public void execute(){
		try{
			Intent intent = new Intent( _context, WinesPhraseSearchService.class );
			intent.putExtra("android.os.Messenger", messenger);
			intent.putExtra(SearchWinesParcel.NAME, payload.getParcelable(SearchWinesParcel.NAME));
			_context.startService(intent);
		}catch( Exception $e ){
			$e.printStackTrace();
		}
	}

}
