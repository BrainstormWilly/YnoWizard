package com.yno.wizard.view;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.internal.nineoldandroids.animation.ObjectAnimator;
import com.yno.wizard.model.WineParcel;
import com.yno.wizard.utils.WineSubnavHelper;
import com.yno.wizard.R;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WineSelectDetailsFragment extends SherlockFragment {
	
	private WineSubnavHelper _helper;
	
	public static WineSelectDetailsFragment newInstance( WineParcel $wine, ArrayList<String> $subnav ){
		Bundle arg = new Bundle();
		arg.putParcelable(WineParcel.NAME, $wine);
		arg.putStringArrayList("subnav", $subnav);
		
		WineSelectDetailsFragment frag = new WineSelectDetailsFragment();
		frag.setArguments( arg );
		return frag;
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView( LayoutInflater $inflator, ViewGroup $container, Bundle $savedInstanceState){
		View view = $inflator.inflate(R.layout.wine_select_details, $container, false);
		TextView varTV = (TextView) view.findViewById(R.id.wineSelectDetailsVarietalTV);
		TextView regTV = (TextView) view.findViewById( R.id.wineSelectDetailsRegionTV );
		TextView proTV = (TextView) view.findViewById(R.id.wineSelectDetailsProducerTV);
		
		WineParcel wine = getArguments().getParcelable( WineParcel.NAME );
		ArrayList<String> subnav = getArguments().getStringArrayList("subnav");
		_helper = new WineSubnavHelper(view);
		_helper.setNav( subnav, WineSelectActivity.NAV_DETAILS);
		
		varTV.setText( wine.varietal );
		regTV.setText( wine.region );
		proTV.setText( wine.producer );
		
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
