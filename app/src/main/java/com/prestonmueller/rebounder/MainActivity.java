package com.prestonmueller.rebounder;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
        
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("rebounderPrefs", Context.MODE_PRIVATE);

        boolean previouslyStarted = sharedPreferences.getBoolean(getString(R.string.previouslyStartedflag), false);
        if(!previouslyStarted) {

            SharedPreferences.Editor edit = sharedPreferences.edit();

            edit.putBoolean(getString(R.string.previouslyStartedflag), true);
            edit.putBoolean("module_enabled_Battery", true);
            edit.putBoolean("module_enabled_GPSLocation", false);
            edit.putBoolean("module_enabled_LastSeen", false);

            edit.commit();

            Intent intent = new Intent(this, FirstRunActivity.class);
            startActivity(intent);
        }

        ArrayList<Module> modules = new ArrayList<Module>();
        modules.add(new ModuleLocateWPI());
        modules.add(new ModuleLastSeen());
        modules.add(new ModuleBattery());
        modules.add(new ModuleLocate());
        
        LinearLayout cardList = (LinearLayout)findViewById(R.id.cardList);
        for(Module m : modules) {
        	
        	final String moduleName = m.name();
            final String moduleTrigger = sharedPreferences.getString("module_triggerCode_" + m.name(), m.triggerString());

        	boolean enabled = sharedPreferences.getBoolean("module_enabled_" + m.name(), false);
            Log.d("Rebounder", "Module + " + m.name() + " is" + enabled + " (module" + m.name() + ")");

        	LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        	RelativeLayout newCard = (RelativeLayout) inflater.inflate(R.layout.modulecard, cardList, false);

        	final TextView name = (TextView)newCard.findViewById(R.id.moduleCardName);
        	name.setText(String.format("%s (%s)", m.humanReadableName(), moduleTrigger));

        	TextView description = (TextView)newCard.findViewById(R.id.moduleCardDescription);
        	description.setText(m.description());

            final Button editButton = (Button)newCard.findViewById(R.id.editButton);

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final EditText modifyField = new EditText(MainActivity.this);

                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("rebounderPrefs", Context.MODE_PRIVATE);

                    modifyField.setText(sharedPreferences.getString("module_triggerCode_" + moduleName, moduleTrigger));

                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Modify trigger code")
                            .setMessage("(case sensitive)")
                            .setView(modifyField)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String newCode = modifyField.getText().toString();
                                    if(newCode == null || newCode.equals(""))newCode = moduleTrigger;

                                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("rebounderPrefs", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor edit = sharedPreferences.edit();
                                    edit.putString("module_triggerCode_" + moduleName, newCode);
                                    edit.commit();

                                    name.setText(moduleName + " (" + newCode + ")");
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

                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("rebounderPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sharedPreferences.edit();
					edit.putBoolean("module_enabled_" + moduleName, isChecked);
                    edit.commit();

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
