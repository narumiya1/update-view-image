package com.example.uploadandviewimage.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.uploadandviewimage.R;

public class AccountFragment extends Fragment {
    TextView txtData;

    public AccountFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        /*
        txtData = rootView.findViewById(R.id.txtData);
        Bundle bundle = this.getArguments();
        String d = bundle.getString("key");
        txtData.setText(d);
        */
        txtData = (TextView) rootView.findViewById(R.id.txtData);


        Bundle bundle = this.getArguments();


        if (bundle != null) {
            String value1 = getArguments().getString("key1");
            String value2 = getArguments().getString("key2");
            value1=bundle.getString("key1", "a");
            value2=bundle.getString("key2","b");

        }

        return rootView;
    }
}
