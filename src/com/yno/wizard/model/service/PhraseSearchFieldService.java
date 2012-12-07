package com.yno.wizard.model.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.util.Log;

import com.yno.wizard.model.VintagesModel;

public class PhraseSearchFieldService {
	
	public static final String TAG = "TextSearchFieldServiceHelper";

	private VinTankServices _svc;
	private ArrayList<String> _values = new ArrayList<String>(SearchData.AUTOCOMPLETE_TOTAL);
	private String _phrase = "";
	
	public PhraseSearchFieldService(){
		_svc = new VinTankServices();
	}
	
	public ArrayList<String> getValues( CharSequence $phrase ){
		
		_phrase = $phrase.toString().toLowerCase();
		_values.clear();
		
		//Log.d(TAG, "running wine names");
		PhraseServiceParcel parcel = _svc.getWineNamesByQuery( _phrase, 1, SearchData.AUTOCOMPLETE_TOTAL );
		
		if( !valuesFilled(parcel) ){
			//Log.d(TAG, "running producer names");
			parcel = _svc.getProducerNamesByQuery( parcel.query, 1, SearchData.AUTOCOMPLETE_TOTAL );
			if( !valuesFilled(parcel) ){
				//Log.d(TAG, "running region names");
				parcel = _svc.getRegionNamesByQuery( parcel.query, 1, SearchData.AUTOCOMPLETE_TOTAL );
				if( !valuesFilled(parcel) ){
					//Log.d(TAG, "running varietal names");
					parcel = _svc.getVarietalNamesByQuery( parcel.query, 1, SearchData.AUTOCOMPLETE_TOTAL );
					valuesFilled(parcel);
				}
			}
		}
		
		_values.trimToSize();
		Collections.sort( _values );
		
		
		return _values;

	}
	
	public Boolean valuesFilled( AsyncServiceParcel $parcel ) {
		PhraseServiceParcel parcel = (PhraseServiceParcel) $parcel;
		
		for( String phrase : parcel.values ){
			if( phrase.toLowerCase().startsWith(_phrase) && !_values.contains(phrase) ){
				_values.add(0, phrase);
			}
		}
		return _values.size()>=SearchData.AUTOCOMPLETE_TOTAL;
		
	}

}
