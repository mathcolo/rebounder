package com.prestonmueller.rebounder;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ModuleLastSeen implements Module {
	
	public String triggerString() {
		return "#seen";
	}
	
	@Override
	public String name() {
		return "LastSeen";
	}

    @Override
    public String humanReadableName() {
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
		return "Returns the last time your phone was used.";
	}

	@Override
	public boolean runCheck(String message, Context c) {

        SharedPreferences sharedPreferences = c.getApplicationContext().getSharedPreferences("rebounderPrefs", Context.MODE_PRIVATE);
        String triggerCode = sharedPreferences.getString("module_triggerCode_" + name(), triggerString());

		if(message.contains(triggerCode)) return true;
		return false;
	}

	@Override
	public void commence(String sender, String message, Context c,
			RebounderReceiver caller) {
		
		Long time = c.getSharedPreferences("rebounderPrefs", Context.MODE_PRIVATE).getLong("lastSeen", 0);
        if(time == 0) {
            caller.sendResponse(sender, "Unable to determine when this phone was last used.", c);
            return;
        }

        Format df = new SimpleDateFormat("MMM dd hh:mm a z", Locale.US);
        Date date = new Date(time);

		caller.sendResponse(sender, "Last seen: " + df.format(date), c);
		
	}


}
