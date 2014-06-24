package com.tikal.lifestyle;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
//import android.location.GpsStatus;
import android.os.Bundle;

public class GpsTracker{
	
	private Context executingContext;
	private ArrayList<Location> locationData;
	
	public GpsTracker()
	{
		//executingContext = context;
		locationData = new ArrayList<Location>();
	}
	
	public void LogLocation(Location loc)
	{
		locationData.add(loc);
	}
	
	public String GetLocationData()
	{
		StringBuilder sb = new StringBuilder();
		for (Location loc : locationData) {
			sb.append("Latitude: "+ loc.getLatitude() +", Longtitude: "+loc.getLongitude());
			sb.append("\n");
		}
		
		return sb.toString();
	}
}
