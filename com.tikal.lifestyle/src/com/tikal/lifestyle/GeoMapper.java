package com.tikal.lifestyle;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

public class GeoMapper {

	public String getAddress(Context context, double lat, double lon)
	{
		try{
			Geocoder geo = new Geocoder(context, Locale.getDefault());

			List<Address> addresses = geo.getFromLocation(lat, lon, 1);
			String result = "";
			for (int i = 0; i < addresses.size(); i++) {
				Address address = addresses.get(i);

				result += "Address #" + i + "\n";

				for (int j = 0; j < address.getMaxAddressLineIndex(); j++) {
					result += address.getAddressLine(j) + "\n";
				}

				if (address.getAdminArea() != null)
					result += address.getAdminArea() + "\n";
				result += address.getCountryName();
				result += address.getPostalCode() + "\n";
			}

			return result;
		}

		catch (Exception e) {
			e.printStackTrace(); 
		}

		return null;
	}
}
