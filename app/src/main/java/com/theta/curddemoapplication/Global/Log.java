package com.theta.curddemoapplication.Global;

/**
 * Created by ashish on 13/10/17.
 */

public class Log {

    public static boolean DEBUG = true;
    public static String TAG = "CURD Operation Demo";

    public static void e(String msg) {
        if (DEBUG)
            android.util.Log.e(TAG, msg);
    }

    public static void v(String msg) {
        if (DEBUG)
            android.util.Log.v(TAG, msg);
    }

    public static void v(Exception e) {
        if (DEBUG)
            e.printStackTrace();
    }

    public static void v(String... args) {
        if (DEBUG) {
            StringBuffer strBuffer = new StringBuffer();
            for (String temp : args) {
                strBuffer.append(temp).append(" ");
            }
            android.util.Log.v(TAG, strBuffer.toString());
        }
    }
}
