package com.example.uploadandviewimage.auth;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.uploadandviewimage.ExampleAdapter;
import com.example.uploadandviewimage.GrainData;
import com.example.uploadandviewimage.GrainItem;
import com.example.uploadandviewimage.GrainPie;
import com.example.uploadandviewimage.MainActivity;
import com.example.uploadandviewimage.NetworkClient;
import com.example.uploadandviewimage.R;
import com.example.uploadandviewimage.SecondActivity;
import com.example.uploadandviewimage.UploadApis;
import com.example.uploadandviewimage.activity.FragmentActivity;
import com.example.uploadandviewimage.activity.PdfActivity;
import com.example.uploadandviewimage.cookies.AddCookiesInterceptor;
import com.example.uploadandviewimage.cookies.JavaNetCookieJar;
import com.example.uploadandviewimage.cookies.ReceivedCookiesInterceptor;
import com.example.uploadandviewimage.roomdbGhistory.GHistory;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginNumber extends AppCompatActivity {
    public static final String API_KEY = "PMAK-6010c29f1b0e6b0034f81b57-8f1c67506ef2eaee798cffec5d44f0496e";
    Button loginnumber;
    EditText numbr, passwrd;
    final Handler handler = new Handler(Looper.getMainLooper());
    private String mImageFileLocation = "";
    private ImageView imageView;
    private final static int ALL_PERMISSIONS_RESULT = 101;
    private static final int PERMISSION_CODE_READ_GALLERY = 1;
    private static final int PERMISSION_CODE_OPEN_CAMERA = 2;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList();
    private ArrayList<String> permissions = new ArrayList();
    PhotoView viewImage;
    ImageButton add_photo;
    private Uri image_uri;
    private String phoneInput, passwordInput;
    protected String jwt;
    TextView reg, forgot_password;
    ProgressDialog progressDialog;
    Sesion session;
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^" +
                            "(?=.*[+])"+     //at least 1 special character
                            ".{6,}"               //at least 4 characters

            );
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_number);
        numbr = findViewById(R.id.et_login_number);
        passwrd = findViewById(R.id.et_pw_number);
        viewImage = (PhotoView) findViewById(R.id.viewImageCb);
        add_photo = findViewById(R.id.ive_add);
        session = new Sesion(this);
        forgot_password = findViewById(R.id.forgot_password);
        progressDialog = new ProgressDialog(LoginNumber.this);
        openMain();
        reg = findViewById(R.id.textview_signup);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //auth here
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(LoginNumber.this, com.example.uploadandviewimage.auth.AuthActivity.class));
                finish();
            }
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(LoginNumber.this, com.example.uploadandviewimage.auth.AuthActivity.class));
                finish();
            }
        });

    }
    private boolean validatePhone(){
        String numberPhoneInput = numbr.getText().toString().trim();
        if (numberPhoneInput.isEmpty()) {
            numbr.setError("number phone can't be empty");
            return false;
        } else if (!PHONE_PATTERN.matcher(numberPhoneInput).matches()) {
            numbr.setError("nomor harus di awali +62 ");
            return false;
        } else {
            numbr.setError(null);
            return true;
        }
    }
    public void buttonLogin(View v){
        if (!validatePhone()){
            return;
        }
        String urlDomain = "http://110.50.85.28:8200";
        JavaNetCookieJar jncj = new JavaNetCookieJar(CookieHandler.getDefault());
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //.cookieJar(jncj)
                .addInterceptor(new AddCookiesInterceptor(getApplicationContext()))
                .addInterceptor(new ReceivedCookiesInterceptor(getApplicationContext()))
                .addInterceptor(loggingInterceptor)
                .build();
        Gson gson = new GsonBuilder().serializeNulls().create();


        //Retrofit retrofits = NetworkClient.getRetrofit();
        Retrofit retrofits = new Retrofit.Builder()
                .baseUrl(urlDomain)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        String phone = "+628156055410";
        String password = "123456";
        Log.d("PHONE <->", "Response: "+passwordInput);
        phoneInput = numbr.getText().toString();
        passwordInput = passwrd.getText().toString();
//                String phone = new Sesion(getApplicationContext()).getPhone();
//                Log.d("String Phone", "String Phone : "+new Sesion(getApplicationContext()).getPhone());
//                String password =  new Sesion(getApplicationContext()).getPassword();
//                Log.d("String Password", "String Password : " +new Sesion(getApplicationContext()).getPassword());
        RequestBody reqq1 = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(phone));
        RequestBody reqq2 = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(password));

        UploadApis uploadApiss = retrofits.create(UploadApis.class);

        Call<ResponseBody> calls = uploadApiss.insertLogin(phoneInput, passwordInput);
        Log.d("Body PHONE", "Response: "+phoneInput);
        Log.d("Body passwrd", "Response: "+passwordInput);
        jwt = "";
        calls.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200){
                    closeProgress();
                    Log.d("Body API <>", "Response: "+response.body().toString());
                    session.setKeyApiJwt(jwt);
                    Log.d("Body Token 1 <>", "Response: "+jwt);
                    //add delay
                    //String jwt = "";
                    ResponseBody responseBody = response.body();
                    try {
                        byte[] myByte = responseBody.bytes();
                        jwt = new String(myByte, StandardCharsets.UTF_8);
                        jwt = jwt.substring(1, jwt.length()-1);
                        Log.d("Body Token 2 <>", "Response: "+jwt);
                        //String strResponse = responseBody.string();
                        //String coba = "disini";
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms
                            closeProgress();
                            File file = new File(mImageFileLocation);
                            int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
                            try {
                                //20210130
                                HttpLoggingInterceptor loggingInterceptor2 = new HttpLoggingInterceptor();
                                loggingInterceptor2.setLevel(HttpLoggingInterceptor.Level.BODY);

                                TokenInterceptor tokenInterceptor = new TokenInterceptor(jwt);

                                Log.d("Body Token <>", "Response: "+jwt);

                                session.setPhone(phoneInput);
                                session.setPassword(passwordInput);
                                session.setKeyApiJwt(jwt);
                                session.setIsLogin(true);
                                OkHttpClient okHttpClient2 = new OkHttpClient.Builder()
                                        .addInterceptor(new AddCookiesInterceptor(getApplicationContext()))
                                        .addInterceptor(new ReceivedCookiesInterceptor(getApplicationContext()))
                                        .addInterceptor(loggingInterceptor2)
                                        .addInterceptor(tokenInterceptor)
                                        .build();

                                Gson gson2 = new GsonBuilder().serializeNulls().create();

                                //Retrofit retrofit = NetworkClient.getRetrofit();
                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(urlDomain)
                                        .addConverterFactory(GsonConverterFactory.create(gson2))
                                        .client(okHttpClient2)
                                        .build();

                                Log.d("Body <>", "Response: "+response.body().toString());
                                Intent intent = new Intent(LoginNumber.this, ProfileActivity.class);
                                startActivity(intent);

                            } catch (Exception e) {
                                String errMessage = e.getMessage();
                            }

                        }

                    }, 1000);
                    Toast.makeText(LoginNumber.this, "LOGIN BERHASIL !", Toast.LENGTH_LONG).show();

                }else {

                    Toast.makeText(LoginNumber.this, "Nomor/Password tidak sesuai, silahkan cek kembali  !", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                String message = "";
                Log.d("Body -->>", "Error: ");
                Toast.makeText(LoginNumber.this, "Password salah !", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void openMain() {
        if (session.isLoggedIn()) {
            Intent intent = new Intent(LoginNumber.this, ProfileActivity.class);
            startActivity(intent);
        }
    }

    private void closeProgress() {
        progressDialog.dismiss();
    }



}
