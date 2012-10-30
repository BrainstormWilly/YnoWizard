package com.yno.wizard.controller;

import com.yno.wizard.model.WineParcel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class StartFacebookRatingCommand {
	
	public static final String ACTION = "com.yno.wizard.intent.START_FB_RATING";
	
	private Context _context;
	
	public Bundle payload = new Bundle();
	
	public StartFacebookRatingCommand( Context $context ){
		_context = $context;
	}
	
	public void execute(){
		Intent intent = new Intent( ACTION );
		intent.putExtra(WineParcel.NAME, payload.getParcelable(WineParcel.NAME));
		_context.startActivity(intent);
	}
	

}
