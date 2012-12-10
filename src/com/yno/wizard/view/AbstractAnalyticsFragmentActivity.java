package com.yno.wizard.view;

import android.content.pm.ApplicationInfo;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.analytics.tracking.android.EasyTracker;

public class AbstractAnalyticsFragmentActivity extends SherlockFragmentActivity {

	public boolean isDebuggable(){
		return ( 0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) );
	}
	
	
	@Override
	protected void onStart() {
		super.onStart();
		
		if( !isDebuggable() )
			EasyTracker.getInstance().activityStart(this);
		
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		if( !isDebuggable() )
			EasyTracker.getInstance().activityStop(this);
	}
}
