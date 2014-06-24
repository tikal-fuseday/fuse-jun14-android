package com.tikal.lifestyle;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

public class TwitterClient {

	public void SendLocation(double lat, double lon, float ele, String text, Context context)
	{
		JSONObject parent = new JSONObject();
		SimpleDateFormat format = new SimpleDateFormat();
		String date = format.format(new Date());
		String message = text + " at: " + lat + ":" + lon + ";" + ele;
		try {
			parent.put("created_at", date);
			parent.put("text", message);
			parent.put("lat", lat);
			parent.put("lon", lon);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		final String json = parent.toString();
		new Thread(new Runnable() {

			@Override
			public void run() {
				Send(json);				
			}
			
		}).start();
		
	
	}
	
	private void Send(String text)
	{
		try {
            URL url = new URL("http://192.168.1.80:8080/save/twit");
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(text);
            out.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));

            while (in.readLine() != null) {
            }
            System.out.println("\nREST Service Invoked Successfully..");
            in.close();
        } catch (Exception e) {
            System.out.println("\nError while calling REST Service");
            System.out.println(e);
        }
	}
}
