package com.yno.wizard.controller;

import java.lang.ref.WeakReference;

import com.yno.wizard.view.ManualEntryActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class OpenManualEntryCommand {

	public final static String ACTION = "com.yno.wizard.intent.OPEN_MANUAL_ENTRY";
	
	public Bundle payload = new Bundle();
	private WeakReference<Activity> _context;
	
	public OpenManualEntryCommand( Activity $context ){
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
