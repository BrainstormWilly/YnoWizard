package com.yno.wizard.utils;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.yno.wizard.R;
import com.yno.wizard.model.WineParcel;

public class SearchResultsListAdaptor extends BaseAdapter implements ListAdapter {
	
	private final Context _context;
	private final List<WineParcel> _values;
	private DownLoadImageQueue _queue;
	
	public SearchResultsListAdaptor( Context $context, List<WineParcel> $values ){
		_values = $values;
		_context = $context;
		_queue = new DownLoadImageQueue();
	}

	@Override
	public int getCount() {
		if( _values!=null )
			return _values.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return _values.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View row = inflater.inflate(R.layout.search_results_row, parent, false);
		ImageView image = (ImageView) row.findViewById(R.id.searchResultsRowIV);
		TextView label = (TextView) row.findViewById(R.id.searchResultsRowTV);
		WineParcel parcel = _values.get(position);
		
		//new AsyncDownloadImage( image ).execute( parcel.imageSmall );
		_queue.addToQueue(image, parcel.imageSmall);
		label.setText(parcel.nameQualified);
		
		
		return row;
	}

}
