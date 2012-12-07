package com.yno.wizard.controller;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
//import android.os.Bundle;

public class OpenPhraseSearchCommand {

	public static String TAG = OpenPhraseSearchCommand.class.getSimpleName();
	public final static String ACTION = "com.yno.wizard.intent.OPEN_PHRASE_SEARCH";
	
	private WeakReference<Activity> _context;
	//public Bundle payload = new Bundle();
	
	public OpenPhraseSearchCommand( Activity $context ){
		_context = new WeakReference<Activity>($context);
	}
	
	public void execute(){
		Activity ctx = _context.get();
		if( ctx==null )
			return;
		Intent intent = new Intent( ACTION );
		ctx.startActivity(intent);
	}
}
