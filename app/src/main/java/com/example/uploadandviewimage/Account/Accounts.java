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

    public Accounts(String id, String nama, String email, String alamat, String phoneNumbers) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.alamat = alamat;
        this.phoneNumbers = phoneNumbers;
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


}


