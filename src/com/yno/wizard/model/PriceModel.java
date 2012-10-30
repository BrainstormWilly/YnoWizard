package com.yno.wizard.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.yno.wizard.utils.PriceComparator;

public class PriceModel {

	private List<PriceParcel> _list = new ArrayList<PriceParcel>();
	
	public PriceModel( List<PriceParcel> $prices ){
		_list = $prices;
	}
	
	public List<PriceParcel> getSortedList(){
		List<PriceParcel> list = new ArrayList<PriceParcel>( _list );
		Collections.sort( list, new PriceComparator() );
		return list;
	}
	
	public int getLength(){
		return _list.size();
	}
	
	public Double getAverage(){
		Double sum = 0.00;
		Iterator<PriceParcel> i = _list.iterator();
		while( i.hasNext() )
			sum += i.next().value;
		
		return (Double) sum/_list.toArray().length;
	}
	
	public PriceParcel getHighest(){
		PriceParcel sel = new PriceParcel();
		PriceParcel cur;
		Iterator<PriceParcel> i = _list.iterator();
		while( i.hasNext() ){
			cur = i.next();
			if( sel.value==0 ) sel = cur;
			if( sel.value < cur.value ) sel = cur;
		}
		
		return sel;
	}
	
	public PriceParcel getLowest(){
		PriceParcel sel = new PriceParcel();
		PriceParcel cur;
		Iterator<PriceParcel> i = _list.iterator();
		while( i.hasNext() ){
			cur = i.next();
			if( sel.value==0 ) sel = cur;
			if( sel.value > cur.value ) sel = cur;
		}
		
		return sel;
	}
}
