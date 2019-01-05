package com.theta.curddemoapplication.Global;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.theta.curddemoapplication.R;

/**
 * Created by Rozina on 04/01/19.
 */
public class AppDialogs {

    public static void showAppSettingDialog(Context context, String title, String msg,
                                            DialogInterface.OnClickListener positiveClick,
                                            DialogInterface.OnClickListener negativeClick) {
        AlertDialog alertDialog = null;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        // set title
        alertDialogBuilder.setTitle(title);
        // set dialog message
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(context.getText(R.string.txt_countinue), positiveClick)
                .setNegativeButton(context.getText(R.string.txt_not_now), negativeClick);

        // create alert dialog
        alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        // show it
        alertDialog.show();
    }
}
