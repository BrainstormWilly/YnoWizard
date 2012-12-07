package com.yno.wizard.controller;

import java.lang.ref.WeakReference;

import android.app.Activity;
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
	private WeakReference<Activity> _context;
	
	public DoWineSelectCommand( Activity $context ){
		_context = new WeakReference<Activity>($context);
	}
	
	public void execute(){
		try{
			Activity ctx = _context.get();
			Intent intent = new Intent( ACTION );
			intent.putExtra( WineParcel.NAME, payload.getParcelable(WineParcel.NAME) );
			ctx.startActivity(intent);
		}catch( Exception $e ){
			$e.printStackTrace();
		}
	}

}
