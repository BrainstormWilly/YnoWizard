package com.ynotasting.finder.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ynotasting.finder.model.SearchWinesParcel;
import com.ynotasting.finder.view.SearchResultsActivity;

public class OpenSearchResultsCommand {

	public Bundle payload = new Bundle();
	private Context _context;
	
	public OpenSearchResultsCommand( Context $context ){
		_context = $context;
	}
	
	public void execute(){
		Intent intent = new Intent( SearchResultsActivity.NAME );
		intent.putExtra(SearchWinesParcel.NAME, payload.getParcelable(SearchWinesParcel.NAME));
		_context.startActivity(intent);
	}
}
