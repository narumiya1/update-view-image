package com.example.uploadandviewimage.history;

import com.example.uploadandviewimage.UploadApis;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyClient {
    private static final String BASE_URL="127.0.0.1/grain/conn.php/";
    private static MyClient myClient;
    private Retrofit retrofit;

    private MyClient(){
        retrofit=new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }
    public static synchronized MyClient getInstance(){
        if (myClient==null){
            myClient=new MyClient();
        }
        return myClient;
    }
    public UploadApis getMyApi(){
        return retrofit.create(UploadApis.class);
    }
}
