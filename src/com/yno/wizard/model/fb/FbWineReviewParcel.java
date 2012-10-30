package com.yno.wizard.model.fb;

import java.text.NumberFormat;

import com.yno.wizard.model.WineParcel;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class FbWineReviewParcel implements Parcelable {
	
	public final static String TAG = FbWineReviewParcel.class.getSimpleName();
	public final static String NAME = FbWineReviewParcel.class.getName();
	
	public static final Parcelable.Creator<FbWineReviewParcel> CREATOR =
	    	new Parcelable.Creator<FbWineReviewParcel>() {
	            public FbWineReviewParcel createFromParcel(Parcel in) {
	                return new FbWineReviewParcel(in);
	            }
	 
	            public FbWineReviewParcel[] newArray(int size) {
	                return new FbWineReviewParcel[size];
	            }
	        };
	        
	public WineParcel wine = new WineParcel();
	public Double value = 0.00;
	public String description = "";
	public String id = "";
	
	private int _max = 5;
	
	
	
	public FbWineReviewParcel(){;}
	
	public FbWineReviewParcel( Parcel $in ){
		readFromParcel($in);
	}
	
	public int getWeightedValue(){
		
		return (int) (100*value/_max) ;
	}
	
	public String getFormattedValue(){
		NumberFormat fmt = NumberFormat.getInstance();
		if( _max<10 ){
			fmt.setMinimumFractionDigits(1);
			fmt.setMaximumFractionDigits(1);
		}else{
			fmt.setMaximumFractionDigits(0);
		}
		
		return fmt.format(value);
	}
	public String getRatingString(){
		String rating;
		if( value==0.00 )
			rating = "n/a";
		else{
			rating = getFormattedValue() + " (" + String.valueOf(_max) + "=best)";
		}
		
		return rating;
	}
	
	public int getMax(){
		return _max;
	}
	public void setMax( int $val ){
		
		value = new Double($val)*value/_max ;
		_max = $val;
	}
	
	

	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel $dest, int $flags) {
		$dest.writeParcelable(wine, $flags);
		$dest.writeDouble(value);
		$dest.writeInt(_max);
		$dest.writeString(description);
		$dest.writeString(id);
	}
	
	public String toReviewString(){
		StringBuilder sb = new StringBuilder(wine.name + "\n");
		sb.append("Rating = ");
		sb.append(getRatingString() + "\n");
		sb.append("Description:\n");
		sb.append(description);
		return sb.toString();
	}
	
	private void readFromParcel(Parcel $in) {
		wine = $in.readParcelable( WineParcel.class.getClassLoader() );
		value = $in.readDouble();
		_max = $in.readInt();
		description = $in.readString();
		id = $in.readString();
	}

}
