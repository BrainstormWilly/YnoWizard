package com.yno.wizard.controller;

import com.yno.wizard.view.ManualEntryActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class OpenManualEntryCommand {

	public final static String ACTION = "com.yno.wizard.intent.OPEN_MANUAL_ENTRY";
	
	public Bundle payload = new Bundle();
	private Context _context;
	
	public OpenManualEntryCommand( Context $context ){
		_context = $context;
	}
	
	public void execute(){
		Intent intent = new Intent( ACTION );
		_context.startActivity(intent);
	}
}
