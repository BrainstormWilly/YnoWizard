package com.ynotasting.finder.view;

import com.ynotasting.finder.R;
import com.ynotasting.finder.model.PriceParcel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;

public class WebViewActivity extends Activity {

	private WebView _view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		
		Intent intent = getIntent();
		PriceParcel parcel = intent.getParcelableExtra(PriceParcel.NAME);
		_view = (WebView) findViewById(R.id.webView);
		
		_view.getSettings().setJavaScriptEnabled(true);
		_view.loadUrl( parcel.url );
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			if(_view.canGoBack())
				_view.goBack();
			else
				finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
