package com.yno.wizard.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RatingParcel implements Parcelable {

	public final static String TAG = RatingParcel.class.getSimpleName();
	public final static String NAME = RatingParcel.class.getName();
	
	public static final RatingParcel.Creator<RatingParcel> CREATOR =
	    	new RatingParcel.Creator<RatingParcel>() {
	            public RatingParcel createFromParcel(Parcel in) {
	                return new RatingParcel(in);
	            }
	 
	            public RatingParcel[] newArray(int size) {
	                return new RatingParcel[size];
	            }
	        };
	        
    public int maxValue = 100;
	public int minValue = 0;
	public Double value = 0.00;
	public String source = "";
	public String seller = "";
	public String image = "";
	
	public RatingParcel(){;}
	
	public RatingParcel( Parcel $in ){
		readFromParcel($in);
	}
	
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(maxValue);
		dest.writeInt(minValue);
		dest.writeDouble(value);
		dest.writeString(source);
		dest.writeString(seller);
		dest.writeString(image);
	}
	
	private void readFromParcel(Parcel in) {
		 
		// We just need to read back each
		// field in the order that it was
		// written to the parcel
		maxValue = in.readInt();
		minValue = in.readInt();
		value = in.readDouble();
		source = in.readString();
		seller = in.readString();
		image = in.readString();
	}
}
