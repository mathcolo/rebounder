package com.prestonmueller.rebounder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


public class ReceiverLastSeen extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
        SharedPreferences.Editor edit = context.getSharedPreferences("rebounderPrefs", Context.MODE_PRIVATE).edit();
        edit.putLong("lastSeen", System.currentTimeMillis()).apply();
	}


}