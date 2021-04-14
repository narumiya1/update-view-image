package com.example.uploadandviewimage.roomdbGhistory;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.uploadandviewimage.GrainItem;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface GHistoryDao  {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertGrain(GHistory gHistory);

//    20 2 21

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertAllItem(Gitem items);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertJson(Gitem items);

    @Update
    int updateGrainHistory(GHistory gHistory);

    @Delete
    void deleteGrainHistory(GHistory gHistory);

    @Query("SELECT * FROM tbGrainHistory order by created_at DESC")
    GHistory[] selectAllItems();

    @Query("SELECT json FROM tbGrainItem WHERE created_at=(SELECT MAX(created_at) FROM tbGrainItem )")
    String selectJsonHistory();

    @Query("SELECT * FROM tbGrainHistory WHERE id = :id ")
    GHistory selectBarangDetail(int id);

    @Query("SELECT image FROM tbGrainHistory")
    List<String> getImageStorage();

//    @Query("SELECT created_at FROM tbGrainHistory")
//    List<Date> getDate();
    @Query("SELECT jenis FROM tbGrainItem")
    String getJenis();

//    @Query("SELECT jumlah FROM tbGjumlah")
//    int getJumlah();
}
