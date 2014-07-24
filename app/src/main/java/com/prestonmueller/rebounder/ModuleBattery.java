package com.prestonmueller.rebounder;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.preference.PreferenceManager;
import android.util.Log;

public class ModuleBattery implements Module {
	
	public String triggerString() {
		return "#battery";
	}
	
	@Override
	public String name() {
		return "Battery";
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
		return "The battery module allows your phone to be queried for its battery level. Responds with a value like 25% or 71%.";
	}

	@Override
	public boolean runCheck(String message, Context c) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        String triggerCode = sharedPreferences.getString("module_triggerCode_" + name(), triggerString());

		if(message.contains(triggerCode)) return true;
		return false;
	}

	@Override
	public void commence(String sender, String message, Context c,
			RebounderReceiver caller) {
		
		Intent batteryIntent = c.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        if(batteryIntent == null) {
            return;
        }

		int rawlevel = batteryIntent.getIntExtra("level", -1);
		double scale = batteryIntent.getIntExtra("scale", -1);

        int plugged = batteryIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean charging = (plugged == BatteryManager.BATTERY_PLUGGED_AC) || (plugged == BatteryManager.BATTERY_PLUGGED_USB);

		double level = -1;
		if (rawlevel >= 0 && scale > 0) {
			level = rawlevel / scale;
		}
		level = level * 100;
		int levelInt = (int)level;

        String response = levelInt + "%";

        if(charging && levelInt == 100) response += " (Charged)";
        else if(charging) response += " (Charging)";

		caller.sendResponse(sender, response, c);
		
	}


}
