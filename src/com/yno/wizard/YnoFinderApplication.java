package com.yno.wizard;

import android.app.Application;
import android.content.res.Configuration;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;

public class YnoFinderApplication extends Application {
	
	private static YnoFinderApplication _INSTANCE;
	
	public static YnoFinderApplication getInstance(){
		return _INSTANCE;
	}
	
	public Facebook FB;
	public AsyncFacebookRunner FB_RUNNER;
	

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		_INSTANCE = this;
	}
	
	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}
	
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}

}
