package com.example.uploadandviewimage.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.uploadandviewimage.R;
import com.example.uploadandviewimage.SecondActivity;

public class AboutFragment extends Fragment {
    Button bt;
    EditText et;

    public AboutFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
//        bt = view.findViewById(R.id.btnPassData);
//        et = view.findViewById(R.id.inMessage);

       /* bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("key", et.getText().toString());

                AccountFragment fragment = new AccountFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = myContext.getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.frl_main, fragment);
                transaction.commit();
            }
        });
        */

        /*fragment put & get
        Fragment videoFragment = new ChartFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.account_menu, videoFragment).commit();

        Bundle args = new Bundle();
        String data1 = "a";
        String data2 = "b";
        args.putString("key1", data1);
        args.putString("key2", data2);
        videoFragment.setArguments(args);
*/
        return view;
    }


}

