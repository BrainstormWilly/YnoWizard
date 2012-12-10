package com.yno.wizard.controller;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Intent;

public class RestartCommand {
	public static String TAG = RestartCommand.class.getSimpleName();
	public final static String ACTION = "android.intent.action.MAIN";
	
	private WeakReference<Activity> _context;
	//public Bundle payload = new Bundle();
	
	public RestartCommand( Activity $context ){
		_context = new WeakReference<Activity>($context);
	}
	
	public void execute(){
		try{
			
			Intent intent = new Intent( ACTION );
			_context.get().startActivity(intent);
		}catch( Exception $e ){
			$e.printStackTrace();
		}
	}
}
