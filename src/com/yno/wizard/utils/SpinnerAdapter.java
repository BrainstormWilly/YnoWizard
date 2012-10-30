package com.yno.wizard.utils;


import java.util.List;

import com.yno.wizard.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SpinnerAdapter extends ArrayAdapter<String> {

	private Context _context;
	private List<String> _items;
	
	public SpinnerAdapter(Context $context, int $textView, List<String> $items){
		super($context, $textView, $items);
		_context = $context;
		_items = $items;
	}
	
	@Override
	public View getDropDownView(int $position, View $convertView, ViewGroup $parent) {
		//return super.getDropDownView($position, $convertView, $parent);
		return getCustomView($position, $convertView, $parent);

	}
	
	@Override
	public View getView(int $position, View $convertView, ViewGroup $parent) {

		View view = getCustomView($position, $convertView, $parent);
		view.setBackgroundColor(_context.getResources().getColor(android.R.color.transparent));
		((TextView) view).setTextColor( _context.getResources().getColorStateList(R.color.yw_edittext_textcolor) );
		((TextView) view).setTextSize(16);
		view.setPadding(0, 0, 0, 0);
		
//		TextView tv = (TextView) view.findViewById(com.yno.wizard.R.id.spinnerItemTV);
//		tv.setTextColor( _context.getResources().getColorStateList(R.color.yw_edittext_textcolor) );
		return view;
	}
	
	public View getCustomView(int $position, View $convertView, ViewGroup $parent){
		LayoutInflater inflater = LayoutInflater.from(_context);
		if( $convertView==null )
			$convertView = inflater.inflate(com.yno.wizard.R.layout.spinner_item, null);
		
		//TextView tv = (TextView) $convertView.findViewById(com.yno.wizard.R.id.spinnerItemTV);
		//tv.setText(_items.get($position));
		((TextView) $convertView).setText(_items.get($position));
		
		return $convertView;
			
		
	}
}
