package com.prestonmueller.rebounder;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by prestonmueller on 1/20/15.
 */
public class RebounderPreferencesManager {

    private SharedPreferences preferences;

    private static RebounderPreferencesManager ourInstance = new RebounderPreferencesManager();

    public static RebounderPreferencesManager getInstance() {
        return ourInstance;
    }

    private RebounderPreferencesManager() {
    }

    public SharedPreferences getSharedPreferences() {
        return preferences;
    }

    private void initWithContext(Context c) {
        preferences = c.getSharedPreferences("rebounderPrefs", Context.MODE_PRIVATE);
    }

}
