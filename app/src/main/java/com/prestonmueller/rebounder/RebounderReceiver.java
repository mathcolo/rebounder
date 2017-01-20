package com.prestonmueller.rebounder;

import java.util.ArrayList;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class RebounderReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Bundle bundle = intent.getExtras();

		Object[] messages = (Object[]) bundle.get("pdus");
		SmsMessage[] sms = new SmsMessage[messages.length];

		String content = "N/A";
		String sender = null;
		
		for(int n = 0; n < messages.length; n++) {
			sms[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
			sender = sms[n].getOriginatingAddress();
			content = sms[n].getMessageBody();
		}

		ArrayList<Module> modules = new ArrayList<Module>();
        //modules.add(new ModuleLocateCampus());
		modules.add(new ModuleETA());
        modules.add(new ModuleLastSeen());
		modules.add(new ModuleLoudRinger());
		modules.add(new ModuleBattery()); //TODO: Test modules
		modules.add(new ModuleLocate());
		
		SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("rebounderPrefs", Context.MODE_PRIVATE);
		
		for(Module m : modules) {
			if(sharedPreferences.getBoolean("module_enabled_" + m.name(), false) && m.runCheck(content, context)) {
				m.commence(sender, content, context, this);
				break;
			}
		}
		
	}
	
	public void sendResponse(String sender, String message, Context c) {
		
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(sender, null, message, null, null);
		
		ContentValues sendMessageValues = new ContentValues();
        sendMessageValues.put("address", sender);
        sendMessageValues.put("date", new Date().getTime());
        sendMessageValues.put("read", Integer.valueOf(1));
        sendMessageValues.put("subject", "");
        sendMessageValues.put("body", message);
        sendMessageValues.put("status", Integer.valueOf(-1));
        sendMessageValues.put("type", Integer.valueOf(2));
        c.getContentResolver().insert(Uri.parse("content://sms/outbox"), sendMessageValues);
        
	}

}