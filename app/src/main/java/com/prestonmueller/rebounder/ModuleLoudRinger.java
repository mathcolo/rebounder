package com.prestonmueller.rebounder;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;

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

        AudioManager manager = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        manager.setStreamVolume(AudioManager.STREAM_RING, manager.getStreamMaxVolume(AudioManager.STREAM_RING), 0);

        caller.sendResponse(sender, "Ringer set to maximum volume.", c);
    }
}
