package com.prestonmueller.rebounder;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;

/**
 * Created by prestonmueller on 1/20/17.
 */

public class ModuleLoudRinger implements Module {
    @Override
    public String name() {
        return "LoudRinger";
    }

    @Override
    public String humanReadableName() {
        return "Loud Ringer";
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
        return "Loud Ringer sets the ringer volume of your phone to 100%. Helpful if you lose it!";
    }

    @Override
    public String triggerString() {
        return "#loud";
    }

    @Override
    public boolean runCheck(String message, Context c) {
        SharedPreferences sharedPreferences = c.getApplicationContext().getSharedPreferences("rebounderPrefs", Context.MODE_PRIVATE);
        String triggerCode = sharedPreferences.getString("module_triggerCode_" + name(), triggerString());

        if(message.contains(triggerCode)) return true;
        return false;
    }

    @Override
    public void commence(String sender, String message, Context c, RebounderReceiver caller) {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NotificationManager nm = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
                nm.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
            }

            AudioManager manager = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
            manager.setStreamVolume(AudioManager.STREAM_RING, manager.getStreamMaxVolume(AudioManager.STREAM_RING), 0);

            caller.sendResponse(sender, "Ringer set to maximum volume.", c);
        }
        catch (Exception e) {
            caller.sendResponse(sender, "Failed to set ringer to maximum volume, or pull phone out of Do Not Disturb mode.", c);
        }

    }
}
