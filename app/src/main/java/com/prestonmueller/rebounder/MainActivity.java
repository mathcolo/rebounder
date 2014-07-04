package com.prestonmueller.rebounder;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) { 	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        boolean previouslyStarted = sharedPreferences.getBoolean(getString(R.string.previouslyStartedflag), false);
        if(!previouslyStarted){
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putBoolean(getString(R.string.previouslyStartedflag), true).apply();
            edit.putBoolean("module_enabled_Battery", true).apply();
            Intent intent = new Intent(this, FirstRunActivity.class);
            startActivity(intent);
        }

        ArrayList<Module> modules = new ArrayList<Module>();
        modules.add(new ModuleBattery());
        modules.add(new ModuleLocate());
        
        LinearLayout cardList = (LinearLayout)findViewById(R.id.cardList);
        for(Module m : modules) {
        	
        	final String moduleName = m.name();
        	
        	boolean enabled = sharedPreferences.getBoolean("module_enabled_" + m.name(), false);
        	
        	LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        	RelativeLayout newCard = (RelativeLayout) inflater.inflate(R.layout.modulecard, null);

        	TextView name = (TextView)newCard.findViewById(R.id.moduleCardName);
        	name.setText(m.name());

        	TextView description = (TextView)newCard.findViewById(R.id.moduleCardDescription);
        	description.setText(m.description());
        	
        	TextView trigger = (TextView)newCard.findViewById(R.id.moduleCardTrigger);
        	trigger.setText("#" + m.triggerString());
        	
        	final CheckBox enabledUI = (CheckBox)newCard.findViewById(R.id.enabled);
        	enabledUI.setChecked(enabled);
        	enabledUI.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					enabledUI.setChecked(isChecked);
					sharedPreferences.edit().putBoolean("module_enabled_" + moduleName, isChecked).apply();
				}
        	});

        	LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        	int dp12 = (int)(12 * MainActivity.this.getResources().getDisplayMetrics().density);
        	int dp10 = (int)(10 * MainActivity.this.getResources().getDisplayMetrics().density);
        	layoutParams.setMargins(dp12, dp10, dp12, 0);
        	cardList.addView(newCard, 2, layoutParams);
        	
        }
    }
}
