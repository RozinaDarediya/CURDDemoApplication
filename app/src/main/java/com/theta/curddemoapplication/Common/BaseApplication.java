package com.theta.curddemoapplication.Common;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Rozina on 04/01/19.
 *
 *  This ia application file which we have to register in manifest as below
 *      android:name=".Common.BaseApplication"
 *
 */
public class BaseApplication extends Application {

    // Application level preference variable declaration
    public static SharedPreferences sharedPref;

    @Override
    public void onCreate() {
        super.onCreate();

        // Application level preference variable initialization
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    }

}
