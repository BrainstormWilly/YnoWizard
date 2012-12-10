package com.yno.wizard.controller;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Messenger;

import com.google.zxing.client.android.Intents.Scan;
import com.yno.wizard.view.ScanSearchActivity;
import com.yno.wizard.view.assist.ActionBarAssist;

public class DoBarcodeScanCommand {
	
	public final static String TAG = "DoBarcodeScanCommand";
	//public final static String ACTION = "com.yno.wizard.intent.DO_BARCODE_SCAN";
	public final static String ACTION = "com.google.zxing.client.android.SCAN";
	
	public Bundle payload = new Bundle();
	public Messenger messenger;
	private WeakReference<Activity> _context;
	
	public DoBarcodeScanCommand( Activity $context ){
		_context = new WeakReference<Activity>($context);
	}
	
	public void execute(){
		try{
			Activity ctx = _context.get();
			Intent zxingInt = new Intent(ctx, ScanSearchActivity.class);
			zxingInt.putExtra(Scan.MODE, Scan.PRODUCT_MODE);
			ctx.startActivityForResult(zxingInt, ActionBarAssist.BARCODE_RESULT);
		}catch( Exception $e ){
			$e.printStackTrace();
		}
	}

}
