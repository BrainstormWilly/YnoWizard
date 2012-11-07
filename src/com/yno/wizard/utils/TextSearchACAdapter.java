package com.yno.wizard.utils;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.yno.wizard.model.VintagesModel;
import com.yno.wizard.model.service.SearchData;
import com.yno.wizard.model.service.VinTankServiceClass;

public class TextSearchACAdapter extends ArrayAdapter<String> implements Filterable {
	
	public static String TAG = TextSearchACAdapter.class.getSimpleName();
	
	private Filter _filter;
	private Context _context;
	private VintagesModel _vintages = new VintagesModel();
	private VinTankServiceClass _vtSvc = new VinTankServiceClass();
	private String _base = "";
	private String _full = "";
	private boolean _partMode = false;
	private ArrayList<String> _newData = new ArrayList<String>(SearchData.AUTOCOMPLETE_TOTAL);
	//private ArrayList<String> _allData = new ArrayList<String>(SearchData.AUTOCOMPLETE_TOTAL);
	private ArrayList<String> _oldData = new ArrayList<String>(SearchData.AUTOCOMPLETE_TOTAL);
	
	public TextSearchACAdapter( Context $context, int $textViewId ){
		super( $context, $textViewId );
		setNotifyOnChange(false);
		
		_context = $context;
		
		_filter = new Filter(){
			private ArrayList<String> _allData = new ArrayList<String>();
			//private String _query = "";
			
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults filtResults = new FilterResults();
				
				if( constraint!=null && constraint.length()>0 ){
					
					int a, l;
					String result;
					String[] parts = constraint.toString().split(" ");
					String query = parts[ parts.length-1 ].toLowerCase();
					_base = "";
					_full = constraint.toString();
					for( a=0, l=parts.length-1; a<l; a++ ){
						_base += parts[a] + " ";
					}
					
					_allData.clear();
					_partMode = false;
					
					_allData.addAll( _vintages.getVintageByQuery(query) );
					
					if( _allData.size()==0 ){
						ArrayList<String> vars = _vtSvc.getVarietalNamesByQuery(query, 1, SearchData.AUTOCOMPLETE_TOTAL);
						for( String var : vars ) {
							//Log.d(TAG, var.var_name + ", " + _full + ": " + _full.compareToIgnoreCase(var.var_name));
							if( var.toLowerCase().startsWith(_full.toLowerCase()) ) 
								_allData.add( var );
						}
					}
					
					if( _allData.size()==0 ){
						ArrayList<String> prods = _vtSvc.getProducerNamesByQuery(query, 1, SearchData.AUTOCOMPLETE_TOTAL);
						for( String prod : prods ) {
							if( prod.toLowerCase().startsWith(_full.toLowerCase()) ) 
								_allData.add( prod );
						}
					}
					
					if( _allData.size()==0){
						ArrayList<String> regs = _vtSvc.getRegionNamesByQuery(query, 1, SearchData.AUTOCOMPLETE_TOTAL);
						for( String reg : regs ) {
							if( reg.toLowerCase().startsWith(_full.toLowerCase()) ) 
								_allData.add( reg );
						}
					}
						
					
					if( _allData.size()==0 ){
						_partMode = true;
						ArrayList<String> wines = _vtSvc.getWineNamesByQuery(query, 1, SearchData.AUTOCOMPLETE_TOTAL);
						for( String wine : wines ) {
							result = evalResult(wine, query);
							if( !result.equals("") && !repeat(_allData, result) ) 
								_allData.add( result );
						}
					}
					
					Collections.sort(_allData);						
					
					if( _allData.size()>0 ){
						_allData.trimToSize();
						_oldData = new ArrayList<String>(_allData);
						filtResults.values = _allData;
						filtResults.count = _allData.size();
					}else if(_partMode){
						filtResults.values = _oldData;
						filtResults.count = _oldData.size();
					}else{
						filtResults.values = _allData;
						filtResults.count = _allData.size();
					}
					
				}
				return filtResults;
			}
			
			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				
				if( results!=null && results.count>0 ){
					
					try{
						_newData = new ArrayList<String>( results.count );
						if( _partMode ){
							for( int a=0, l=_allData.size(); a<l; a++ ){
								_newData.add( _base + ((ArrayList<String>) results.values).get(a) );
							}
						}else{
							_newData = new ArrayList<String>( (ArrayList<String>) results.values );
						}
					
						
							
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
	public View getDropDownView(int $position, View $convertView, ViewGroup $parent) {
		return getCustomView($position, $convertView, $parent);

	}
	
	@Override
	public View getView(int $position, View $convertView, ViewGroup $parent) {
		View view = getCustomView($position, $convertView, $parent);
//		view.setBackgroundColor(_context.getResources().getColor(android.R.color.transparent));
//		((TextView) view).setTextColor( _context.getResources().getColorStateList(R.color.yw_edittext_textcolor) );
//		((TextView) view).setTextSize(16);
//		view.setPadding(0, 0, 0, 0);
		
		return view;
	}

	@Override
	public int getCount() {
		return _newData.size();
	}
	
	@Override
	public String getItem(int position) {
		return _newData.get(position);
	}
	
	@Override
	public Filter getFilter() {
		return _filter;
	}
	
	public View getCustomView(int $position, View $convertView, ViewGroup $parent){
		LayoutInflater inflater = LayoutInflater.from(_context);
		if( $convertView==null )
			$convertView = inflater.inflate(com.yno.wizard.R.layout.spinner_item, null);
		
		//TextView tv = (TextView) $convertView.findViewById(com.yno.wizard.R.id.spinnerItemTV);
		//tv.setText(_items.get($position));
		((TextView) $convertView).setText(_newData.get($position));
		
		return $convertView;
			
		 
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
