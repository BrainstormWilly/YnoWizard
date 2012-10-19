package com.ynotasting.finder.controller;

import com.ynotasting.finder.view.ChooseSearchActivity;

import android.content.Context;
import android.content.Intent;


public class OpenChooseSearchCommand {

	private Context _context;
	
	public OpenChooseSearchCommand( Context $context ){
		_context = $context;
	}
	
	public void execute(){
		Intent intent = new Intent( ChooseSearchActivity.NAME );
		_context.startActivity(intent);
	}
}
