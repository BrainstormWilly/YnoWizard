package com.yno.wizard.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Messenger;

import com.yno.wizard.model.WineParcel;
import com.yno.wizard.view.IActionBarActivity;

public class DoWineSelectCommand {
	
	public final static String TAG = DoWineSelectCommand.class.getSimpleName();
	public final static String ACTION = "com.yno.wizard.intent.START_WINE_SELECT";
	
	public Bundle payload = new Bundle();
	public Messenger messenger;
	private IActionBarActivity _context;
	
	public DoWineSelectCommand( IActionBarActivity $context ){
		_context = $context;
	}
	
	public void execute(){
		try{
			Intent intent = new Intent( ACTION );
			intent.putExtra( WineParcel.NAME, payload.getParcelable(WineParcel.NAME) );
			_context.startActivity(intent);
		}catch( Exception $e ){
			$e.printStackTrace();
		}
	}

}
