package com.prestonmueller.rebounder;

import android.content.Context;

public interface Module {
	
	public String name();
	public String version();
	public String author();
	public String description();

	public String triggerString();
	public boolean runCheck(String message, String prefix);
	
	public void commence(String sender, String message, Context c, RebounderReceiver caller);
	
}
