package com.ynotasting.finder.utils;

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

import com.ynotasting.finder.R;
import com.ynotasting.finder.model.PriceModel;
import com.ynotasting.finder.model.PriceParcel;

public class WineSelectPriceListAdapter extends BaseAdapter implements ListAdapter {
	
	private final Context _context;
	private final List<PriceParcel> _values;
	private final PriceModel _model;
	
	public WineSelectPriceListAdapter( Context $context, PriceModel $model ){
		_context = $context;
		_model = $model;
		_values = _model.getSortedList();
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
		
		View row = inflater.inflate(R.layout.wine_select_prices_row, parent, false);
		ImageView image = (ImageView) row.findViewById(R.id.wineSelectPricesIV);
		TextView retailer = (TextView) row.findViewById(R.id.wineSelectPricesRetailerTV);
		TextView price = (TextView) row.findViewById(R.id.wineSelectPricesPriceTV);
		PriceParcel parcel = _values.get(position);
		
//		if( parcel.image.indexOf("drawable")==0 ){
//			int rsrc = _context.getResources().getIdentifier( parcel.image, null, _context.getPackageName() );
//			Drawable img = _context.getResources().getDrawable(rsrc);
//			image.setImageDrawable(img);
//		}else{
//			new AsyncDownloadImage( image ).execute( parcel.image );
//		}
		
		image.setImageDrawable(_context.getResources().getDrawable(parcel.sponsor.logo));
		
		retailer.setText( parcel.seller );
		
		// this is just a guess, but there is no 
		// other criteria to go on
		if( parcel.value > _model.getLowest().value*6 )
			price.setText( "$" + parcel.getCaseValueString() );
		else
			price.setText( "$" + parcel.getValueString() );
		
		return row;
	}

}
