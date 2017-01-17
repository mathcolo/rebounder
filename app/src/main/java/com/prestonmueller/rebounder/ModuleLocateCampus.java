package com.prestonmueller.rebounder;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ModuleLocateCampus implements Module, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    RebounderReceiver caller;
    String sender;
    Context c;

	private int numberOfUpdates = 0;

	public String triggerString() {
		return "#campus";
	}
	
	@Override
	public String name() {
		return "CampusLocation";
	}

    @Override
    public String humanReadableName() {
        return "Campus Loc.";
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
		return "The GPS Location module polls your phone's current GPS location and responds with your WPI building location. Requires an enabled GPS or high accuracy mode in Android 4.4 or newer.";
	}

    @Override
    public boolean runCheck(String message, Context c) {

        SharedPreferences sharedPreferences = c.getApplicationContext().getSharedPreferences("rebounderPrefs", Context.MODE_PRIVATE);
        String triggerCode = sharedPreferences.getString("module_triggerCode_" + name(), triggerString());

        if(message.contains(triggerCode)) return true;
        return false;
    }

	@Override
	public void commence(final String sender, String message, final Context c,
			final RebounderReceiver caller) {

        googleApiClient = new GoogleApiClient.Builder(c).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setNumUpdates(5);
        this.caller = caller;
        this.sender = sender;
        this.c = c;

        googleApiClient.connect();

	}

    @Override
    public void onConnected(Bundle bundle) {

        if (ContextCompat.checkSelfPermission( c, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

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

            url = "As of " + dateString + ":\nCampus loc: " + CampusUtilities.locationWPI(location.getLatitude(), location.getLongitude());

            caller.sendResponse(sender, url, c);

            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
            numberOfUpdates = 0;
        }

    }
}
