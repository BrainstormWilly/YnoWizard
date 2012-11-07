package com.yno.wizard.view;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.yno.wizard.R;
import com.yno.wizard.controller.ShowPriceVendorCommand;
import com.yno.wizard.controller.TrackBuyLinkCommand;
import com.yno.wizard.model.PriceModel;
import com.yno.wizard.model.PriceParcel;
import com.yno.wizard.model.WineParcel;
import com.yno.wizard.utils.WineSelectPriceListAdapter;
import com.yno.wizard.utils.WineSubnavHelper;

public class WineSelectPricesFragment extends Fragment implements OnItemClickListener {
	
	public static final String TAG = WineSelectPricesFragment.class.getSimpleName();
	
	private WineSubnavHelper _helper;
	
	private List<PriceParcel> _prices;
	private WineParcel _wine;
	
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
		
		_wine = getArguments().getParcelable( WineParcel.NAME );
		PriceModel model = new PriceModel( _wine.prices);
		_prices = model.getSortedList();
		WineSelectPriceListAdapter adapter = new WineSelectPriceListAdapter( getActivity().getApplicationContext(), model );
		
		ArrayList<String> subnav = getArguments().getStringArrayList("subnav");
		_helper = new WineSubnavHelper(view);
		_helper.setNav( subnav, WineSelectActivity.NAV_PRICES);
		
		rngTV.setText( getString(R.string.price_range) + String.valueOf(model.getLowest().value) + getString(R.string.price_range_to) + String.valueOf(model.getHighest().value));
		
		retailersLV.setAdapter( adapter );
		retailersLV.setOnItemClickListener( this );
		
		return view;
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		TrackBuyLinkCommand tmd = new TrackBuyLinkCommand();
		tmd.payload.putParcelable(PriceParcel.NAME, _prices.get(arg2));
		tmd.payload.putParcelable(WineParcel.NAME, _wine);
		tmd.execute();
		
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
