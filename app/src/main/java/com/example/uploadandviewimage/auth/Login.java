package com.example.uploadandviewimage.auth;

import com.google.gson.annotations.SerializedName;

public class Login {
    @SerializedName("phone")
    private String phone;
    @SerializedName("password")
    private String password;

    public Login(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
