package com.example.uploadandviewimage.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.uploadandviewimage.R;
import com.example.uploadandviewimage.auth.LoginNumber;
import com.example.uploadandviewimage.auth.Sesion;
import com.example.uploadandviewimage.fragment.AccountFragment;
import com.example.uploadandviewimage.fragment.AboutFragment;
import com.example.uploadandviewimage.fragment.HistoryFragment;
import com.example.uploadandviewimage.fragment.HomeFragment;
import com.example.uploadandviewimage.fragment.MeinenDialogFragment;
import com.example.uploadandviewimage.utils.DialogUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FragmentActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener, MeinenDialogFragment.DialogListener {

    Sesion sesion;
    private CountDownTimer countDownTimer;
    private TextView textViewTime;

    private enum TimerStatus {
        STARTED,
        STOPPED
    }

    private FragmentActivity.TimerStatus timerStatus = FragmentActivity.TimerStatus.STOPPED;
    private long timeCountInMilliSeconds = 1 * 60000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragmnet);
        sesion = new Sesion(this);
        if (!sesion.isLoggedIn()) {


            String jwtNull = "";
            Intent intent = new Intent(FragmentActivity.this, LoginNumber.class);
            ;
            sesion.setKeyApiJwt(jwtNull);
            sesion.setIsLogin(false);
            sesion.logoutUser();
            startActivity(intent);

        }
        loadFragment(new HomeFragment());

        BottomNavigationView bottomNavigationView = findViewById(R.id.btn_main);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        textViewTime = findViewById(R.id.textViewCountTime);
        startStops();
    }

    private void startStops() {
        if (timerStatus == FragmentActivity.TimerStatus.STOPPED) {
            // call to initialize the timer values
            setTimerValues();
            // changing the timer status to started
            timerStatus = FragmentActivity.TimerStatus.STARTED;
            // call to start the count down timer
            startCountDownTimer();
        } else {
            // changing the timer status to stopped
            timerStatus = FragmentActivity.TimerStatus.STOPPED;
            stopCountDownTimer();

        }
    }

    private void stopCountDownTimer() {
        countDownTimer.cancel();

    }

    private void startCountDownTimer() {
        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));


            }

            @Override
            public void onFinish() {

                textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));

                // changing the timer status to stopped
                timerStatus = FragmentActivity.TimerStatus.STOPPED;
                String jwtNull = "";
                showDialogs();
                sesion.setKeyApiJwt(jwtNull);
                sesion.setIsLogin(false);
                sesion.logoutUser();

                Log.d("Body jwtNull", "String jwtNull : " + jwtNull);
            }

        }.start();
        countDownTimer.start();
    }

    private void showDialogs() {
        new SweetAlertDialog(FragmentActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Session Login telah habis")
                .setContentText("silahkan lakukan login kembali untuk menggunakan aplikasi")
                .setConfirmText("ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog
                                .setTitleText("!")
                                .setContentText("")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    }
                })

                .show();
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//                this);
//
//        // set title dialog
//        alertDialogBuilder.setTitle("Session Login telah habis, silahkan login kembali");
//
//        // set pesan dari dialog
//        alertDialogBuilder
//                .setMessage("Klik Ok untuk login")
//                .setIcon(R.drawable.no_result)
//                .setCancelable(false)
//                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog,int id) {
//
//                        FragmentActivity.this.finish();
//                    }
//                });
//
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
    }


    private void setTimerValues() {
        int minute = 30;
        int minuteDiemn = Integer.parseInt(getString(R.string.minutes));

        // assigning values after converting to milliseconds
        timeCountInMilliSeconds = minuteDiemn * 60 * 1000;
        Log.d("Body jwtNull", "String jwtNull : " + timeCountInMilliSeconds);

    }

    /**
     * method to convert millisecond to time format
     *
     * @param milliSeconds
     * @return HH:mm:ss time formatted string
     */
    private String hmsTimeFormatter(long milliSeconds) {

        String hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));

        return hms;


    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frl_main, fragment).commit();

            return true;
        }
        return false;
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.home_menu:
                fragment = new HomeFragment();
                break;
            case R.id.chart_menu:
                fragment = new AboutFragment();
                break;
            case R.id.account_menu:
                fragment = new AccountFragment();
                break;
            // 13 1 21 coba history fragment
            case R.id.history:
                fragment = new HistoryFragment();
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frl_main, fragment).commit();

        }
        return loadFragment(fragment);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {

        super.onResume();
        Log.e("DEBUG", "onResume of HomeFragment");
        Fragment fragment = null;
        fragment = new HomeFragment();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linearLayout_dialog:
                MeinenDialogFragment dialogFragment = new MeinenDialogFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                ft.replace(R.id.frl_main, dialogFragment);
                ft.commit();
                break;

        }
    }

    @Override
    public void onFinishEditDialog(String inputText) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.time, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.time_running_out:
                Toast.makeText(this, "update clicked", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
