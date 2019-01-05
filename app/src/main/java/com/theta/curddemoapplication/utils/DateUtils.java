package com.theta.curddemoapplication.utils;

import android.text.format.DateFormat;

import com.theta.curddemoapplication.Global.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ashish on 17/10/16.
 */

public class DateUtils {

    public static final String APP_DATE_FORMAT = "EEE, MMM dd";
    public static final String APP_TIME_FORMAT = "h:mm a";
    public static final String PROFILE_DATE_FORMAT = "dd-MM-yyyy";

    private static final SimpleDateFormat appDateFormat = new SimpleDateFormat(APP_DATE_FORMAT, Locale.getDefault());

    public static String parseAndFormatDate(String strDate, String dateFormat) {
        SimpleDateFormat dateFormatUser = new SimpleDateFormat(dateFormat, Locale.getDefault());
        try {
            Date date = appDateFormat.parse(strDate);
            return dateFormatUser.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String parseAndGetDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(time);
        String date = DateFormat.format(APP_DATE_FORMAT, cal).toString();

        return date;
    }

    public static String parseAndGetDateData(long time) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(time);
        String date = DateFormat.format(PROFILE_DATE_FORMAT, cal).toString();

        return date;
    }


    public static String parseAndGetTime(long time) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(time);
        String date = DateFormat.format(APP_TIME_FORMAT, cal).toString();

        return date;
    }

    public static String getUpdatedExpirationDate(String activationDate, Integer duration) {
        SimpleDateFormat formatter = new SimpleDateFormat(APP_DATE_FORMAT);
        Calendar c = Calendar.getInstance();
        String expiryDate = "";
        try {
            c.setTime(formatter.parse(activationDate));
            c.add(Calendar.DAY_OF_MONTH, duration);
            expiryDate = DateFormat.format(APP_DATE_FORMAT, c).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.v("Expiry Date : ", expiryDate);
        return expiryDate;
    }


    public static String getCurrentTimestamp() {
        Date today = new Date();
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(today.getTime());
        String date = DateFormat.format(APP_DATE_FORMAT, cal).toString();

        return date;
    }

    public static boolean isSystemDateChanged(Calendar startDate, Calendar endDate) {
        boolean b = false;
        try {
            if (startDate.before(endDate)) {
                b = false;//If start date is before end date
            } else if (startDate.equals(endDate)) {
                b = false;//If two dates are equal
            } else {
                b = true; //If start date is after the end date
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;
    }

    public static String addDays(int noOfDays) {
        String date = "";
        try {
            Date today = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(today.getTime());
            calendar.add(Calendar.DAY_OF_YEAR, noOfDays);
            date = DateFormat.format(APP_DATE_FORMAT, calendar).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
}
