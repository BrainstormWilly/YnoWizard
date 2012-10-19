package com.ynotasting.finder.controller;

import com.ynotasting.finder.model.WineParcel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class StartFacebookRatingCommand {
	
	public static final String ACTION = "com.ynotasting.finder.intent.START_FB_RATING";
	
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
