package com.example.test;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class MyLocationListener implements LocationListener{

	private double latitude,longitude;
	
	
	@Override
	public void onLocationChanged(Location loc) {
		// TODO Auto-generated method stub
		latitude =loc.getLatitude();
		longitude = loc.getLongitude();
	}
	
	public double latitude(){
		return latitude;
	}
	
	public double longitude(){
		return longitude;
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

}
