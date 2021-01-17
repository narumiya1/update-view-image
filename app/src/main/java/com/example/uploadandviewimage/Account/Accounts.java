package com.example.uploadandviewimage.Account;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
@IgnoreExtraProperties
public class Accounts implements Serializable {
    private String id;
    private String nama;
    private String email;
    private String alamat;
    private String phoneNumbers;
    private String password;
    private String retypePassword;

    public Accounts(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public String getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(String phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public Accounts(String id, String nama, String email, String alamat, String phoneNumbers, String password, String retypePassword) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.alamat = alamat;
        this.phoneNumbers = phoneNumbers;
        this.password = password;
        this.retypePassword = retypePassword;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRetypePassword() {
        return retypePassword;
    }

    public void setRetypePassword(String retypePassword) {
        this.retypePassword = retypePassword;
    }
}


