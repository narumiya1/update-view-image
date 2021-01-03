package com.example.uploadandviewimage.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
@IgnoreExtraProperties
public class Accounts implements Serializable {

    private String nama;
    private String email;
    private String alamat;

    public Accounts(){

    }

    public String getNama() {
        return nama;
    }

    public Accounts(String nama, String email, String alamat) {
        this.nama = nama;
        this.email = email;
        this.alamat = alamat;
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


