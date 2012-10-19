package com.ynotasting.finder.utils;

import java.util.ArrayList;
import java.util.Collections;

import com.ynotasting.finder.model.service.SearchData;
import com.ynotasting.finder.model.service.VinTankServiceClass;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

public class WineTypesACAdapter extends ArrayAdapter<String> implements Filterable {
	
	public static final String TAG = WineTypesACAdapter.class.getSimpleName();

	private ArrayList<String> _allData = new ArrayList<String>();
	private Filter _filter;
	private VinTankServiceClass _vtSvc = new VinTankServiceClass();
	
	public WineTypesACAdapter( Context $context, int $textViewId ){
		super( $context, $textViewId );
		setNotifyOnChange(false);
		
		_filter = new Filter(){
			private ArrayList<String> _allData = new ArrayList<String>();
			//private String _query = "";
			
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults filtResults = new FilterResults();
				
				if( constraint!=null && constraint.length()>0 ){
					
					int a, l;
					String result;
//					String[] parts = constraint.toString().split(" ");
//					String query = parts[ parts.length-1 ].toLowerCase();
					String query = constraint.toString();
					
					_allData.clear();
					
					ArrayList<String> vars = _vtSvc.getVarietalNamesByQuery(query, 1, SearchData.AUTOCOMPLETE_TOTAL);
					_allData.addAll(vars);
					
					filtResults.values = _allData;
					filtResults.count = _allData.size();
//						for( String var : vars ) {
//							if( var.toLowerCase().startsWith(_full.toLowerCase()) ) 
//								_allData.add( var );
//						}
					
					//Collections.sort(_allData);						
					
//					if( _allData.size()>0 ){
//						_allData.trimToSize();
//						_oldData = new ArrayList<String>(_allData);
//						filtResults.values = _allData;
//						filtResults.count = _allData.size();
//					}else if(_partMode){
//						filtResults.values = _oldData;
//						filtResults.count = _oldData.size();
//					}else{
//						filtResults.values = _allData;
//						filtResults.count = _allData.size();
//					}
					
				}
				return filtResults;
			}
			
			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				
				if( results!=null && results.count>0 ){
					
					try{
						//_newData = new ArrayList<String>( results.count );
//						if( _partMode ){
//							for( int a=0, l=_allData.size(); a<l; a++ ){
//								_newData.add( _base + ((ArrayList<String>) results.values).get(a) );
//							}
//						}else{
//							_newData = new ArrayList<String>( (ArrayList<String>) results.values );
//						}
					
						Log.d(TAG, "data length = " + _allData.size());
							
						notifyDataSetChanged();
					}catch( Exception $e ){
						$e.printStackTrace();
					}
				}else{
					notifyDataSetInvalidated();
				}
			}

		};
	}

	@Override
	public int getCount() {
		return _allData.size();
	}
	
	@Override
	public String getItem(int position) {
		return _allData.get(position);
	}
	
	@Override
	public Filter getFilter() {
		return _filter;
	}
	
	private String evalResult( String $result, String $query ){
		String[] parts = $result.split(" ");
		for( int a=0, l=parts.length; a<l; a++ ){
			//Log.d(TAG, parts[a] + ", " + $query + ": " + parts[a].toLowerCase().equals($query));
			if( parts[a].toLowerCase().indexOf($query)==0)
				return parts[a];
		}
		return "";
	}
	
	private boolean repeat( ArrayList<String> $array, String $result ){
		if( $array.contains($result) ) return true;
		if( $array.contains( $result.toLowerCase() ) ) return true;
		return false;
	}
}
