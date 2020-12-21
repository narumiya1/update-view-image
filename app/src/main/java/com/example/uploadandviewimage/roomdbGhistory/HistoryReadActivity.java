package com.example.uploadandviewimage.roomdbGhistory;

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

import java.util.ArrayList;
import java.util.Arrays;

public class HistoryReadActivity extends AppCompatActivity {

    private AppDatabase db;
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<GHistory> listGrainType;

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
        listGrainType = new ArrayList<>();

        /**
         * Initialize database
         * allow main thread queries
         */

//        db = Room.databaseBuilder(getApplicationContext(), AppzDatabase.class, "tbType").allowMainThreadQueries().build();
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "tbGrainHistory")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addMigrations(AppDatabase.MIGRATION_2_3)
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
        listGrainType.addAll(Arrays.asList(db.gHistoryDao().selectAllItems()));
        GrainHistoryCollection historyCol = new GrainHistoryCollection(listGrainType);
        ArrayList<GrainHistory>history = historyCol.GetList();

        // filter data depend on date
        /**
         * Set all data ke adapter, dan menampilkannya
         */
        adapter = new AdapterTypeRecyclerView(history, this );
        rvView.setAdapter(adapter);
    }


    public static Intent getActIntent(Activity activity) {
        // kode untuk pengambilan Intent
        return new Intent(activity, HistoryReadActivity.class);
    }
}
