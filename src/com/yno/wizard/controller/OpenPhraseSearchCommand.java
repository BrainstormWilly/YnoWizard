package com.yno.wizard.controller;

import com.yno.wizard.view.TextSearchActivity;

import android.content.Context;
import android.content.Intent;
//import android.os.Bundle;

public class OpenPhraseSearchCommand {

	public static String TAG = OpenPhraseSearchCommand.class.getSimpleName();
	public final static String ACTION = "com.yno.wizard.intent.OPEN_PHRASE_SEARCH";
	
	private Context _context;
	//public Bundle payload = new Bundle();
	
	public OpenPhraseSearchCommand( Context $context ){
		_context = $context;
	}
	
	public void execute(){
		Intent intent = new Intent( ACTION );
		_context.startActivity(intent);
	}
}
