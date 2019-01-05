package com.theta.curddemoapplication.activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.theta.curddemoapplication.Common.BaseActivity;
import com.theta.curddemoapplication.Global.AppDialogs;
import com.theta.curddemoapplication.Global.GlobalClass;
import com.theta.curddemoapplication.Global.Log;
import com.theta.curddemoapplication.R;
import com.theta.curddemoapplication.database.DatabaseHelper;
import com.theta.curddemoapplication.model.EnquiryDataModel;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.theta.curddemoapplication.Global.GlobalClass.createImageFile;
import static com.theta.curddemoapplication.utils.DateUtils.parseAndGetDateData;
import static com.theta.curddemoapplication.utils.ImageUtils.getGalleryImagePath;

public class EnquiryFormActivity extends BaseActivity implements View.OnClickListener {

    ImageView image;
    EditText etFormName;
    Spinner spinner;
    TextView txtDate;
    EditText etEmail;
    EditText etContact;
    EditText etMsg;
    Button btnSave;
    Button btnDelete;

    private ArrayAdapter<String> arrayAdapter;
    private String[] eduArr;
    private String selectedEdu = "";
    private Calendar dateSelected = Calendar.getInstance();
    private EnquiryDataModel enquiryDataModel;
    private boolean toUpdate = false;

    // Database Helper
    DatabaseHelper db;

