package com.theta.curddemoapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.theta.curddemoapplication.Global.Log;
import com.theta.curddemoapplication.model.EnquiryDataModel;
import com.theta.curddemoapplication.model.SignUpModel;

import java.util.ArrayList;

/**
 * Created by Rozina on 04/01/19.
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "studentDatabse";

    // Table Names
    private static final String TABLE_SIGNUP = "signupTable";
    private static final String TABLE_ENQUIRY = "enquiryTable";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    // SIGNUP Table - column names
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_PASSWORD = "password";

    //ENQUIRY Table - column names
    private static final String KEY_EN_NAME = "name";
    private static final String KEY_EN_EDU = "education";
    private static final String KEY_EN_DOB = "dob";
    private static final String KEY_EN_EMAIL = "email";
    private static final String KEY_EN_CONTACT = "contact";
    private static final String KEY_EN_MSG = "message";
    private static final String KEY_EN_IMAGE = "image";


    // Table Create Statements
    // Todo table create statement
    private static final String CREATE_TABLE_SIGNUP = "CREATE TABLE "
            + TABLE_SIGNUP + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_NAME + " TEXT,"
            + KEY_EMAIL + " TEXT,"
            + KEY_GENDER + " TEXT,"
            + KEY_PASSWORD + " TEXT"
            + ")";

    private static final String CREATE_TABLE_ENQUIRY = "CREATE TABLE "
            + TABLE_ENQUIRY + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_EN_NAME + " TEXT,"
            + KEY_EN_EDU + " TEXT,"
            + KEY_EN_DOB + " TEXT,"
            + KEY_EN_EMAIL + " TEXT,"
            + KEY_EN_CONTACT + " TEXT,"
            + KEY_EN_MSG + " TEXT,"
            + KEY_EN_IMAGE + " TEXT"
            + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // creating required tables
        sqLiteDatabase.execSQL(CREATE_TABLE_SIGNUP);
        sqLiteDatabase.execSQL(CREATE_TABLE_ENQUIRY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        // on upgrade drop older tables
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_SIGNUP);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_ENQUIRY);

        // create new tables
        onCreate(sqLiteDatabase);
    }

    public long insertSignupData(SignUpModel signUpModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, signUpModel.getUserName());
        values.put(KEY_EMAIL, signUpModel.getEmail());
        values.put(KEY_GENDER, signUpModel.getGender());
        values.put(KEY_PASSWORD, signUpModel.getPassword());

        // insert row
        long todo_id = db.insert(TABLE_SIGNUP, null, values);
        db.close();

        return todo_id;
    }

    public boolean userSignup(String strEmail, String strPassword) {

        boolean match = false;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor2 = db.rawQuery("select * from " + TABLE_SIGNUP, null);
        Log.e("cursor2.getCount() : " + String.valueOf(cursor2.getCount()));

        if (cursor2 != null && cursor2.getCount() != 0) {
            if (cursor2.moveToFirst()) {
                do {
                    //String name = cursor2.getString(cursor2.getColumnIndex(KEY_NAME));
                    String email = cursor2.getString(cursor2.getColumnIndex(KEY_EMAIL));
                    //String gender = cursor2.getString(cursor2.getColumnIndex(KEY_GENDER));
                    String password = cursor2.getString(cursor2.getColumnIndex(KEY_PASSWORD));

                    if (email.equals(strEmail) && password.equals(strPassword)) {
                        match = true;
                    } else {
                        match = false;
                    }
                } while (cursor2.moveToNext());
            } else {
                Log.e("no rows returned");
            }
        } else {
            Log.e("debug Cursor cannot be created");
        }

        cursor2.close();
        db.close();

        return match;
    }


    public long insertEnquiryData(EnquiryDataModel enquiryDataModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EN_NAME, enquiryDataModel.getName());
        values.put(KEY_EN_EDU, enquiryDataModel.getEducation());
        values.put(KEY_EN_DOB, enquiryDataModel.getDob());
        values.put(KEY_EN_EMAIL, enquiryDataModel.getEmail());
        values.put(KEY_EN_CONTACT, enquiryDataModel.getContact());
        values.put(KEY_EN_MSG, enquiryDataModel.getMessage());
        values.put(KEY_EN_IMAGE, enquiryDataModel.getImage());

        // insert row
        long todo_id = db.insert(TABLE_ENQUIRY, null, values);
        db.close();

        return todo_id;
    }

    public int updateRecord(EnquiryDataModel enquiryDataModel) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EN_NAME, enquiryDataModel.getName());
        values.put(KEY_EN_EDU, enquiryDataModel.getEducation());
        values.put(KEY_EN_DOB, enquiryDataModel.getDob());
        values.put(KEY_EN_EMAIL, enquiryDataModel.getEmail());
        values.put(KEY_EN_CONTACT, enquiryDataModel.getContact());
        values.put(KEY_EN_MSG, enquiryDataModel.getMessage());
        values.put(KEY_EN_IMAGE, enquiryDataModel.getImage());

        // updating row
        return db.update(TABLE_ENQUIRY, values, KEY_ID + " = ?",
                new String[]{String.valueOf(enquiryDataModel.getID())});
    }

    /*
     * Deleting a todo
     */
    public boolean deleteRecord(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ENQUIRY, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});

        return true;
    }

    public ArrayList<EnquiryDataModel> getEnquiryData() {

        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<EnquiryDataModel> dataModels = new ArrayList<>();

        Cursor cursor2 = db.rawQuery("select * from " + TABLE_ENQUIRY, null);
        Log.e("cursor2.getCount() : " + String.valueOf(cursor2.getCount()));

        if (cursor2 != null && cursor2.getCount() != 0) {
            if (cursor2.moveToFirst()) {
                do {
                    int id = Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(KEY_ID)));
                    String name = cursor2.getString(cursor2.getColumnIndex(KEY_EN_NAME));
                    String edu = cursor2.getString(cursor2.getColumnIndex(KEY_EN_EDU));
                    String dob = cursor2.getString(cursor2.getColumnIndex(KEY_EN_DOB));
                    String email = cursor2.getString(cursor2.getColumnIndex(KEY_EN_EMAIL));
                    String contact = cursor2.getString(cursor2.getColumnIndex(KEY_EN_CONTACT));
                    String msg = cursor2.getString(cursor2.getColumnIndex(KEY_EN_MSG));
                    String img_path = cursor2.getString(cursor2.getColumnIndex(KEY_EN_IMAGE));

                    EnquiryDataModel model = new EnquiryDataModel(id,name, edu, dob, email, contact, msg, img_path);
                    dataModels.add(model);

                } while (cursor2.moveToNext());
            } else {
                Log.e("no rows returned");
            }
        } else {
            Log.e("debug Cursor cannot be created");
        }
        cursor2.close();
        db.close();
        return dataModels;
    }

}
