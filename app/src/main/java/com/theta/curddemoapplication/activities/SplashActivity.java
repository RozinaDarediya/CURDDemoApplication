package com.theta.curddemoapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.theta.curddemoapplication.Common.AppConstants;
import com.theta.curddemoapplication.Global.GlobalClass;
import com.theta.curddemoapplication.Global.Log;
import com.theta.curddemoapplication.R;

public class SplashActivity extends AppCompatActivity {

    private Handler handler;
    private static final int SPLASH_TIME_MILLISEC = 2000;
    private Intent intent;
    private boolean isLogged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // check the preference if user is logged in
        // logged in than redirect to home activity
        // else redirect to login activity
        try {
            if (GlobalClass.getPreference(AppConstants.PREF_KEY_IS_LOGGED_IN, false) != null) {
                isLogged = GlobalClass.getPreference(AppConstants.PREF_KEY_IS_LOGGED_IN, false);
            }
        } catch (Exception e) {
            Log.e(e.toString());
        }

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isLogged) {
                    intent = new Intent(SplashActivity.this, HomeActivity.class);
                    // this flag is set because from login or sign up page we don't want to come bace to this splash activity.
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                    // this flag is set because from login or sign up page we don't want to come bace to this splash activity.
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

            }
        }, SPLASH_TIME_MILLISEC);
    }
}
