package com.tikal.lifestyle;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class GpsTrackerListener implements LocationListener {

	private GpsTracker gpsTracker;
	public GpsTrackerListener(GpsTracker tracker)
	{
		gpsTracker = tracker;		
	}
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		gpsTracker.LogLocation(location);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
