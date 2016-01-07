package com.prestonmueller.rebounder;

import android.content.Context;

public interface Module {
	
	public String name();
    public String humanReadableName();

	public String version();
	public String author();
	public String description();

	public String triggerString();
	public boolean runCheck(String message, Context c);
	
	public void commence(String sender, String message, Context c, RebounderReceiver caller);
	
}
