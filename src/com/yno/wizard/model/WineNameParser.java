package com.yno.wizard.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.util.Log;

import com.yno.wizard.model.db.YnoDbOpenHelper;
import com.yno.wizard.model.service.VinTankServiceClass;

public class WineNameParser {
	
	public static final String TAG = WineNameParser.class.getSimpleName();

	public static ArrayList<SearchTypeParcel> WINE_TYPES = new ArrayList<SearchTypeParcel>();
	public static int YEAR_MIN = 1900;
	public static String REGEX_750 = ".*\\(?\\s?750.*";
	public static String REGEX_750R = "\\s?\\(?\\s?750";
	public static String REGEX_375 = ".*\\(?\\s?375.*";
	public static String REGEX_375R = "\\s?\\(?\\s?375";
	public static String REGEX_ML = ".*\\s?(([mM][lL])|([mM][iI][lL][lL][iI][lL][iI][tT][eE][rR]))\\s?\\)?.*";
	public static String REGEX_MLR = "\\s?(([mM][lL])|([mM][iI][lL][lL][iI][lL][iI][tT][eE][rR]))\\s?\\)?";
	public static String REGEX_Liter = ".*\\(?\\s?(1\\.5|1500)\\s?([lL][iI][tT][eE][rR])(\\s[mM][aA][gG][nN][uU][mM])?\\s?\\)?.*";
	public static String REGEX_LiterR = "\\(?\\s?(1\\.5|1500)\\s?([lL][iI][tT][eE][rR])(\\s[mM][aA][gG][nN][uU][mM])?\\s?\\)?";
	public static String REGEX_L = ".*\\(?\\s?(1\\.5|1500)\\s?[lL](\\s\\[mM][aA][gG][nN][uU][mM])?\\s?\\)?.*";
	public static String REGEX_LR = "\\(?\\s?(1\\.5|1500)\\s?[lL](\\s[mM][aA][gG][nN][uU][mM])?\\s?\\)?";
	public static String REGEX_375_HALF = ".*\\s[hH][aA][lL][fF](\\s|\\-)[bB][oO][tT][tT][lL][eE]\\s?\\)?.*";
	public static String REGEX_375_HALFR = "\\s[hH][aA][lL][fF](\\s|\\-)[bB][oO][tT][tT][lL][eE]\\s?\\)?";
	public static String REGEX_HALF = ".*\\(?\\s?[hH][aA][lL][fF](\\s|\\-)[bB][oO][tT][tT][lL][eE]\\s?\\)?.*";
	public static String REGEX_HALFR = "\\s?\\(?\\s?[hH][aA][lL][fF](\\s|\\-)[bB][oO][tT][tT][lL][eE]\\s?\\)?";
	public static String REGEX_NV = "[nN][vV]";
	public static String REGEX_NON_VINTAGE = ".*[nN][oO][nN](\\s|\\-)[vV][iI][nN][tT][aA][gG][eE].*";
	public static String REGEX_NON_VINTAGER = "[nN][oO][nN](\\s|\\-)[vV][iI][nN][tT][aA][gG][eE]\\s?";
	//public static [] VOLUMES = new int[]{750, 1.5, 1500, 375};

	private String _origName = "";
	private String _parsedName = "";
	private String _year = "";
	private String _varietal;
	private int _volume = 750;
	private int _maxYear = 1901;
	
	public String getOrigName(){
		return _origName;
	}
	
	public String getParsedName(){
		return _parsedName;
	}
	
	public String getYear(){
		return _year;
	}
	
