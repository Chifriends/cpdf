package com.cpdf;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Address;
import android.location.Geocoder;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@SuppressLint("ParserError")
public class GetAddress extends Activity implements LocationListener {

	private TextView myAddress;
	private LocationManager locationManager;
	private String provider;
	private double LATITUDE;
	private double LONGITUDE;
	
	//Date & Time value
	Date now = new Date();
	//Let's think more about range time, for example 1~5pm = afternoon, 5~8 = evening, something like this..
	String dateTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(now);
	
	// textView is the TextView view that should display it
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_getaddress);
		myAddress = (TextView) findViewById(R.id.myaddress);
		
		// Get the location manager
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// Define the criteria how to select the location provider -> use
		// default
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);
		
		onLocationChanged(location);

		Geocoder geocoder = new Geocoder (this, Locale.ENGLISH);
		
		try {
			List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
			
			if(addresses.size() > 0 && addresses != null) {
				Address returnedAddress = addresses.get(0);
				myAddress.setText(returnedAddress.getFeatureName()+"_"+returnedAddress.getThoroughfare()+"_"+ dateTime);
			}
			else { 	
				myAddress.setText(R.string.default_filename+"_"+ dateTime);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			myAddress.setText(R.string.default_filename+"_"+ dateTime);
		}
	}

	/* Request updates at startup */
	@Override
	protected void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(provider, 400, 1, this);
	}

	/* Remove the locationlistener updates when Activity is paused */
	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
	}

	//@Override
	public void onLocationChanged(Location location) {
		LATITUDE = (double) (location.getLatitude());
		LONGITUDE = (double) (location.getLongitude());
	}

	//@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	//@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(this, "Enabled new provider " + provider,
				Toast.LENGTH_SHORT).show();

	}

	//@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(this, "Disabled provider " + provider,
				Toast.LENGTH_SHORT).show();
	}
	
}
