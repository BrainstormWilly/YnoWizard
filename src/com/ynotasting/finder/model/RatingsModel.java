package com.ynotasting.finder.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.ynotasting.finder.utils.RatingComparator;

public class RatingsModel {
	
	final public static String SYSTEM_PARKER = "Parker";
	public static String SYSTEM = "Parker";

	private List<RatingParcel> _list = new ArrayList<RatingParcel>();
	
	public RatingsModel( List<RatingParcel> $list ){
		_list = $list;
	}
	
	public List<RatingParcel> getSortedList(){
		List<RatingParcel> list = new ArrayList<RatingParcel>( _list );
		Collections.sort( list, new RatingComparator() );
		return list;
	}
	
	public Double getAverage(){
		Double sum = 0.00;
		Iterator<RatingParcel> i = _list.iterator();
		while( i.hasNext() )
			sum += getWeightedValue( i.next() );
		
		return (Double) sum/_list.toArray().length;
	}
	
	public RatingParcel getHighest(){
		RatingParcel sel = null;
		RatingParcel cur;
		Iterator<RatingParcel> i = _list.iterator();
		while( i.hasNext() ){
			cur = i.next();
			if( sel==null ) sel = cur;
			if( getWeightedValue(sel) < getWeightedValue(cur) ) sel = cur;
		}
		
		
		return sel;
	}
	
	public RatingParcel getLowest(){
		RatingParcel sel = null;
		RatingParcel cur;
		Iterator<RatingParcel> i = _list.iterator();
		while( i.hasNext() ){
			cur = i.next();
			if( sel==null ) sel = cur;
			if( getWeightedValue(sel) > getWeightedValue(cur) ) sel = cur;
		}
		
		
		return sel;
	}
	
	private Double getWeightedValue( RatingParcel $rating ){
		int rng = $rating.maxValue-$rating.minValue;
		Double val = 0.00;
		
		if( SYSTEM.equals( SYSTEM_PARKER ) ){
			val = $rating.value*100/rng;
		}
		
		return val;
	}
}
