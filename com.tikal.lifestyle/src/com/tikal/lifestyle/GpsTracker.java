package com.tikal.lifestyle;

import java.util.ArrayList;

import android.location.Location;

public class GpsTracker{
	
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
