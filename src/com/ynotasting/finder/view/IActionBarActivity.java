package com.ynotasting.finder.view;

import android.content.Intent;

public interface IActionBarActivity {

	//void onActivityResult(int requestCode, int resultCode, Intent data);
	void startActivity( Intent intent );
	void startActivityForResult(Intent intent, int requestCode);
	void dismissProgress( boolean $hasResults );
	void showProgress( String $title, String $msg );
}
