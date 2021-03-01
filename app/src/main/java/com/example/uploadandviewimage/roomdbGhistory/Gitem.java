package com.example.uploadandviewimage.roomdbGhistory;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.uploadandviewimage.utils.TimeStampConverters;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "tbGrainItem")
public class Gitem implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "score")
    public String score;

    @ColumnInfo(name = "json")
    public String json;

    @ColumnInfo(name = "created_at")
    @TypeConverters({TimeStampConverters.class})
    private Date createdAt;


    @ColumnInfo(name = "grainType")
    public double grainType;

    @ColumnInfo(name = "grainSize")
    public double grainSize;

    @ColumnInfo(name = "shape")
    public double shape;



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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public double getGrainType() {
        return grainType;
    }

    public void setGrainType(double grainType) {
        this.grainType = grainType;
    }

    public double getGrainSize() {
        return grainSize;
    }

    public void setGrainSize(double grainSize) {
        this.grainSize = grainSize;
    }

    public double getShape() {
        return shape;
    }

    public void setShape(double shape) {
        this.shape = shape;
    }
}
