package com.yno.wizard.model.service;

import java.util.ArrayList;

import com.yno.wizard.model.WineParcel;

import android.os.Parcel;
import android.os.Parcelable;

public class WinesServiceParcel extends AsyncServiceParcel implements
		Parcelable {
	
	public final static String TAG = "WinesServiceParcel";
	public final static String NAME = WinesServiceParcel.class.getName();
	
	public static final Parcelable.Creator<WinesServiceParcel> CREATOR =
	    	new Parcelable.Creator<WinesServiceParcel>() {
	            public WinesServiceParcel createFromParcel(Parcel in) {
	                return new WinesServiceParcel(in);
	            }
	 
	            public WinesServiceParcel[] newArray(int size) {
	            	 return new WinesServiceParcel[size];
	            }
	        };
	        
	
	public ArrayList<WineParcel> unqualified = new ArrayList<WineParcel>();
	public ArrayList<WineParcel> qualified = new ArrayList<WineParcel>();
	
	
	public WinesServiceParcel(){
		super();
	}
	
	public WinesServiceParcel( Parcel $in ){
		this();
		readFromParcel( $in );
	}
	
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		super.writeToParcel(arg0, arg1);
		arg0.writeList(qualified);
		arg0.writeList(unqualified);
	}
	
	@Override
	protected void readFromParcel(Parcel $in) {
		super.readFromParcel($in);
		$in.readList(qualified, WineParcel.class.getClassLoader());
		$in.readList(unqualified, WineParcel.class.getClassLoader());
	};

}
