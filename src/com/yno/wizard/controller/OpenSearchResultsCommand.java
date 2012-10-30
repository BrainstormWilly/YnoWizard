package com.yno.wizard.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yno.wizard.model.SearchWinesParcel;
import com.yno.wizard.view.SearchResultsActivity;

public class OpenSearchResultsCommand {

	public final static String ACTION = "com.yno.wizard.intent.OPEN_SEARCH_RESULTS";
	
	public Bundle payload = new Bundle();
	private Context _context;
	
	public OpenSearchResultsCommand( Context $context ){
		_context = $context;
	}
	
	public void execute(){
		Intent intent = new Intent( ACTION );
		intent.putExtra(SearchWinesParcel.NAME, payload.getParcelable(SearchWinesParcel.NAME));
		_context.startActivity(intent);
	}
}
