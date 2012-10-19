package com.ynotasting.finder.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PriceParcel implements Parcelable {
	
	public final static String TAG = PriceParcel.class.getSimpleName();
	public final static String NAME = PriceParcel.class.getName();
	
	public static final Parcelable.Creator<PriceParcel> CREATOR =
	    	new Parcelable.Creator<PriceParcel>() {
	            public PriceParcel createFromParcel(Parcel in) {
	                return new PriceParcel(in);
	            }
	 
	            public PriceParcel[] newArray(int size) {
	                return new PriceParcel[size];
	            }
	        };

	public Double value = 0.00;
	public String source = "unknown";
	public String seller = "unknown";
	public String url = "";
	public String image = "";
	public int volume = 750;
	public SponsorParcel sponsor;
	
	public PriceParcel(){;}
	
	public PriceParcel( Parcel $in ){
		readFromParcel($in);
	}
	
	public String getValueString(){
		if( volume==375 )
			return value + " (375 ML)";
		if( volume==1500 )
			return value + " (Magnum)";
		return value + " (750 ML)";
	}
	
	public String getCaseValueString(){
		if( volume==375 )
			return value + " (375 ML x 12)";
		if( volume==1500 )
			return value + " (Magnum x 12)";
		return value + " (750 ML x 12)";
	}
	
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeDouble(value);
		dest.writeString(source);
		dest.writeString(seller);
		dest.writeString(url);
		dest.writeString(image);
		dest.writeInt(volume);
		dest.writeParcelable(sponsor, flags);
	}
	
	private void readFromParcel(Parcel in) {
		 
		// We just need to read back each
		// field in the order that it was
		// written to the parcel
		value = in.readDouble();
		source = in.readString();
		seller = in.readString();
		url = in.readString();
		image = in.readString();
		volume = in.readInt();
		sponsor = in.readParcelable(SponsorParcel.class.getClassLoader());
	}

}
