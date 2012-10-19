package com.ynotasting.finder.view;

import java.util.ArrayList;
import java.util.List;

import com.ynotasting.finder.R;
import com.ynotasting.finder.controller.ShowPriceVendorCommand;
import com.ynotasting.finder.model.PriceModel;
import com.ynotasting.finder.model.PriceParcel;
import com.ynotasting.finder.model.WineParcel;
import com.ynotasting.finder.utils.WineSelectPriceListAdapter;
import com.ynotasting.finder.utils.WineSubnavHelper;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class WineSelectPricesFragment extends Fragment implements OnItemClickListener {
	
	public static final String TAG = WineSelectPricesFragment.class.getSimpleName();
	
	private WineSubnavHelper _helper;
	
	private List<PriceParcel> _prices;
	
	public static WineSelectPricesFragment newInstance( WineParcel $wine, ArrayList<String> $subnav ){
		Bundle arg = new Bundle();
		arg.putParcelable(WineParcel.NAME, $wine);
		arg.putStringArrayList("subnav", $subnav);
		
		WineSelectPricesFragment frag = new WineSelectPricesFragment();
		frag.setArguments( arg );
		return frag;
	}

	
	@Override
	public View onCreateView( LayoutInflater $inflator, ViewGroup $container, Bundle $savedInstanceState){
		View view = $inflator.inflate(R.layout.wine_select_prices, $container, false);
		TextView rngTV = (TextView) view.findViewById(R.id.wineSelectPriceRangeTV);
		ListView retailersLV = (ListView) view.findViewById( R.id.wineSelectPriceLV ); 
		
		WineParcel wine = getArguments().getParcelable( WineParcel.NAME );
		PriceModel model = new PriceModel( wine.prices);
		_prices = model.getSortedList();
		WineSelectPriceListAdapter adapter = new WineSelectPriceListAdapter( getActivity().getApplicationContext(), model );
		
		ArrayList<String> subnav = getArguments().getStringArrayList("subnav");
		_helper = new WineSubnavHelper(view);
		_helper.setNav( subnav, WineSelectActivity.NAV_PRICES);
		
		rngTV.setText("Price Range: $" + model.getLowest().value + "-$" + model.getHighest().value);
		
		retailersLV.setAdapter( adapter );
		retailersLV.setOnItemClickListener( this );
		
		return view;
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ShowPriceVendorCommand cmd = new ShowPriceVendorCommand(getActivity());
		cmd.payload.putParcelable(PriceParcel.NAME, _prices.get(arg2));
		cmd.execute();
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
