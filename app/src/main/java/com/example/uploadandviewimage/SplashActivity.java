package com.example.uploadandviewimage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uploadandviewimage.auth.LoginActivity;
import com.example.uploadandviewimage.auth.LoginNumber;
import com.example.uploadandviewimage.auth.Preference;
import com.example.uploadandviewimage.auth.ProfileActivity;
import com.example.uploadandviewimage.auth.Sesion;

import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {
    private static final int TIME = 3000;
    Sesion session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        session = new Sesion(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openMain();
            }
        }, TIME);
    }

    void openMain() {

            Preference.setLoggedInStatus(getApplicationContext(), false);
            Intent i = null;

          i = new Intent(this, LoginNumber.class);

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();


    }
}