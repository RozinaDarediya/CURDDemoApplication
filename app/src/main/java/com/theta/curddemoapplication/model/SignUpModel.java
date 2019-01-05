package com.theta.curddemoapplication.model;

/**
 * Created by Rozina on 04/01/19.
 */
public class SignUpModel {

    String userName;
    String email;
    String password;
    String gender;

    public SignUpModel(String userName, String email, String password, String gender) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.gender = gender;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
