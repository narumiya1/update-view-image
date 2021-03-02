package com.example.uploadandviewimage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uploadandviewimage.activity.FragmentActivity;
import com.example.uploadandviewimage.auth.LoginActivity;
import com.example.uploadandviewimage.auth.LoginNumber;
import com.example.uploadandviewimage.auth.Preference;
import com.example.uploadandviewimage.auth.ProfileActivity;
import com.example.uploadandviewimage.auth.Sesion;

import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {
    private static final int TIME = 3000;
    Sesion session;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        session = new Sesion(this);
        showProgress();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openMain();
            }
        }, TIME);
    }

    void openMain() {
        if (session.isLoggedIn()) {
            Intent intent = new Intent(SplashActivity.this, FragmentActivity.class);
            startActivity(intent);
        }else {
            Intent i = null;

            i = new Intent(this, LoginNumber.class);

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
            finish();
            closeProgress();

    }
    private void showProgress() {

        progressDialog.setMessage("Loading . . .");
        progressDialog.show();

    }

    private void closeProgress() {
        progressDialog.dismiss();
    }

}