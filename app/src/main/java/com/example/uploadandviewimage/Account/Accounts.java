package com.example.uploadandviewimage.Account;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Accounts implements Serializable {

//    @SerializedName("Id")
//    private String Id;
//    @SerializedName("Username")
//    private String Username;
//    @SerializedName("Email")
//    private String Email;
//    @SerializedName("Address")
//    private String Address;
//    @SerializedName("Phone")
//    private String Phone;
//    @SerializedName("Password")
//    private String Password;
//
//    public Accounts(){
//
//    }
//
//    public Accounts(String Id, String Username, String Email, String Address, String Phone, String Password) {
//        this.Id = Id;
//        this.Username = Username;
//        this.Email = Email;
//        this.Address = Address;
//        this.Phone = Phone;
//        this.Password = Password;
//    }
//
//    public String getId() {
//        return Id;
//    }
//
//    public void setId(String Id) {
//        Id = Id;
//    }
//
//    public String getUsername() {
//        return Username;
//    }
//
//    public void setUsername(String Username) {
//        Username = Username;
//    }
//
//    public String getEmail() {
//        return Email;
//    }
//
//    public void setEmail(String Email) {
//        Email = Email;
//    }
//
//    public String getAddress() {
//        return Address;
//    }
//
//    public void setAddress(String Address) {
//        Address = Address;
//    }
//
//    public String getPhone() {
//        return Phone;
//    }
//
//    public void setPhone(String Phone) {
//        Phone = Phone;
//    }
//
//    public String getPassword() {
//        return Password;
//    }
//
//    public void setPassword(String Password) {
//        Password = Password;
//    }

    @SerializedName("id")
    private String id;
    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("address")
    private String address;
    @SerializedName("phone")
    private String phone;
    @SerializedName("password")
    private String password;
    public Accounts(){
    }
    public Accounts(String id, String username, String email, String address, String phone, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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


