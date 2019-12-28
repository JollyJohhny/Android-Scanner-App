package com.example.scannerapp;
import android.net.Uri;

public class User {
    private String FullName,Email,Gender,ima;
    public User(){

    }

    public User(String fullName, String email, String gender, String ima) {
        FullName = fullName;
        Email = email;
        Gender = gender;
        this.ima = ima;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getIma() {
        return ima;
    }

    public void setIma(String ima) {
        this.ima = ima;
    }
}
