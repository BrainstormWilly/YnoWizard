package com.yno.wizard.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class WineParcel implements Parcelable {
	
	public final static String TAG = WineParcel.class.getSimpleName();
	public final static String NAME = WineParcel.class.getName();
	
//	private static String REGEX_WINERY = ".*[wW]inery.*";
//	private static String REGEX_VINEYARD = ".*[vV]inyards?.*";
//	private static String REGEX_ESTATE = ".*[eE]states?.*";
//	private static String REGEX_W_AND_V = ".*[wW]inery//s((and)|&)//s[vV]inyards?.*";
//	private static String REGEX_WINERYR = ".*[wW]inery.*";
//	private static String REGEX_VINEYARDR = "//s?[vV]inyards?";
//	private static String REGEX_ESTATER = "//s?[eE]states?";
//	private static String REGEX_W_AND_VR = "//s[wW]inery//s((and)|&)//s[vV]inyards?";
	
	
	public static final Parcelable.Creator<WineParcel> CREATOR =
	    	new Parcelable.Creator<WineParcel>() {
	            public WineParcel createFromParcel(Parcel in) {
	                return new WineParcel(in);
	            }
	 
	            public WineParcel[] newArray(int size) {
	                return new WineParcel[size];
	            }

	        };
	
	public String description = "";
	public String id = "";
	public String imageLarge = "";
	public String imageSmall = "";
	public Boolean manual = false;
	public String name = "";
	public String nameQualified = "";
	public String notes = "";
	public List<PriceParcel> prices = new ArrayList<PriceParcel>();
	public String producer = "";
	public List<RatingParcel> ratings = new ArrayList<RatingParcel>();
	public String region = "";
	public SponsorParcel sponsor;
	public String varietal = "";
	public int year = 0;
	
	
	
	public WineParcel(  ){ ; }
	
	public WineParcel( Parcel $in ){
		readFromParcel($in);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(description);
		dest.writeString(id);
		dest.writeString(imageLarge);
		dest.writeString(imageSmall);
		dest.writeByte( (byte)(manual ? 1 : 0) );
		dest.writeString(name);
		dest.writeString(nameQualified);
		dest.writeString(notes);
		dest.writeTypedList(prices);
		dest.writeString(producer);
		dest.writeTypedList(ratings);
		dest.writeString(region);
		dest.writeParcelable(sponsor, flags);
		dest.writeString(varietal);
		dest.writeInt(year);
	}
	
	public void setName( String $name ){
		WineNameParser parser = new WineNameParser($name);
		
		// name from data
		name = $name;
		
		// qualified name ( [YYYY|NV] [Producer] [Region, appellation, name, etc..] [Varietal] )
		nameQualified = parser.getParsedName();
		
		// need to compare data year with parser year
		// year not established in data
		if( year==0 ){
			// year was extracted from parser
			// so set it in data
			if( !parser.getYear().equals("NV") && !parser.getYear().equals("") ){
				year = Integer.parseInt( parser.getYear() );
				//nameQualified = String.valueOf(year) + " " + nameQualified;
			// nothing was extracted from parser
			// assume it as NV
			// leave data year=0
			}else if( parser.getYear().equals("") )
				nameQualified = "NV " + nameQualified;
		// year established in data but not parser
		// add year to qualified name
		}else if( parser.getYear().equals("") ){
			nameQualified = String.valueOf(year) + " " + nameQualified;
		}
		// could still have a potential mismatch between 
		// data year and parser year... oh well
		
		int vol = parser.getVolume();
		if( vol>0 ){
			Iterator<PriceParcel> itr = prices.iterator();
			while( itr.hasNext() )
				itr.next().volume = vol;
		}
		
		if( varietal.equals("") )
			varietal = parser.getVarietal();
	}
	
//	public String getProducerBase(){
//		if( producer.equals("") )
//			return "";
//		if( producer.matches(REGEX_W_AND_V) )
//			return producer.replaceAll(REGEX_W_AND_VR, "");
//		else if( producer.matches(REGEX_WINERY) )
//			return producer.replaceAll(REGEX_WINERYR, "");
//		else if( producer.matches(REGEX_VINEYARD) )
//			return producer.replaceAll(REGEX_VINEYARDR, "");
//		else if( producer.matches(REGEX_ESTATE) )
//			return producer.replaceAll(REGEX_ESTATER, "");
//		return producer;
//	}
	
	private void readFromParcel(Parcel in) {
		 
		// We just need to read back each
		// field in the order that it was
		// written to the parcel
		description = in.readString();
		id = in.readString();
		imageLarge = in.readString();
		imageSmall = in.readString();
		manual = in.readByte()==1;
		name = in.readString();
		nameQualified = in.readString();
		notes = in.readString();
		in.readTypedList(prices, PriceParcel.CREATOR);
		producer = in.readString();
		in.readTypedList(ratings, RatingParcel.CREATOR);
		region = in.readString();
		sponsor = in.readParcelable(SponsorParcel.class.getClassLoader());
		varietal = in.readString();
		year = in.readInt();
	}

}
