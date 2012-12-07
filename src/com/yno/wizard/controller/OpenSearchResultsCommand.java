package com.yno.wizard.controller;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yno.wizard.model.SearchWinesParcel;
import com.yno.wizard.view.SearchResultsActivity;

public class OpenSearchResultsCommand {

	public final static String ACTION = "com.yno.wizard.intent.OPEN_SEARCH_RESULTS";
	
	public Bundle payload = new Bundle();
	private WeakReference<Activity> _context;
	
	public OpenSearchResultsCommand( Activity $context ){
		_context = new WeakReference<Activity>($context);
	}
	
	public void execute(){
		Intent intent = new Intent( ACTION );
		Activity ctx = _context.get();
		intent.putExtra(SearchWinesParcel.NAME, payload.getParcelable(SearchWinesParcel.NAME));
		ctx.startActivity(intent);
	}
}
