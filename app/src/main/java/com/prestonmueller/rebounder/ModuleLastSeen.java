package com.prestonmueller.rebounder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Date;

/**
 * Created by prestonmueller on 10/29/14.
 */
public class ModuleLastSeen extends BroadcastReceiver implements Module {



    @Override
    public void onReceive(Context context, Intent intent) {

        String strAction = intent.getAction();

        if (strAction.equals(Intent.ACTION_SCREEN_ON)) {

            Date current = new Date();
            SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(context).edit();
            prefs.putLong("lastSeen_date", current.getTime());
        }

    }

    @Override
    public String name() {
        return "Last Seen";
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
        return "Reports the most recent time the device's screen was on.";
    }

    @Override
    public String triggerString() {
        return "#seen";
    }

    @Override
    public boolean runCheck(String message, Context c) {
        return false;
    }

    @Override
    public void commence(String sender, String message, Context c, RebounderReceiver caller) {

    }
}
