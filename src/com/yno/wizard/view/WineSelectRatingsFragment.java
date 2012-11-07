package com.yno.wizard.view;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.yno.wizard.R;
import com.yno.wizard.model.RatingParcel;
import com.yno.wizard.model.RatingsModel;
import com.yno.wizard.model.WineParcel;
import com.yno.wizard.utils.WineSelectRatingListAdapter;
import com.yno.wizard.utils.WineSubnavHelper;

public class WineSelectRatingsFragment extends Fragment {
	
	public static final String TAG = WineSelectRatingsFragment.class.getSimpleName();
	
	private WineSubnavHelper _helper;
	private Button _rateBtn;
	private List<RatingParcel> _ratings;

	public static WineSelectRatingsFragment newInstance( WineParcel $wine, ArrayList<String> $subnav ){
		Bundle arg = new Bundle();
		arg.putParcelable(WineParcel.NAME, $wine);
		arg.putStringArrayList("subnav", $subnav);
		
		WineSelectRatingsFragment frag = new WineSelectRatingsFragment();
		frag.setArguments( arg );
		return frag;
	}

	
	@Override
	public View onCreateView( LayoutInflater $inflator, ViewGroup $container, Bundle $savedInstanceState){
		View view = $inflator.inflate(R.layout.wine_select_ratings, $container, false);
		//Button _rateBtn = (Button) view.findViewById(R.id.wineSelectRatingRateBtn);
		TextView rngTV = (TextView) view.findViewById(R.id.wineSelectRatingRangeTV);
		ListView retailersLV = (ListView) view.findViewById( R.id.wineSelectRatingLV ); 
		
		final WineParcel wine = getArguments().getParcelable( WineParcel.NAME );
		RatingsModel model = new RatingsModel( wine.ratings );
		_ratings = model.getSortedList();
		WineSelectRatingListAdapter adapter = new WineSelectRatingListAdapter( getActivity().getApplicationContext(), _ratings );
		
		ArrayList<String> subnav = getArguments().getStringArrayList("subnav");
		_helper = new WineSubnavHelper(view);
		_helper.setNav( subnav, WineSelectActivity.NAV_RATINGS);
		rngTV.setText( getString(R.string.rating_range) + String.valueOf( model.getLowest().value ) + "-" + String.valueOf( model.getHighest().value ) );
		
		retailersLV.setAdapter( adapter );
		
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
