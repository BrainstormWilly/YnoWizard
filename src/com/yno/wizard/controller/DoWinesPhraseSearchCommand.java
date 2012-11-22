package com.yno.wizard.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Messenger;

import com.yno.wizard.model.SearchWineParcel;
import com.yno.wizard.model.SearchWinesParcel;
import com.yno.wizard.model.service.WineBarcodeSearchService;
import com.yno.wizard.model.service.WinesPhraseSearchService;

public class DoWinesPhraseSearchCommand {

	public Bundle payload = new Bundle();
	public Messenger messenger;
	
	private Context _context;
	
	public DoWinesPhraseSearchCommand( Context $context ){
		_context = $context;
	}
	
	public void execute(){
		try{
			Intent intent = new Intent( _context, WinesPhraseSearchService.class );
			intent.putExtra( "android.os.Messenger", messenger );
			intent.putExtra( SearchWinesParcel.NAME, payload.getParcelable( SearchWinesParcel.NAME ) );
			_context.startService(intent);
		}catch( Exception $e ){
			$e.printStackTrace();
		}
	}
	
}
