package com.example.uploadandviewimage.roomdbGhistory;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

    @Entity(tableName = "tbGindeks")
    public class Gindeks implements Serializable {
        @PrimaryKey(autoGenerate = true)
        public int id;
        @ColumnInfo(name = "type")
        public int type;
        @ColumnInfo(name = "value")
        public int value;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }