package com.yno.wizard.controller;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Messenger;

import com.yno.wizard.model.service.WineTypesSearchService;

public class DoWineTypesSearchCommand {

public final static String TAG = DoWineTypesSearchCommand.class.getSimpleName();
	
	public Bundle payload = new Bundle();
	public Messenger messenger;
	private WeakReference<Context> _context;
	
	public DoWineTypesSearchCommand( Context $context ){
		_context = new WeakReference<Context>($context);
	}
	
	public void execute(){
		try{
			Context ctx = _context.get();
			Intent intent = new Intent( ctx, WineTypesSearchService.class );
			intent.putExtra("android.os.Messenger", messenger);
			ctx.startService(intent);
		}catch(Exception $e){
			$e.printStackTrace();
		}
	}
}
