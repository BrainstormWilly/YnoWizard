package com.yno.wizard.view;

import java.util.ArrayList;

import com.yno.wizard.model.WineParcel;
import com.yno.wizard.view.assist.WineSubnavAssist;
import com.yno.wizard.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WineSelectNotesFragment extends Fragment {

	public static String TAG = WineSelectNotesFragment.class.getSimpleName();
	
	private WineSubnavAssist _helper;
	
	public static WineSelectNotesFragment newInstance( WineParcel $wine, ArrayList<Integer> $subnav ){
		Bundle arg = new Bundle();
		arg.putParcelable(WineParcel.NAME, $wine);
		arg.putIntegerArrayList("subnav", $subnav);
		
		WineSelectNotesFragment frag = new WineSelectNotesFragment();
		frag.setArguments( arg );
		return frag;
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView( LayoutInflater $inflator, ViewGroup $container, Bundle $savedInstanceState){
		View view = $inflator.inflate(R.layout.wine_select_notes, $container, false);
		TextView descTV = (TextView) view.findViewById(R.id.wineSelectNotesTV);
		
		WineParcel wine = getArguments().getParcelable( WineParcel.NAME );
		ArrayList<Integer> subnav = getArguments().getIntegerArrayList("subnav");
		_helper = new WineSubnavAssist(view);
		_helper.setNav( subnav, R.string.select_pager_subtitle_tasting_notes);
		
		descTV.setText( wine.notes );
		
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
