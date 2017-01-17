package com.prestonmueller.rebounder;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by prestonmueller on 1/6/16.
 */
public class ModuleETA implements Module, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    RebounderReceiver caller;
    String sender;
    Context c;

    String destination = "";

    private int numberOfUpdates = 0;

    @Override
    public String name() {
        return "eta";
    }

    @Override
    public String humanReadableName() {
        return "ETA";
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
        return "Provides the driving ETA (estimated time to arrival) to the destination specified after the trigger code, traffic included. For example: #eta Nashua, NH";
    }

    @Override
    public String triggerString() {
        return "#eta";
    }

    @Override
    public boolean runCheck(String message, Context c) {
        SharedPreferences sharedPreferences = c.getApplicationContext().getSharedPreferences("rebounderPrefs", Context.MODE_PRIVATE);
        String triggerCode = sharedPreferences.getString("module_triggerCode_" + name(), triggerString());

        if(message.contains(triggerCode)) {

            destination = message.substring(message.indexOf(triggerCode)+triggerCode.length()+1, message.length());

            return true;
        }
        return false;
    }

    @Override
    public void commence(String sender, String message, Context c, RebounderReceiver caller) {

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

        if(numberOfUpdates == 4) {

            String lat = Double.toString(location.getLatitude());
            String lon = Double.toString(location.getLongitude());

            String httpCall = null;
            try {
                httpCall = "https://maps.googleapis.com/maps/api/directions/json?origin=" + lat + "," + lon + "&destination=" + URLEncoder.encode(destination, "UTF-8") + "&key=" + APIAccess.GoogleMaps;
            }
            catch(UnsupportedEncodingException e) {
            }

            RequestQueue queue = Volley.newRequestQueue(c);
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, httpCall,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            String returnSMS = "";

                            try {

                                returnSMS += response.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("duration").getString("text");
                                returnSMS += " from now.";
                            }
                            catch(JSONException e) {
                                returnSMS += "ETA could not be calculated, please try again.";
                            }

                            caller.sendResponse(sender, returnSMS, c);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(stringRequest);



            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
            numberOfUpdates = 0;
        }

    }
}
