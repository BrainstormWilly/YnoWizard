package com.ynotasting.finder.controller;

import com.ynotasting.finder.view.TextSearchActivity;

import android.content.Context;
import android.content.Intent;
//import android.os.Bundle;

public class OpenPhraseSearchCommand {

	public static String TAG = OpenPhraseSearchCommand.class.getSimpleName();
	
	private Context _context;
	//public Bundle payload = new Bundle();
	
	public OpenPhraseSearchCommand( Context $context ){
		_context = $context;
	}
	
	public void execute(){
		Intent intent = new Intent( TextSearchActivity.NAME );
		_context.startActivity(intent);
	}
}
