package com.example.uploadandviewimage.helper;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.uploadandviewimage.GrainItem;

@Dao
public interface TypeDAO  {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertBarang(Type barang);

    @Update
    int updateBarang(Type barang);

    @Delete
    int deleteBarang(Type barang);

    @Query("SELECT * FROM tbType")
    Type[] selectAllItems();

    @Query("SELECT * FROM tbType WHERE typeId = :id LIMIT 1")
    Type selectBarangDetail(int id);

}