	public String getVarietal(){
		// tried searching Wine.com types DB
		// returned non varietal names like "vintage"
//		if( _varietal==null ){
//			Iterator<SearchTypeParcel> typeItr = WINE_TYPES.iterator();
//			while( typeItr.hasNext() ){
//				SearchTypeParcel sType = typeItr.next();
//				Iterator<String> aliasItr = sType.aliases.iterator();
//				while( aliasItr.hasNext() ){
//					String type = aliasItr.next();
//					if( _origName.toLowerCase().contains( type.toLowerCase() ) ){
//						_varietal = type;
//						break;
//					}
//				}
//				if( _varietal!=null )
//					break;
//			}
//			if( _varietal==null )
//				_varietal = "";
		
		// tried searching against VinTank varietals Service
		// took too long and results were dicey
//			VinTankServiceClass svc = new VinTankServiceClass();
//			List<String> nameParts = new ArrayList<String>( Arrays.asList(_origName.split(" ")) );
//			List<String> vars;
//			for( int a=0, l=nameParts.size(); a<l; a++ ){
//				if( a+1<l ){
//					vars = svc.getVarietalNamesByQuery( nameParts.get(a) + " " + nameParts.get(a+1), 1, 1);
//					if( vars.size()>0 ){
//						_varietal = vars.get(0);
//						break;
//					}
//				}
//				vars = svc.getVarietalNamesByQuery( nameParts.get(a), 1, 1);
//				if( vars.size()>0 ){
//					_varietal = vars.get(0);
//					break;
//				}
//			}
//			if( _varietal==null )
//				_varietal = "";
//			
//		}
		
		// just return no results for now
		return "Unidentified";
	}
	
	public int getVolume(){
		return _volume;
	}
	
	
	
	
	
	public WineNameParser( String $name ){
		int num;
		List<String> parts;
		String part;
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy");
		Iterator<String> itr;
		
		
		_maxYear = Integer.parseInt( fmt.format( new Date() ) );
		
		_origName = $name;
		
		//search for and reposition year
		if( $name.matches(REGEX_NON_VINTAGE) ){
			$name = $name.replaceAll(REGEX_NON_VINTAGER, "");
			_year = "NV";
		}
		parts = new LinkedList<String>( Arrays.asList($name.split(" ")) );
		itr = parts.iterator();
		
		while( itr.hasNext() ){
			part = itr.next(); 
			try{
				num = Integer.parseInt(part);
				if( num>=YEAR_MIN && num<=_maxYear )
					_year = String.valueOf(num);
				
			}catch(Exception $e){
				if( part.matches(REGEX_NV) )
					_year = "NV";
				
			}
			if( !_year.equals("") ){
				try{
					_parsedName = _year + " ";
					itr.remove();
				}catch(UnsupportedOperationException $e){
					$e.printStackTrace();
				}
				break;
			}
		}
		itr = parts.iterator();
		while( itr.hasNext() )
			_parsedName += itr.next() + " ";
		_parsedName = _parsedName.trim();
		
		
		// search for and remove volume
		if( _parsedName.matches(REGEX_750) ){
			_volume = 750;
			_parsedName = _parsedName.replaceAll(REGEX_750R, "");
			if( _parsedName.matches(REGEX_ML) )
				_parsedName = _parsedName.replaceAll(REGEX_MLR, "");
		}
		if( _parsedName.matches(REGEX_375) ){
			_volume = 375;
			_parsedName = _parsedName.replaceAll(REGEX_375R, "");
			if( _parsedName.matches(REGEX_ML) )
				_parsedName = _parsedName.replaceAll(REGEX_MLR, "");
			if( _parsedName.matches(REGEX_375_HALF) )
				_parsedName = _parsedName.replaceAll(REGEX_375_HALFR, "");
		}
		if( _parsedName.matches(REGEX_HALF) ){
			_volume = 375;
			_parsedName = _parsedName.replaceAll(REGEX_HALFR, "");
		}	
		if( _parsedName.matches(REGEX_L) ){
			_volume = 1500;
			_parsedName = _parsedName.replaceAll(REGEX_LR, "");
		}	
		if( _parsedName.matches(REGEX_Liter) ){
			_volume = 1500;
			_parsedName = _parsedName.replaceAll(REGEX_LiterR, "");
		}	
		
		
	}
	

}
