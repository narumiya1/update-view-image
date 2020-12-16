package com.example.uploadandviewimage.helper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.uploadandviewimage.GrainHistoryCollection;
import com.example.uploadandviewimage.R;
import com.example.uploadandviewimage.GrainHistory;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class RoomReadActivity extends AppCompatActivity {

    private AppzDatabase db;
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<GrainTypeData> daftarBarang;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        /**
         * Initialize layout dan sebagainya
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        /**
         * Initialize ArrayList untuk data barang
         */
        daftarBarang = new ArrayList<>();

        /**
         * Initialize database
         * allow main thread queries
         */

//        db = Room.databaseBuilder(getApplicationContext(), AppzDatabase.class, "tbType").allowMainThreadQueries().build();
        db = Room.databaseBuilder(getApplicationContext(), AppzDatabase.class, "tbType")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addMigrations(AppzDatabase.MIGRATION_4_5)
                .build();
        /**
         * Initialize recyclerview dan layout manager
         */
        rvView = findViewById(R.id.rv_main);
        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);

        /**
         * Add all data to arraylist
         */
//        daftarBarang.addAll(Arrays.asList(db.typeDAO().selectAllItems()));
        daftarBarang.addAll(Arrays.asList(db.typeDAO().selectAllItems()));
        GrainHistoryCollection historyCol = new GrainHistoryCollection(daftarBarang);
        ArrayList<GrainHistory>history = historyCol.GetList();

        // filter data depend on date
        /**
         * Set all data ke adapter, dan menampilkannya
         */
        adapter = new AdapterTypeRecyclerView(daftarBarang,history, this);
        rvView.setAdapter(adapter);
    }


    public static Intent getActIntent(Activity activity) {
        // kode untuk pengambilan Intent
        return new Intent(activity, RoomReadActivity.class);
    }
}
