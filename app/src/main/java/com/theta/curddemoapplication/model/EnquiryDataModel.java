package com.theta.curddemoapplication.model;

import java.io.Serializable;

/**
 * Created by Rozina on 04/01/19.
 */
public class EnquiryDataModel implements Serializable {

    int ID;
    String name;
    String education;
    String dob;
    String email;
    String contact;
    String message;
    String image;

    public EnquiryDataModel(String name, String education, String dob, String email, String contact, String message, String image) {
        this.name = name;
        this.education = education;
        this.dob = dob;
        this.email = email;
        this.contact = contact;
        this.message = message;
        this.image = image;
    }

    public EnquiryDataModel(int id, String name, String education, String dob, String email, String contact, String message, String image) {
        this.ID = id;
        this.name = name;
        this.education = education;
        this.dob = dob;
        this.email = email;
        this.contact = contact;
        this.message = message;
        this.image = image;
    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
