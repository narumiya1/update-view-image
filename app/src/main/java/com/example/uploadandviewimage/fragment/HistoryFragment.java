package com.example.uploadandviewimage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.example.uploadandviewimage.GrainPie;
import com.example.uploadandviewimage.R;
import com.example.uploadandviewimage.SecondActivity;

//implements FragmentResultListener
public class HistoryFragment extends Fragment implements FragmentResultListener {
    TextView histprycb;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return  inflater.inflate(R.layout.fragment_history,container, false);
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        histprycb = view.findViewById(R.id.historycb);

        return view;

    }


//    @Override
//    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
//        getParentFragmentManager()
//                .setFragmentResultListener("requestKey", this, new FragmentResultListener() {
//                    @Override
//                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
//                        String result = getArguments().getString("bundleKey");
//                        histprycb.setText(result);
//                    }
//                });
//    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("BundleKey_Home", this, this::onFragmentResult);
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // We set the listener on the child fragmentManager
//        getChildFragmentManager()
//                .setFragmentResultListener("BundleKey_Home", this, new FragmentResultListener() {
//                    @Override
//                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
//                        Bundle bundle1 = new Bundle();
//                        String result = bundle1.getString("BundleKey_Home");
//                        // Do something with the result
//                    }
//                });
//    }

    @Override
    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
        String mResult = result.getString("data");

    }
}
