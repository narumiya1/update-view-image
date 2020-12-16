package com.example.uploadandviewimage.helper;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.sql.Date;

@Dao
public interface TypeDAO  {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertBarang(GrainTypeData barang);

    @Update
    int updateBarang(GrainTypeData barang);

    @Delete
    int deleteBarang(GrainTypeData barang);

    @Query("SELECT * FROM tbType order by created_at DESC")
    GrainTypeData[] selectAllItems();

    @Query("SELECT * FROM tbType WHERE typeId = :id ")
    GrainTypeData selectBarangDetail(int id);
}