    public int REQUEST_READ_EXTERNAL_PERMISSION = 11, REQUEST_CAMERA_PERMISSION = 10, REQUEST_CAMERA_PHOTO = 1, REQUEST_GALLERY_PHOTO = 2;
    private int REQUEST;
    public String userChoosenTask = "";
    private String mCurrentPhotoPath;
    private DialogInterface.OnClickListener positiveClick, negativeClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry_form);

        setToolBar();
        setTitle("Enquiry Form");

        initView();

        try {
            // if data is already filled show it here
            Intent intent = getIntent();
            if (intent.hasExtra("data")) {
                enquiryDataModel = (EnquiryDataModel) intent.getSerializableExtra("data");
                etFormName.setText(enquiryDataModel.getName());
                txtDate.setText(enquiryDataModel.getDob());
                etEmail.setText(enquiryDataModel.getEmail());
                etContact.setText(enquiryDataModel.getContact());
                etMsg.setText(enquiryDataModel.getMessage());

                if (enquiryDataModel.getImage()!= null && enquiryDataModel.getImage().length() > 0)
                    Glide.with(this).load(enquiryDataModel.getImage()).into(image);

                spinner.setSelection(arrayAdapter.getPosition(enquiryDataModel.getEducation()));

                btnSave.setText("Update");
                btnDelete.setVisibility(View.VISIBLE);
                toUpdate = true;
            }
        } catch (Exception e) {
            Log.e(e.toString());
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                try {
                    selectedEdu = (eduArr[position]);
                    Log.e(" data : " + selectedEdu);
                } catch (Exception e) {
                    Log.e(e.toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // click of dialog
        positiveClick = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                if (REQUEST == REQUEST_READ_EXTERNAL_PERMISSION) {
                    startActivityForResult(intent, REQUEST_READ_EXTERNAL_PERMISSION);
                }
                if (REQUEST == REQUEST_CAMERA_PERMISSION) {
                    startActivityForResult(intent, REQUEST_CAMERA_PERMISSION);
                }

            }
        };

        negativeClick = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        };

    }

    private void initView() {
        db = new DatabaseHelper(this);

        image = findViewById(R.id.image);
        etFormName = findViewById(R.id.etFormName);
        spinner = findViewById(R.id.spinner);
        txtDate = findViewById(R.id.txtDate);
        etEmail = findViewById(R.id.etEmail);
        etContact = findViewById(R.id.etContact);
        etMsg = findViewById(R.id.etMsg);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);

        btnSave.setOnClickListener(this);
        txtDate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        image.setOnClickListener(this);

        eduArr = getResources().getStringArray(R.array.eduArray);
        arrayAdapter = new ArrayAdapter<String>(EnquiryFormActivity.this, R.layout.spinner_layout, eduArr);
        spinner.setAdapter(arrayAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.image) {
            // first get the required permission 
            // then show dialog to for select image
            getImage();
        } else if (view.getId() == R.id.btnSave) {
            // apply validation and on true store it in database
            boolean cancle = validateData();
            if (cancle)
                saveData();
        } else if (view.getId() == R.id.txtDate) {
            showDatePickerDialog();
        } else if (view.getId() == R.id.btnDelete) {
            deleteRecord();
        }
    }

    private void deleteRecord() {
        boolean flag = db.deleteRecord(enquiryDataModel.getID());
        finish();
    }

    private void saveData() {
        long record = 0;
        if (!toUpdate) {
            record = db.insertEnquiryData(new EnquiryDataModel(etFormName.getText().toString().trim(),
                    selectedEdu,
                    txtDate.getText().toString().trim(),
                    etEmail.getText().toString().trim(),
                    etContact.getText().toString().trim(),
                    etMsg.getText().toString().trim(),
                    mCurrentPhotoPath));
        } else {
            record = db.updateRecord(new EnquiryDataModel(etFormName.getText().toString().trim(),
                    selectedEdu,
                    txtDate.getText().toString().trim(),
                    etEmail.getText().toString().trim(),
                    etContact.getText().toString().trim(),
                    etMsg.getText().toString().trim(),
                    mCurrentPhotoPath));
        }
        if (record > 0) {
            Snackbar.make(etEmail, "Enquiry data inserted successfully.", Snackbar.LENGTH_LONG).show();
            finish();
        } else {
            Snackbar.make(etEmail, "Something went wrong Enquiry data failed to insert.", Snackbar.LENGTH_LONG).show();
            finish();
        }
    }

    // function to validate data
    private boolean validateData() {
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(etFormName.getText().toString().trim())) {
            etFormName.setError(getString(R.string.required_field));
            focusView = etFormName;
            cancel = true;
        }

        if (TextUtils.isEmpty(txtDate.getText().toString().trim())) {
            txtDate.setError(getString(R.string.required_field));
            focusView = txtDate;
            cancel = true;
        }

        if (TextUtils.isEmpty(etEmail.getText().toString().trim())) {
            etEmail.setError(getString(R.string.required_field));
            focusView = etEmail;
            cancel = true;
        }

        if (TextUtils.isEmpty(etContact.getText().toString().trim()) && etContact.getText().toString().trim().length() == 10) {
            etContact.setError(getString(R.string.required_field) + " " + getString(R.string.required_num));
            focusView = etContact;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
            return false;
        }

        return true;
    }

    private void showDatePickerDialog() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateSelected.set(year, monthOfYear, dayOfMonth);
                String date = parseAndGetDateData(dateSelected.getTime().getTime());
                txtDate.setText(date);
            }

        }, dateSelected.get(Calendar.YEAR), dateSelected.get(Calendar.MONTH), dateSelected.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    } //showDatePickerDialog

    // check permission if granted show dialog for selecting image
    private void getImage() {
        if (!checkStoragePermission()) {
            REQUEST = REQUEST_READ_EXTERNAL_PERMISSION;
            getReadExternalPermission();
        } else if (!checkCameraPermission()) {
            REQUEST = REQUEST_CAMERA_PERMISSION;
            getCameraPermission();
        } else {
            selectImage();
        }
    }

    private void selectImage() {
        final CharSequence[] items = getResources().getStringArray(R.array.arr_photo);
        AlertDialog.Builder builder = new AlertDialog.Builder(EnquiryFormActivity.this);
        builder.setTitle((getString(R.string.add_photo)));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.take_photo))) {
                    userChoosenTask = (getString(R.string.take_photo));
                    REQUEST = REQUEST_CAMERA_PERMISSION;
                    cameraIntent();
                } else if (items[item].equals((getString(R.string.choose_from_gallery)))) {
                    userChoosenTask = (getString(R.string.choose_from_gallery));
                    REQUEST = REQUEST_READ_EXTERNAL_PERMISSION;
                    //galleryIntent();
                    galleryIntent();
                } else if (items[item].equals((getString(R.string.cancel)))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    // camera intent to open the camera
    private void cameraIntent() {
        mCurrentPhotoPath = "";
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = null;
        try {
            f = setUpPhotoFile();
            mCurrentPhotoPath = f.getAbsolutePath();
            Log.e("current path : " + mCurrentPhotoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mCurrentPhotoPath != null) {
            Uri imageUri = GlobalClass.getUriFromFile(this, mCurrentPhotoPath);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, REQUEST_CAMERA_PHOTO);
        }
    }

    // create a file and get absolute path
    private File setUpPhotoFile() throws IOException {

        File f = createImageFile(this);
        mCurrentPhotoPath = f.getAbsolutePath();
        return f;
    }//setUpPhotoFile

    // function to open gallery
    private void galleryIntent() {
        File f = null;
        try {
            f = setUpPhotoFile();
            mCurrentPhotoPath = f.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GALLERY_PHOTO);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_GALLERY_PHOTO);
        }
    }//galleryIntent

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (ActivityCompat.checkSelfPermission(this, CAMERA) == PERMISSION_GRANTED) {
                Log.e("permisson granted");
                getImage();
            }
        } else if (requestCode == REQUEST_READ_EXTERNAL_PERMISSION) {
            if (ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED) {
                Log.e("permisson granted");
                getImage();
            }
        } else if (requestCode == REQUEST_GALLERY_PHOTO) {
            if (resultCode == RESULT_OK) // if user has chooes image than the result code will be RESULT_OK -1 else it will be RESULT_CANCELED 0
                onSelectFromGalleryResult(data, requestCode);
        } else if (requestCode == REQUEST_CAMERA_PHOTO) {
            if (resultCode == RESULT_OK)  // if user has clicked image than the result code will be RESULT_OK -1 else it will be RESULT_CANCELED 0
                onCaptureImageResult(data);
        }
    }//onActivityResult

    // handel the image selected from gallery
    private void onSelectFromGalleryResult(Intent data, int requestCode) {
        String path = "";
        if (data != null) {
            try {
                path = getGalleryImagePath(this, data);
                mCurrentPhotoPath = path;
                Glide.with(this).load(path).into(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//onSelectFromGalleryResult

    // handel image from camera
    private void onCaptureImageResult(Intent data) {
        try {
            Log.e("camera : " + mCurrentPhotoPath);
            Glide.with(this).load(mCurrentPhotoPath).into(image);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error : " + e.toString());
        }
    }//onCaptureImageResult


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_EXTERNAL_PERMISSION) {
            if (ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED) {
                Log.e("storage permisson granted");
                getImage();
            } else {
                Log.e("set to never ask again");
                AppDialogs.showAppSettingDialog(EnquiryFormActivity.this,
                        getString(R.string.txt_read_permission_title), getString(R.string.txt_read_permission),
                        positiveClick, negativeClick);
            }
        }//REQUEST_READ_EXTERNAL_PERMISSION
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (ActivityCompat.checkSelfPermission(this, CAMERA) == PERMISSION_GRANTED) {
                Log.e("camera permisson granted");
                getImage();
            } else {
                Log.e("set to never ask again");
                AppDialogs.showAppSettingDialog(EnquiryFormActivity.this,
                        getString(R.string.camera_permission_title), getString(R.string.camera_permission),
                        positiveClick, negativeClick);
            }
        }
    }//onRequestPermissionsResult

}
