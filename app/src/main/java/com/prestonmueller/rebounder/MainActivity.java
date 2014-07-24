package com.prestonmueller.rebounder;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
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
            final String moduleTrigger = m.triggerString();
        	
        	boolean enabled = sharedPreferences.getBoolean("module_enabled_" + m.name(), false);

        	LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        	RelativeLayout newCard = (RelativeLayout) inflater.inflate(R.layout.modulecard, null);

        	TextView name = (TextView)newCard.findViewById(R.id.moduleCardName);
        	name.setText(m.name());

        	TextView description = (TextView)newCard.findViewById(R.id.moduleCardDescription);
        	description.setText(m.description());

            String currentTrigger = sharedPreferences.getString("module_triggerCode_" + m.name(), m.triggerString());
        	final TextView trigger = (TextView)newCard.findViewById(R.id.moduleCardTrigger);
            setTextViewUnderlineText(trigger, currentTrigger);

            trigger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final EditText modifyField = new EditText(MainActivity.this);

                    modifyField.setText(sharedPreferences.getString("module_triggerCode_" + moduleName, moduleTrigger));

                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Modify trigger code")
                            .setMessage("(case sensitive)")
                            .setView(modifyField)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String newCode = modifyField.getText().toString();
                                    if(newCode == null || newCode == "")newCode = moduleTrigger;

                                    sharedPreferences.edit().putString("module_triggerCode_" + moduleName, newCode).commit();
                                    setTextViewUnderlineText(trigger, newCode);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                }
                            })
                            .show();
                }
            });
        	
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

    private void setTextViewUnderlineText(TextView t, String s) {
        String truncated = s.substring(0, Math.min(s.length(), 10));
        if(!truncated.equals(s))truncated += "...";

        //final SpannableString ss = new SpannableString(truncated);
        //ss.setSpan(new UnderlineSpan(), 0, ss.length(), 0);
        t.setText(truncated);
    }
}
