package com.yno.wizard.view;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.yno.wizard.R;
import com.yno.wizard.model.WineParcel;
import com.yno.wizard.view.assist.WineSubnavAssist;

public class WineSelectDescripFragment extends SherlockFragment {
	
	public static String TAG = WineSelectDescripFragment.class.getSimpleName();
	private WineSubnavAssist _helper;
	
	public static WineSelectDescripFragment newInstance( WineParcel $wine, ArrayList<Integer> $subnav ){
		Bundle arg = new Bundle();
		arg.putParcelable(WineParcel.NAME, $wine);
		arg.putIntegerArrayList("subnav", $subnav);
		
		WineSelectDescripFragment frag = new WineSelectDescripFragment();
		frag.setArguments( arg );
		return frag;
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView( LayoutInflater $inflator, ViewGroup $container, Bundle $savedInstanceState){
		View view = $inflator.inflate(R.layout.wine_select_description, $container, false);
		TextView descTV = (TextView) view.findViewById(R.id.wineSelectDescrTV);
		
		WineParcel wine = getArguments().getParcelable( WineParcel.NAME );
		ArrayList<Integer> subnav = getArguments().getIntegerArrayList("subnav");
		_helper = new WineSubnavAssist(view);
		_helper.setNav(subnav, R.string.select_pager_subnav_description);
		
		descTV.setText( wine.description );
		
		return view;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		_helper.reset();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		_helper.resume();
	}

}
