package com.example.uploadandviewimage.roomdbGhistory;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.uploadandviewimage.utils.TimeStampConverters;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "tbGrainHistory")
public class GHistory implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "value")
    public double val;

    @ColumnInfo(name = "percent")
    public double pct;

    @ColumnInfo(name = "created_at")
    @TypeConverters({TimeStampConverters.class})
    private Date createdAt;

    @ColumnInfo(name = "image")
    public String image;

    @ColumnInfo(name = "data_type")
    @TypeConverters({TimeStampConverters.class})
    private int dataType;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataTye) {
        this.dataType = dataTye;
    }
}