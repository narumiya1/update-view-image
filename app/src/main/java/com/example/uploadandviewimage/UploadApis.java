package com.example.uploadandviewimage;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadApis {
    @Multipart
    @POST("api/DocFile2")
    Call<GrainData> uploadImage(@Part MultipartBody.Part part, @Part("USER_ID") RequestBody requestBody1, @Part("LATITUDE") RequestBody requestBody2, @Part("LONGITUDE") RequestBody requestBody3);

    @FormUrlEncoded
    @POST("insertdata.php")
    Call<ResponseBody>insertdata(
            @Field("name") String name,
            @Field("email")int jumlah
    );
}