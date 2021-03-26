package com.example.uploadandviewimage.fragment

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.uploadandviewimage.R
import com.example.uploadandviewimage.SplashActivity
import com.example.uploadandviewimage.auth.LoginNumber

class OnBoarding : AppCompatActivity()  {
    lateinit var prefrences : Preff
    lateinit var btn_home:Button;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        prefrences = Preff(this)
//        prefrences.setValues("status","1")
//        if (prefrences.getValues("onboarding").equals("1")){
//            finishAffinity()
//
//            val intent = Intent(this, SplashActivity::class.java)
//            startActivity(intent)
//        }
//        btn_home=findViewById(R.id.btn_next)
//        btn_home.setOnClickListener {
//            prefrences.setValues("onboarding", "1")
//            finishAffinity()
//
//            val intent = Intent(this,
//                    SplashActivity::class.java)
//            startActivity(intent)
//        }

    }
}