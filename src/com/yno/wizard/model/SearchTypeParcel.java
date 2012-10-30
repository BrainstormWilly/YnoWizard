package com.yno.wizard.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class SearchTypeParcel implements Parcelable {

	public final static String TAG = SearchTypeParcel.class.getSimpleName();
	public final static String NAME = SearchTypeParcel.class.getName();
	
	public static final Parcelable.Creator<SearchTypeParcel> CREATOR =
	    	new Parcelable.Creator<SearchTypeParcel>() {
	            public SearchTypeParcel createFromParcel(Parcel in) {
	                return new SearchTypeParcel(in);
	            }
	 
	            public SearchTypeParcel[] newArray(int size) {
	            	 return new SearchTypeParcel[size];
	            }
	        };
	        
	        
	public String name = "";
	public ArrayList<String> aliases = new ArrayList<String>();
	public int id = 0;
	
    
	public SearchTypeParcel(){;}
	
	public SearchTypeParcel( Parcel $in ){
		this();
		readFromParcel($in);
	}
	        
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel $dest, int $flags) {
		$dest.writeString(name);
		$dest.writeInt(id);
		$dest.writeList(aliases);
	}
	
	public void setName( String $name ){
		name = $name;
		aliases = new ArrayList<String>();
		
		if( name.toLowerCase().contains("sparkling") ){
			aliases.add("Sparkling");
			aliases.add("Champagne");
		}else if( name.toLowerCase().contains("bordeaux") ){
			aliases.add("Bordeaux");
			aliases.add("Meritage");
		}else if( name.toLowerCase().contains("red wine") ){
			aliases.add("Red");
		}else if( name.toLowerCase().contains("white wine") ){
			aliases.add("White");
		}else if( name.toLowerCase().contains("dessert") ){
			aliases.add("Dessert");
			aliases.add("Sherry");
			aliases.add("Late Harvest");
			aliases.add("Port");
		}else if( name.toLowerCase().contains("grigio") ){
			aliases.add("Pinot Gris");
			aliases.add("Pinot Grigio");
		}else if( name.toLowerCase().contains("ros�") ){
			aliases.add("Ros�");
			aliases.add("Blush");
			aliases.add("Pink");
		}else if( name.toLowerCase().contains("syrah") ){
			aliases.add("Syrah");
			aliases.add("Shiraz");
		}else if( name.toLowerCase().contains("gamay") ){
			aliases.add("Gamay Beaujolais");
			aliases.add("Gamay");
		}else if( name.toLowerCase().contains("rh�ne") ){
			aliases.add("C�tes du Rh�ne");
			aliases.add("GSM");
		}else{
			aliases.add($name);
		}
	}
	
	private void readFromParcel(Parcel $in) {
		name = $in.readString();
		id = $in.readInt();
		$in.readList(aliases, null);
	}

}
