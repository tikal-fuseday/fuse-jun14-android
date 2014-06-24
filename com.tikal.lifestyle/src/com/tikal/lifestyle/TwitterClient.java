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

public class TwitterClient {

	public void SendLocation(float lat, float lon, float ele, String text)
	{
		JSONObject parent = new JSONObject();
		SimpleDateFormat format = new SimpleDateFormat();
		String date = format.format(new Date());
		try {
			parent.put("created_at", date);
			parent.put("text", text + " at: " + lat + ":" + lon + ";" + ele);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Send(parent.toString());
	}
	
	private void Send(String text)
	{
		try {
            URL url = new URL("http://localhost/TwitterService");
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
