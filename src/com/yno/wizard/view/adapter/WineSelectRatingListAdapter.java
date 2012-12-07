package com.yno.wizard.view.adapter;

import java.text.NumberFormat;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.yno.wizard.R;
import com.yno.wizard.model.RatingParcel;

public class WineSelectRatingListAdapter extends BaseAdapter implements ListAdapter {
	
	private final Context _context;
	private final List<RatingParcel> _values;
	
	public WineSelectRatingListAdapter( Context $context, List<RatingParcel> $values ){
		_context = $context;
		_values = $values;
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
		
		View row = inflater.inflate(R.layout.wine_select_ratings_row, parent, false);
		ImageView image = (ImageView) row.findViewById(R.id.wineSelectRatingIV);
		TextView rater = (TextView) row.findViewById(R.id.wineSelectRatingRaterTV);
		TextView rating = (TextView) row.findViewById(R.id.wineSelectRatingValueTV);
		RatingParcel parcel = _values.get(position);
		NumberFormat fmt = NumberFormat.getIntegerInstance();
		
		if( parcel.image.indexOf("drawable")==0 ){
			int rsrc = _context.getResources().getIdentifier( parcel.image, null, _context.getPackageName() );
			Drawable img = _context.getResources().getDrawable(rsrc);
			image.setImageDrawable(img);
		}else{
			//new AsyncDownloadImage( image ).execute( parcel.image );
		}
		
		rater.setText( parcel.seller );
		rating.setText( parcel.maxValue>10 ? fmt.format(parcel.value) : parcel.value 
				+ " (" + parcel.minValue + "-" + parcel.maxValue + ")" );
		
		
		return row;
	}

}
