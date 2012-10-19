package com.ynotasting.finder.utils;

import com.ynotasting.finder.R;
import com.ynotasting.finder.model.db.SearchTypesTable;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CursorSpinnerAdapter extends SimpleCursorAdapter {
	
	public static final String TAG = CursorSpinnerAdapter.class.getSimpleName();

	private Context _context;
	private Cursor _cursor;
	
	public CursorSpinnerAdapter(Context $context, int $layout, Cursor $cursor, String[] $from, int[] $to, int $flags){
		super($context, $layout, $cursor, $from, $to, $flags);
		_context = $context;
		_cursor = $cursor;
	}
	
	@Override
	public View getDropDownView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		//return super.getDropDownView(arg0, arg1, arg2);
		return getCustomView(arg0, arg1, arg2);
	}
	
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		//return super.getView(arg0, arg1, arg2);
		View view = getCustomView(arg0, arg1, arg2);
		view.setBackgroundColor(_context.getResources().getColor(android.R.color.transparent));
		((TextView) view).setTextColor( _context.getResources().getColorStateList(R.color.yw_edittext_textcolor) );
		((TextView) view).setTextSize(16);
		view.setPadding(0, 0, 0, 0);
		return view;
	}
	
	private View getCustomView(int $position, View $convertView, ViewGroup $parent){
		LayoutInflater inflater = LayoutInflater.from(_context);
		if( $convertView==null )
			$convertView = inflater.inflate(com.ynotasting.finder.R.layout.spinner_item, null);
		
		//TextView tv = (TextView) $convertView.findViewById(com.ynotasting.finder.R.id.spinnerItemTV);
		//tv.setText(_items.get($position));
		_cursor.moveToPosition($position);
		((TextView) $convertView).setText( _cursor.getString( _cursor.getColumnIndex(SearchTypesTable.COLUMN_NAME) ) );
		
		return $convertView;
	}
	
}
