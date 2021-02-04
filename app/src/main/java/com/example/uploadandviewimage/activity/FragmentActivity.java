package com.example.uploadandviewimage.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.uploadandviewimage.R;
import com.example.uploadandviewimage.auth.Sesion;
import com.example.uploadandviewimage.fragment.AccountFragment;
import com.example.uploadandviewimage.fragment.AboutFragment;
import com.example.uploadandviewimage.fragment.HistoryFragment;
import com.example.uploadandviewimage.fragment.HomeFragment;
import com.example.uploadandviewimage.fragment.MeinenDialogFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FragmentActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener, MeinenDialogFragment.DialogListener {

    Sesion sesion;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragmnet);
        sesion = new Sesion(this);
        if (sesion.isLoggedIn()){

        }
        loadFragment(new HomeFragment());

        BottomNavigationView bottomNavigationView = findViewById(R.id.btn_main);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

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
        switch (view.getId()){
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


}
