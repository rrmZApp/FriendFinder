package com.rrmsense.friendfinder.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rrmsense.friendfinder.R;
import com.rrmsense.friendfinder.service.NetworkStateReceiver;

public class SplashScreenActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {

    private static int SPLASH_TIME_OUT = 3000;
    private NetworkStateReceiver networkStateReceiver;
    RelativeLayout relativeLayout;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_splash_screen);

        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    @Override
    public void networkAvailable() {
        final Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
        if (getIntent().hasExtra("Fragment")) {
            int value = getIntent().getExtras().getInt("Fragment");
            i.putExtra("Fragment",value);
            SPLASH_TIME_OUT = 0;
        }

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                progressDialog.cancel();

                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);

    }

    @Override
    public void networkUnavailable() {

        //Toast.makeText(this,"No internet connection!",Toast.LENGTH_SHORT).show();
        Snackbar snackbar = Snackbar.make(relativeLayout, "No internet connection!", Snackbar.LENGTH_LONG);
        snackbar.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(networkStateReceiver);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }
}
