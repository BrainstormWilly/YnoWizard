package com.yno.wizard.view;

import android.app.AlertDialog;
import android.content.pm.ApplicationInfo;
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
import com.yno.wizard.model.db.YnoDbOpenHelper;
import com.yno.wizard.model.service.AsyncServiceParcel;
import com.yno.wizard.model.service.WineTypesServiceParcel;
import com.yno.wizard.view.assist.ActivityAlertAssist;

public class SplashActivity extends SherlockActivity implements IAlertActivity {
    /** Called when the activity is first created. */
	public final static String TAG = SplashActivity.class.getSimpleName();
	private final static int _DELAY = 3000;
	
	private ActivityAlertAssist _alertAssist;
	private YnoDbOpenHelper _dbHelper;
	private Handler _delayHdl;
	
	
	private Handler _wineTypesSearchHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			WineTypesServiceParcel parcel = (WineTypesServiceParcel) msg.obj;
			_dbHelper.insertSearchTypes( parcel.types );
			getAlertAssist().alertDismiss();
			new OpenChooseSearchCommand( SplashActivity.this ).execute();
		}
	};
	
	private Runnable _delayRnb = new Runnable() {
		
		@Override
		public void run() {
			Cursor cursor = _dbHelper.getAllFromSearchTypes();
			if( cursor.moveToFirst() ){
				new OpenChooseSearchCommand( SplashActivity.this ).execute();
			}else{
				getAlertAssist().alertShowProgress(R.string.please_wait_while_the_wizard_gets_its_magic_on);
				
		    	DoWineTypesSearchCommand cmd = new DoWineTypesSearchCommand( SplashActivity.this );
				cmd.messenger = new Messenger(_wineTypesSearchHandler);
				cmd.execute();
			}
		}
	};
	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.splash);
        
        getSupportActionBar().hide();
        
        _alertAssist = new ActivityAlertAssist(this);
        _dbHelper = new YnoDbOpenHelper( this );
    	_delayHdl = new Handler();
    	_delayHdl.postDelayed( _delayRnb, _DELAY);
       
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	if( _dbHelper!=null )
    		_dbHelper.close();
    	
    }
    
    /*
     * IAlertActivity methods
     */
    public ActivityAlertAssist getAlertAssist(){
    	return _alertAssist;
    }
    
    
    /*
     * IServiceActivity methods
     */
//    public void resume( AsyncServiceParcel $parcel ){
//    	WineTypesServiceParcel parcel = (WineTypesServiceParcel) $parcel;
//    	_dbHelper.insertSearchTypes( parcel.types );
//    	getAlertAssist().alertDismissProgress();
//		new OpenChooseSearchCommand(this).execute();
//    }

    
    
    
    
}