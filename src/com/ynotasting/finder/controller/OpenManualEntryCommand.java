package com.ynotasting.finder.controller;

import com.ynotasting.finder.view.ManualEntryActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class OpenManualEntryCommand {

	
	public Bundle payload = new Bundle();
	private Context _context;
	
	public OpenManualEntryCommand( Context $context ){
		_context = $context;
	}
	
	public void execute(){
		Intent intent = new Intent(ManualEntryActivity.NAME);
		_context.startActivity(intent);
	}
}
