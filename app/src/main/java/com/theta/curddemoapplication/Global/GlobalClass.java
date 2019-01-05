package com.theta.curddemoapplication.Global;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Patterns;

import com.theta.curddemoapplication.Common.AppConstants;
import com.theta.curddemoapplication.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static com.theta.curddemoapplication.Common.BaseApplication.sharedPref;

/**
 * Created by Rozina on 04/01/19.
 */
public class GlobalClass {

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";


    public static boolean isValidEmail(CharSequence target) {
        return (!Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isFieldFilled(String target) {
        return (!TextUtils.isEmpty(target));
    }

    // stores string value
    public static void storePreference(String key, String value) {
        SharedPreferences.Editor editor = sharedPref
                .edit();
        editor.putString(key, value);
        editor.commit();
    }

    // stores int value
    public static void storePreference(String key, int value) {
        SharedPreferences.Editor editor = sharedPref
                .edit();
        editor.putInt(key, value);
        editor.commit();
    }

    // stores boolean value
    public static void storePreference(String key, Boolean value) {
        SharedPreferences.Editor editor = sharedPref
                .edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    // stores float value
    public static void storePreference(String key, Float value) {
        SharedPreferences.Editor editor = sharedPref
                .edit();
        editor.putFloat(key, value);
        editor.commit();
    }


    // stores array value
    public static HashMap<String, String> getPreference(String[] keys) {
        HashMap<String, String> parameters = new HashMap<String, String>();
        for (String key : keys) {
            parameters.put(key,
                    sharedPref.getString(key, null));
        }
        return parameters;
    }

    // get string value from SharedPreference
    public static String getPreference(String key, String defValue) {
        return sharedPref.getString(key, defValue);
    }

    // get string int from SharedPreference
    public static int getPreference(String key, Integer defValue) {
        return sharedPref.getInt(key, defValue);
    }

    // get boolean value from SharedPreference
    public static Boolean getPreference(String key, Boolean defValue) {
        return sharedPref.getBoolean(key, defValue);
    }

    // get boolean value from SharedPreference
    public static float getPreferenceFloat(String key, float defValue) {
        return sharedPref.getFloat(key, defValue);
    }

    // remove whole preference
    public static void removeAllPreferences() {

        //Global.removePreferences(new String[]{Constants.IS_LOGGED_IN});
    }

    // remove string preference
    public static void removePreference(String key) {
        SharedPreferences.Editor editor = sharedPref
                .edit();
        editor.remove(key);
        editor.commit();
    }

    // remove array preference
    public static void removePreferences(String keys[]) {
        SharedPreferences.Editor editor = sharedPref
                .edit();
        for (String key : keys) {
            editor.remove(key);
        }
        editor.commit();
    }

    // clears the prefernce
    public static void clearPreferences() {
        SharedPreferences.Editor editor = sharedPref
                .edit();
        editor.clear();
        editor.commit();
    }

    //getUriFromFile
    public static Uri getUriFromFile(Context context, String mCurrentPhotoPath) {
        return FileProvider.getUriForFile(context,
                context.getApplicationContext().getPackageName() +
                        ".provider", new File(mCurrentPhotoPath));
    }

    //createImageFile
    public static File createImageFile(Context context) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";

        File instanceRecordDirectory = new File(Environment.getExternalStorageDirectory() + File.separator + context.getString(R.string.app_name) + File.separator + AppConstants.imageFolderName);

        if (!instanceRecordDirectory.exists()) {
            try {
                instanceRecordDirectory.mkdirs();
            } catch (Exception e) {
                Log.e(e.toString());
            }
        }
        File instanceRecord = new File(instanceRecordDirectory.getAbsolutePath() + File.separator + imageFileName + JPEG_FILE_SUFFIX);
        if (!instanceRecord.exists()) {
            try {
                instanceRecord.createNewFile();
            } catch (IOException e) {
                Log.e(e.toString());
            }
        }
        return instanceRecord;
    }


}
