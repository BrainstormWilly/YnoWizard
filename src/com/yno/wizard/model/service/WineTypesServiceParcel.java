package com.yno.wizard.model.service;

import java.util.ArrayList;

import com.yno.wizard.model.SearchTypeParcel;

import android.os.Parcel;
import android.os.Parcelable;

public class WineTypesServiceParcel extends AsyncServiceParcel implements
		Parcelable {
	
	public final static String TAG = "WineTypesServiceParcel";
	public final static String NAME = WineTypesServiceParcel.class.getName();
	
	public static final Parcelable.Creator<WineTypesServiceParcel> CREATOR =
	    	new Parcelable.Creator<WineTypesServiceParcel>() {
	            public WineTypesServiceParcel createFromParcel(Parcel in) {
	                return new WineTypesServiceParcel(in);
	            }
	 
	            public WineTypesServiceParcel[] newArray(int size) {
	            	 return new WineTypesServiceParcel[size];
	            }
	        };
	        
	public ArrayList<SearchTypeParcel> types = new ArrayList<SearchTypeParcel>();
	
	
	public WineTypesServiceParcel(){
		super();
	}
	
	public WineTypesServiceParcel( Parcel $in ){
		this();
		readFromParcel( $in );
	}
	
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		super.writeToParcel(arg0, arg1);
		arg0.writeList(types);
	}
	
	@Override
	protected void readFromParcel(Parcel $in) {
		super.readFromParcel($in);
		$in.readList(types, SearchTypeParcel.class.getClassLoader());
		
	};

}
