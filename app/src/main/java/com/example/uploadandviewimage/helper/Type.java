package com.example.uploadandviewimage.helper;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.uploadandviewimage.utils.TimeStampConverters;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "tbType")
public class Type implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int typeId;

    @ColumnInfo(name = "nama_type")
    public String namaType;

    @ColumnInfo(name = "jumlah_type")
    public double jumlahType;

    @ColumnInfo(name = "created_at")
    @TypeConverters({TimeStampConverters.class})
    private Date createdAt;

    @ColumnInfo(name = "modified_at")
    @TypeConverters({TimeStampConverters.class})
    private Date modifiedAt;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }



    public String getNamaType() {
        return namaType;
    }

    public void setNamaType(String namaType) {
        this.namaType = namaType;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getNamaBarang() {
        return namaType;
    }

    public void setNamaBarang(String namaType) {
        this.namaType = namaType;
    }

    public double getJumlahType() {
        return jumlahType;
    }

    public void setJumlahType(double jumlahType) {
        this.jumlahType = jumlahType;
    }


}
