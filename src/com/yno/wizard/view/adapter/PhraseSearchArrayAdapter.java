 package com.yno.wizard.view.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.yno.wizard.model.service.SearchData;
import com.yno.wizard.model.service.PhraseSearchFieldService;

public class PhraseSearchArrayAdapter extends ArrayAdapter<String> implements Filterable {
	
	public static String TAG = PhraseSearchArrayAdapter.class.getSimpleName();
	
	private Filter _filter;
	private Context _context;
	private ArrayList<String> _newData = new ArrayList<String>(SearchData.AUTOCOMPLETE_TOTAL);
	//private TextSearchFieldServiceHelper _svcHelper;

	
	public PhraseSearchArrayAdapter( Context $context, int $textViewId ){
		super( $context, $textViewId );
		setNotifyOnChange(false);
		
		_context = $context;
		//_svcHelper = new TextSearchFieldServiceHelper();
		
		_filter = new Filter(){
			
			PhraseSearchFieldService _helper = new PhraseSearchFieldService();
			
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults filtResults = new FilterResults();
				
				if( constraint!=null && constraint.length()>0 ){
					//Log.d(TAG, constraint.toString());
					
					ArrayList<String> values = new ArrayList<String>();
					
					try{
						values =  _helper.getValues(constraint);
					}catch( Exception $e ){
						//Log.d(TAG, "service failed");
					}
					
					filtResults.values = values;
					filtResults.count = values.size();
					
					
				}
				return filtResults;
			}
			
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				//Log.d(TAG, "publishResults = " results.count);
				if( results!=null && results.count>0 ){
					_newData = new ArrayList<String>( (ArrayList<String>) results.values );
					notifyDataSetChanged();
				}else
					notifyDataSetInvalidated();
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
	

}
