package com.theta.curddemoapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.theta.curddemoapplication.Common.AppConstants;
import com.theta.curddemoapplication.Common.BaseActivity;
import com.theta.curddemoapplication.Global.GlobalClass;
import com.theta.curddemoapplication.R;
import com.theta.curddemoapplication.database.DatabaseHelper;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private TextView btnSignup;
    private Intent intent;

    // Database Helper
    DatabaseHelper db;

    private boolean userRegisterd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        db = new DatabaseHelper(this);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);

        btnLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnLogin) {
            validateData();
        } else if (view.getId() == R.id.btnSignup) {
            intent = new Intent(LoginActivity.this, SignUpActivity.class);
            // this flag is set because we don't want to come bace to this activity while back press.
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    // check the field is not empty and the password should be atleast of 8 digits
    //TODO : NOTE don't forgot appply the 8 digit validation on sign up also
    private void validateData() {
        if (GlobalClass.isValidEmail(etEmail.getText().toString())) {
            Snackbar.make(etEmail, "Please enter valid email address", Snackbar.LENGTH_LONG).show();
        } else if (etPassword.getText().toString().length() < 8) {
            Snackbar.make(etEmail, "Password length should be at least 8 ", Snackbar.LENGTH_LONG).show();
        } else {
            if (GlobalClass.isFieldFilled(etEmail.getText().toString()) && GlobalClass.isFieldFilled(etPassword.getText().toString())) {
                userRegisterd = db.userSignup(etEmail.getText().toString(), etPassword.getText().toString());
                if (userRegisterd) {
                    intent = new Intent(LoginActivity.this, HomeActivity.class);
                    // this flag is set because we don't want to come bace to this activity while back press.
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    GlobalClass.storePreference(AppConstants.PREF_KEY_IS_LOGGED_IN, true);
                } else {
                    Snackbar.make(etEmail, "You are not registered", Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }
}
