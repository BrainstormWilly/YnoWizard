package com.yno.wizard.view;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Messenger;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.yno.wizard.R;
import com.yno.wizard.controller.DoWineTypesSearchCommand;
import com.yno.wizard.controller.OpenChooseSearchCommand;
import com.yno.wizard.model.SearchTypeParcel;
import com.yno.wizard.model.db.YnoDbOpenHelper;

public class SplashActivity extends SherlockActivity {
    /** Called when the activity is first created. */
	public static String TAG = SplashActivity.class.getSimpleName();
	private static int _DELAY = 3000;

	private YnoDbOpenHelper _dbHelper;
	private AlertDialog _dbProg;
	private Handler _delayHdl = new Handler();
	
	private Handler _wineTypesSearchHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			ArrayList<SearchTypeParcel> types = (ArrayList<SearchTypeParcel>) msg.obj;
			_dbHelper.insertSearchTypes( types );
			_dbProg.dismiss();
			SplashActivity.this.endSplash();
		}
	};
	
	private Runnable _delayRnb = new Runnable() {
		
		@Override
		public void run() {
			Cursor cursor = _dbHelper.getAllFromSearchTypes();
			if( cursor.moveToFirst() ){
				SplashActivity.this.endSplash();
			}else{
				SplashActivity.this.loadData();
			}
		}
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.splash);
        
        getSupportActionBar().hide();
        
        _dbHelper = new YnoDbOpenHelper( this );
       
        _delayHdl.postDelayed( _delayRnb, _DELAY);
       
    }
    
    public void loadData(){
    	//_dbProg = ProgressDialog.show(this, "Hang on...", "The Wizard is getting its magic on!", true);
    	AlertDialog.Builder bldr = new AlertDialog.Builder(this);
		
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dialog_progress, (ViewGroup) findViewById(R.id.dialogProgressRL));
		
		TextView title = (TextView) layout.findViewById(R.id.dialogProgressTitleTV);
		
		title.setText("Please wait while the Wizard gets its magic on!");
		
		
		bldr.setView( layout );
		_dbProg = bldr.create();
		_dbProg.show();
		
    	DoWineTypesSearchCommand cmd = new DoWineTypesSearchCommand( this );
		cmd.messenger = new Messenger(_wineTypesSearchHandler);
		cmd.execute();
    }
    
    public void endSplash(){
    	OpenChooseSearchCommand cmd = new OpenChooseSearchCommand(this);
	    cmd.execute();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	//_delayHdl.postDelayed( _delayRnb, _DELAY);
    } 
    
    
    
}