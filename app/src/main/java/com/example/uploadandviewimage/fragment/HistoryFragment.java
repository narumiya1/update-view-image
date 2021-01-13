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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.uploadandviewimage.GrainHistory;
import com.example.uploadandviewimage.GrainHistoryCollection;
import com.example.uploadandviewimage.GrainPie;
import com.example.uploadandviewimage.R;
import com.example.uploadandviewimage.SecondActivity;
import com.example.uploadandviewimage.roomdbGhistory.AdapterTypeRecyclerView;
import com.example.uploadandviewimage.roomdbGhistory.AppDatabase;
import com.example.uploadandviewimage.roomdbGhistory.GHistory;

import java.util.ArrayList;
import java.util.Arrays;

//implements FragmentResultListener
public class HistoryFragment extends Fragment implements FragmentResultListener {
    private AppDatabase db;
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<GHistory> listGrainType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return  inflater.inflate(R.layout.fragment_history,container, false);
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        listGrainType = new ArrayList<>();
        db = Room.databaseBuilder(getContext(), AppDatabase.class, "tbGrainHistory")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addMigrations(AppDatabase.MIGRATION_2_3)
                .build();
        rvView = view.findViewById(R.id.rv_history);
        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        rvView.setLayoutManager(layoutManager);
        listGrainType.addAll(Arrays.asList(db.gHistoryDao().selectAllItems()));
        GrainHistoryCollection historyCol = new GrainHistoryCollection(listGrainType);
        ArrayList<GrainHistory>history = historyCol.GetList();

        // filter data depend on date
        /**
         * Set all data ke adapter, dan menampilkannya
         */
        adapter = new AdapterTypeRecyclerView(history, getContext() );
        rvView.setAdapter(adapter);
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
