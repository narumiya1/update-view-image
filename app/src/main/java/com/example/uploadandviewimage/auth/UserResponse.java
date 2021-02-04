package com.example.uploadandviewimage.auth;

import com.example.uploadandviewimage.Account.Accounts;
import com.google.gson.annotations.SerializedName;

public class UserResponse {
    private User data;

    @SerializedName("data")
    public User getData() {
        return data;
    }
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    public boolean isStatus(){
        return status;
    }

}
