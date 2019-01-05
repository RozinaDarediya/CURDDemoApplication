package com.theta.curddemoapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.theta.curddemoapplication.Common.AppConstants;
import com.theta.curddemoapplication.Common.BaseActivity;
import com.theta.curddemoapplication.Global.GlobalClass;
import com.theta.curddemoapplication.R;
import com.theta.curddemoapplication.database.DatabaseHelper;
import com.theta.curddemoapplication.model.SignUpModel;

public class SignUpActivity extends BaseActivity implements View.OnClickListener {

    private EditText etUserName;
    private RadioGroup rgGender;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Button btnSignup;
    private TextView txtPasswordMismatch;
    private RadioButton rbGender;

    private Intent intent;

    // Database Helper
    DatabaseHelper db;

    private String password = "";
    private String confirmPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initView();
    }

    private void initView() {
        db = new DatabaseHelper(this);

        etUserName = findViewById(R.id.etUserName);
        rgGender = findViewById(R.id.rgGender);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignup = findViewById(R.id.btnSignup);
        txtPasswordMismatch = findViewById(R.id.txtPasswordMismatch);

        btnSignup.setOnClickListener(this);
        etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                password = etPassword.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                confirmPassword = etConfirmPassword.getText().toString();
                if (!confirmPassword.equals(password)) {
                    txtPasswordMismatch.setVisibility(View.VISIBLE);
                } else {
                    txtPasswordMismatch.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSignup) {
            int selectedId = rgGender.getCheckedRadioButtonId();
            rbGender = findViewById(selectedId);

            boolean cancle = validateData();
            if (cancle) {
                if (!confirmPassword.equals(password)) {
                    txtPasswordMismatch.setVisibility(View.VISIBLE);
                } else {
                    txtPasswordMismatch.setVisibility(View.GONE);
                    storeData();
                }
            }
        }
    }

    // store data in database and manage preference for login process
    // TODO : NOTE don't forget to manage preference for logout
    private void storeData() {
        SignUpModel signUpModel = new SignUpModel(etUserName.getText().toString().trim(), etEmail.getText().toString().trim(), etPassword.getText().toString(), rbGender.getText().toString());
        long record = db.insertSignupData(signUpModel);
        if (record > 0) {
            intent = new Intent(SignUpActivity.this, HomeActivity.class);
            // this flag is set because we don't want to come bace to this activity while back press.
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            GlobalClass.storePreference(AppConstants.PREF_KEY_IS_LOGGED_IN, true);
        } else {
            Snackbar.make(etEmail, "Something went wrong you are not registered", Snackbar.LENGTH_LONG).show();
        }
    }

    // vlaidation to check none of the field is empty
    private boolean validateData() {
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(etUserName.getText().toString().trim())) {
            etUserName.setError(getString(R.string.required_field));
            focusView = etUserName;
            cancel = true;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString().trim()).matches()) {
            etEmail.setError(getString(R.string.email_field_required));
            focusView = etEmail;
            cancel = true;
        }

        if (TextUtils.isEmpty(etPassword.getText().toString().trim()) && etPassword.getText().toString().trim().length() < 8) {
            etPassword.setError(getString(R.string.required_field) + " and " + getString(R.string.strPasswordLength));
            focusView = etPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(etConfirmPassword.getText().toString().trim()) && etConfirmPassword.getText().toString().trim().length() < 8) {
            etConfirmPassword.setError(getString(R.string.required_field) + " and " + getString(R.string.strPasswordLength));
            focusView = etConfirmPassword;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
            return false;
        }

        return true;
    }
}
