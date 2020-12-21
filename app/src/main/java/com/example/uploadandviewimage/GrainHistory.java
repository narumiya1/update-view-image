package com.example.uploadandviewimage;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.uploadandviewimage.GrainPie;
import com.example.uploadandviewimage.GrainType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GrainHistory implements Serializable {

    ArrayList<GrainPie> sizes;
    ArrayList<GrainPie> types;
    private Date dateTime;

    public ArrayList<GrainPie> getPie ()
    {
        return sizes;
    }

    public void setPie (ArrayList<GrainPie> sizes)
    {
        this.sizes = sizes;
    }

    public ArrayList<GrainPie> getType ()
    {
        return types;
    }

    public void setType (ArrayList<GrainPie> listType)
    {
        this.types = listType;
    }
    public ArrayList<GrainPie> getSize() {
        return sizes;
    }

    public void setSizes(ArrayList<GrainPie> sizes) {
        this.sizes = sizes;
    }

    public Date getDateTime ()
    {
        return dateTime;
    }

    public void setDateTime (Date dateTime)
    {
        this.dateTime = dateTime;
    }

    public GrainHistory() {

    }

}
