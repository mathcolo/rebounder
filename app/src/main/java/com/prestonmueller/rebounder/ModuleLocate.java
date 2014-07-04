package com.prestonmueller.rebounder;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;

public class ModuleLocate implements Module, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    LocationClient locationClient;
    LocationRequest locationRequest;
    RebounderReceiver caller;
    String sender;
    Context c;

	private int numberOfUpdates = 0;

	public String triggerString() {
		return "locate";
	}
	
	@Override
	public String name() {
		return "GPS Location";
	}

	@Override
	public String version() {
		return "1.0";
	}

	@Override
	public String author() {
		return "Preston Mueller";
	}

	@Override
	public String description() {
		return "The GPS Location module polls your phone's current GPS location and responds with a Google Maps link. Requires an enabled GPS or high accuracy mode in Android 4.4 KitKat.";
	}
	
	@Override
	public boolean runCheck(String message, String prefix) {
        return message.contains(prefix + triggerString());
    }

	@Override
	public void commence(final String sender, String message, final Context c,
			final RebounderReceiver caller) {
        locationClient = new LocationClient(c, this, this);
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setNumUpdates(5);
        this.caller = caller;
        this.sender = sender;
        this.c = c;

        locationClient.connect();

	}

    @Override
    public void onConnected(Bundle bundle) {

        locationClient.requestLocationUpdates(locationRequest, this);

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        // Called when a new location is found by the network location provider.
        numberOfUpdates++;

        double accuracy = location.getAccuracy();
        if(numberOfUpdates == 4) {

            DecimalFormat accuracyFormatter = new DecimalFormat("#.##");

            String lat = Double.toString(location.getLatitude());
            String lon = Double.toString(location.getLongitude());

            String url = null;

            Date now = new Date();
            Format df = new SimpleDateFormat("MMM dd hh:mm a z", Locale.US);
            String dateString = df.format(now);

            url = "As of " + dateString + ":\n\n" + "http://maps.google.com/maps?q=" + "loc:" + lat + ",+" + lon + "\n\nAccuracy: " + accuracyFormatter.format(accuracy) + "m";

            caller.sendResponse(sender, url, c);

            locationClient.removeLocationUpdates(this);
            locationClient.disconnect();
            numberOfUpdates = 0;
        }

    }
}
