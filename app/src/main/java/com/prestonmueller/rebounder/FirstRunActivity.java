package com.prestonmueller.rebounder;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class FirstRunActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (checkPlayServices()) {
            setContentView(R.layout.activity_first_run);
            Button b = (Button) findViewById(R.id.getgoing);
            b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        }

    }

    private boolean checkPlayServices() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
                GooglePlayServicesUtil.getErrorDialog(status, this, 10).show();
            } else {
                Toast.makeText(this, "Unfortunately, Google Play services weren't available and the issue couldn't be automatically resolved.", Toast.LENGTH_LONG).show();
                finish();
                System.exit(0);
            }
            return false;
        }
        return true;
    }

}
