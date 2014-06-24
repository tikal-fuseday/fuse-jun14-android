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

	private boolean _isTracking;
	
	public void StartGpsManager()
	{
		_isTracking = true;
		if(gpsLocationManager == null)
		{
			gpsLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);	
		}
		
	    gpsTracker = new GpsTracker();
				
		if(gpsTrackerListener == null)
		{
			gpsTrackerListener = new GpsTrackerListener(getGpsTracker());
		}
		
		gpsLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, gpsTrackerListener);		
	}
	
	public void StopGpsManager()
	{
		_isTracking = false;
		gpsLocationManager.removeUpdates(gpsTrackerListener);
	}


	@Override
	public IBinder onBind(Intent arg0) {
		return gpsTrackingBinder;
	}
	
	public class GpsTrackingBinder extends Binder {
		public GpsTrackerService getService()
		{
			return GpsTrackerService.this;
		}
	}

	public boolean isTracking() {
		return _isTracking;
	}
	public GpsTracker getGpsTracker() {
		return gpsTracker;
	}
}
