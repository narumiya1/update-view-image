package com.example.uploadandviewimage.roomdbGhistory;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "tbGjumlah")
public class Gjumlah implements Serializable {
    @ColumnInfo(name = "jumlah")
    public int jumlah;

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
}