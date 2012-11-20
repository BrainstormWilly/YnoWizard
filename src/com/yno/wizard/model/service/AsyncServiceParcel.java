package com.yno.wizard.model.service;

import android.os.Parcel;
import android.os.Parcelable;

public class AsyncServiceParcel implements Parcelable {
	
	public final static String TAG = "AsyncServiceParcel";
	public final static String NAME = AsyncServiceParcel.class.getName();
	
	public static final Parcelable.Creator<AsyncServiceParcel> CREATOR =
	    	new Parcelable.Creator<AsyncServiceParcel>() {
	            public AsyncServiceParcel createFromParcel(Parcel in) {
	                return new AsyncServiceParcel(in);
	            }
	 
	            public AsyncServiceParcel[] newArray(int size) {
	            	 return new AsyncServiceParcel[size];
	            }
	        };
	        
	public int svc_id = 0;
	public String svc = "";
	public String url = "";
	public String query = "";
	public String result = "";
	public int page = 1;
	public String app_id = "";
	
	        
    public AsyncServiceParcel(){;}
	
	public AsyncServiceParcel( Parcel $in ){
		this();
		readFromParcel($in);
	}

	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeString(svc);
		arg0.writeString(url);
		arg0.writeString(query);
		arg0.writeString(result);
		arg0.writeInt(svc_id);
		arg0.writeInt(page);
		arg0.writeString(app_id);
	}
	
	protected void readFromParcel(Parcel $in) {
		svc = $in.readString();
		url = $in.readString();
		query = $in.readString();
		result = $in.readString();
		svc_id = $in.readInt();
		page = $in.readInt();
		app_id = $in.readString();
	}

}
