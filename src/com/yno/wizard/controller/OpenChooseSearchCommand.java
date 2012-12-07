package com.yno.wizard.controller;

import java.lang.ref.WeakReference;

import com.yno.wizard.view.ChooseSearchActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;


public class OpenChooseSearchCommand {

	private WeakReference<Activity> _context;
	
	public OpenChooseSearchCommand( Activity $context ){
		_context = new WeakReference<Activity>($context);
	}
	
	public void execute(){
		Intent intent = new Intent( ChooseSearchActivity.NAME );
		_context.get().startActivity( intent );
	}
}
