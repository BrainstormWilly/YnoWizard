package com.yno.wizard.view;

import com.actionbarsherlock.app.SherlockActivity;

import android.content.Intent;

public interface IActionBarActivity {

	//void onActivityResult(int requestCode, int resultCode, Intent data);
	void startActivity( Intent intent );
	void startActivityForResult(Intent intent, int requestCode);
	void dismissProgress( boolean $hasResults );
	void showProgress( String $msg );
	String getString( int $int );
	com.actionbarsherlock.app.ActionBar getSupportActionBar();
	android.app.ActionBar getActionBar();
}
