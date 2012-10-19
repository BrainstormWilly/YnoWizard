package com.ynotasting.finder.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Messenger;

import com.ynotasting.finder.model.PriceParcel;

public class ShowPriceVendorCommand {

	public final static String ACTION = "com.ynotasting.finder.intent.SHOW_PRICE_VENDOR";
	
	public Bundle payload = new Bundle();
	public Messenger messenger;
	
	private Context _context;
	
	public ShowPriceVendorCommand( Context $context ){
		_context = $context;
	}
	
	public void execute(){
		try{
			Intent intent = new Intent( ACTION );
			intent.putExtra(PriceParcel.NAME, payload.getParcelable(PriceParcel.NAME));
			_context.startActivity(intent);
		}catch( Exception $e ){
			$e.printStackTrace();
		}
	}
	
}
