package com.example.uploadandviewimage.helper;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.uploadandviewimage.utils.TimeStampConverters;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "tbType")
public class GrainTypeData implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int typeId;

    @ColumnInfo(name = "nama_type")
    public String namaType;

    @ColumnInfo(name = "type_value")
    public double val;

    @ColumnInfo(name = "type_percent")
    public double pct;

    @ColumnInfo(name = "nama_szie")
    public String namaSize;

    @ColumnInfo(name = "size_value")
    public double val_size;

    @ColumnInfo(name = "pct_percent")
    public double pct_size;

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

    public double getVal() {
        return val;
    }

    public void setVal(double val) {
        this.val = val;
    }

    public double getPct() {
        return pct;
    }

    public void setPct(double pct) {
        this.pct = pct;
    }

    public String getNamaSize() {
        return namaSize;
    }

    public void setNamaSize(String namaSize) {
        this.namaSize = namaSize;
    }

    public double getVal_size() {
        return val_size;
    }

    public void setVal_size(double val_size) {
        this.val_size = val_size;
    }

    public double getPct_size() {
        return pct_size;
    }

    public void setPct_size(double pct_size) {
        this.pct_size = pct_size;
    }
}
