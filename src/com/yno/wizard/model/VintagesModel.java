package com.yno.wizard.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.yno.wizard.model.service.SearchData;

public class VintagesModel {
	
	public static final int FIRST_YEAR = 1900;
	public static final String NV = "NV (Non Vintage)";

	private ArrayList<String> _years = new ArrayList<String>();
	
	public VintagesModel(){
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy");
		int thisYear = Integer.parseInt( fmt.format( new Date() ) );
		_years.add(NV);
		do{
			_years.add( String.valueOf(thisYear) );
		}while( --thisYear>FIRST_YEAR );
	}
	
	public ArrayList<String> getVintageByQuery( String $query ){
		ArrayList<String> years = new ArrayList<String>();
		
		for( int a=0, l=_years.size(); a<l; a++ ){
			String year = _years.get(a);
			if( year.contains($query) ) years.add(year);
			if( years.size()==SearchData.AUTOCOMPLETE_TOTAL ) break;
		}
		
		return years;
	}
	
	public ArrayList<String> getAllVintages(){
		ArrayList<String> years = new ArrayList<String>();
		
		for( int a=0, l=_years.size(); a<l; a++ ){
			String year = _years.get(a);
			years.add(year);
		}
		
		return years;
	}
}
