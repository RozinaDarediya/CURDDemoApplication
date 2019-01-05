package com.theta.curddemoapplication.Common;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.theta.curddemoapplication.R;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

/**
 * Created by Rozina on 04/01/19.
 * <p>
 * This is base activity for our project and all activity will extend this base activity
 * So we can write here functionality which can directly use by all the activities
 * like toolbar, showing progress dialog
 */
public class BaseActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView title;
    ImageView ivLeft;
    ImageView ivRight;
    public int REQUEST_READ_EXTERNAL_PERMISSION = 11, REQUEST_CAMERA_PERMISSION = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    //setting toolbar
    public Toolbar setToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = findViewById(R.id.title);
        ivLeft = findViewById(R.id.ivLeft);
        ivRight = findViewById(R.id.ivRight);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        return toolbar;
    }

    public void setTitle(String strSitle) {
        title.setVisibility(View.VISIBLE);
        title.setText(strSitle);
    }

    public ImageView setLeftMenu() {
        ivLeft.setVisibility(View.VISIBLE);
        return ivLeft;
    }

    public ImageView setRightMenu() {
        ivRight.setVisibility(View.VISIBLE);
        return ivRight;
    }

    public boolean checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }
    public boolean checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    public void getReadExternalPermission() {
        if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_PERMISSION);
        }
    }// getReadExternalPermission
    public void getCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }// getCameraPermission

}
