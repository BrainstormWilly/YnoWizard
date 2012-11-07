package com.yno.wizard.model;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationModel implements LocationListener {
	
	public static final String TAG = LocationModel.class.getSimpleName();
	

	private Context _context;
	private LocationManager _manager;
	private Location _location;
	private String _provider;
	private HashMap<String, String> _states = new HashMap<String, String>();
	
	public boolean hasLocation(){
		return _location!=null;
	}
	
	public LocationModel( Context $context ){
		_context = $context;
		_manager = (LocationManager) _context.getSystemService(Context.LOCATION_SERVICE);
		
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		
		_provider = _manager.getBestProvider(criteria, false);
		if( _provider==null )
			_provider = LocationManager.GPS_PROVIDER;
		//Log.d(TAG, "Location Provider -> " + _provider);
			
		
		_states.put("Alabama", "AL");
		_states.put("Alaska", "AK");
		_states.put("American Samoa", "AS");
		_states.put("Arizona", "AZ");
		_states.put("Arkansas", "AR");
		_states.put("California", "CA");
		_states.put("Colorado", "CO");
		_states.put("Connecticut", "CT");
		_states.put("Delaware", "DE");
		_states.put("District of Columbia", "DC");
		_states.put("Florida", "FL");
		_states.put("Georgia", "GA");
		_states.put("Guam", "GU");
		_states.put("Hawaii", "HI");
		_states.put("Idaho", "ID");
		_states.put("Illinois", "IL");
		_states.put("Indiana", "IN");
		_states.put("Iowa", "IA");
		_states.put("Kansas", "KS");
		_states.put("Kentucky", "KY");
		_states.put("Louisiana", "LA");
		_states.put("Maine", "ME");
		_states.put("Marshall Islands", "MH");
		_states.put("Maryland", "MD");
		_states.put("Massachusetts", "MA");
		_states.put("Michigan", "MI");
		_states.put("Minnesota", "MN");
		_states.put("Mississippi", "MS");
		_states.put("Missouri", "MO");
		_states.put("Montana", "MT");
		_states.put("Nebraska", "NE");
		_states.put("Nevada", "NV");
		_states.put("New Hampshire", "NH");
		_states.put("New Jersey", "NJ");
		_states.put("New Mexico", "NM");
		_states.put("New York", "NY");
		_states.put("North Carolina", "NC");
		_states.put("North Dakota", "ND");
		_states.put("Ohio", "OH");
		_states.put("Oklahoma", "OK");
		_states.put("Oregon", "OR");
		_states.put("Palau", "PW");
		_states.put("Pennsylvania", "PA");
		_states.put("Puerto Rico", "PR");
		_states.put("Rhode Island", "RI");
		_states.put("South Carolina", "SC");
		_states.put("South Dakota", "SD");
		_states.put("Tennessee", "TN");
		_states.put("Texas", "TX");
		_states.put("Utah", "UT");
		_states.put("Vermont", "VT");
		_states.put("Virginia", "VA");
		_states.put("Virgin Islands", "VI");
		_states.put("Washington", "WA");
		_states.put("West Virginia", "WV");
		_states.put("Wisconsin", "WI");
		_states.put("Wyoming", "WY");	
		
		Location loc = _manager.getLastKnownLocation(_provider);
		
		if( loc!=null )
			onLocationChanged( loc );
		else
			enable();
	}
	
	public String getLocalIpAddress() {
	    try {
	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	            NetworkInterface intf = en.nextElement();
	            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                InetAddress inetAddress = enumIpAddr.nextElement();
	                if( !inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() ) {
	                	//Log.d(TAG, "IP = " + inetAddress.getHostAddress().toString());
	                    return inetAddress.getHostAddress().toString();
	                }
	            }
	        }
	    } catch (SocketException ex) {
	        //Log.e(TAG, "no IP found: " + ex.toString());
	    }
	    return "";
	}
	
	public Address getPrimaryAddress(){
		try{
			Geocoder gc = new Geocoder(_context, Locale.getDefault());
			List<Address> addrs = gc.getFromLocation(_location.getLatitude(), _location.getLongitude(), 1);
			//Log.d(TAG, "addresses = " + addrs.size());
			if( addrs!=null && addrs.size()>0 )
				return addrs.get(0);
		}catch(Exception $e){
			//Log.d(TAG, "no addresses found");
			$e.printStackTrace();
		}
		return null;
	}
	
	public String getStateCode(String $state){
		if( $state.length()==2 ) return $state;
		if( _states.get($state)!=null ) return _states.get($state);
		return "";
	}
	
	public void enable(){
		_manager.requestLocationUpdates(_provider, 0, 0, this);
	}
	
	public void disable(){
		_manager.removeUpdates(this);
	}
	
	@Override
	public void onLocationChanged(Location location) {
		//Log.d(TAG, "onLocationChanged");
		disable();
		_location = location;
	}
	
	@Override
	public void onProviderDisabled(String provider) {
		//Log.d(TAG, "onProviderDisabled");
	}
	
	@Override
	public void onProviderEnabled(String provider) {
		//Log.d(TAG, "onProviderEnabled");
	}
	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		//Log.d(TAG, "onStatusChanged");
		
	}

	
}
