package com.tikal.lifestyle;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
import android.os.Binder;

public class GpsTrackerService extends Service{
	private LocationManager gpsLocationManager;
	
	private IBinder gpsTrackingBinder = new GpsTrackingBinder();
	private GpsTrackerListener gpsTrackerListener;
	private GpsTracker gpsTracker;
	
	public void StartGpsManager()
	{
		if(gpsLocationManager == null)
		{
			gpsLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);	
		}
		
		if(gpsTracker == null)
		{
			gpsTracker = new GpsTracker();
		}
		
		if(gpsTrackerListener == null)
		{
			gpsTrackerListener = new GpsTrackerListener(gpsTracker);
		}
		
		gpsLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, gpsTrackerListener);
	}
	
	public void StopGpsManager()
	{
		gpsLocationManager.removeUpdates(gpsTrackerListener);
	}


	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return gpsTrackingBinder;
	}
}
