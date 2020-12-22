package com.example.uploadandviewimage.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.uploadandviewimage.R;
import com.example.uploadandviewimage.fragment.AccountFragment;
import com.example.uploadandviewimage.fragment.AboutFragment;
import com.example.uploadandviewimage.fragment.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FragmentActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener  {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragmnet);

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
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frl_main, fragment).commit();

        }
        return loadFragment(fragment);
    }



}
