package com.example.uploadandviewimage;

import java.io.Serializable;

public class GrainPie implements Serializable {
    private String Name;
    private double Value;
    private double Percent;

//    private String NameSize;
//    private double ValueSize;
//    private double PercentSize;
//
//    public String getNameSize() {
//        return NameSize;
//    }
//
//    public void setNameSize(String nameSize) {
//        NameSize = nameSize;
//    }
//
//    public double getValueSize() {
//        return ValueSize;
//    }
//
//    public void setValueSize(double valueSize) {
//        ValueSize = valueSize;
//    }
//
//    public double getPercentSize() {
//        return PercentSize;
//    }
//
//    public void setPercentSize(double percentSize) {
//        PercentSize = percentSize;
//    }

    public String getName ()
    {
        return Name;
    }

    public void setName (String name)
    {
        this.Name = name;
    }

    public double getValue ()
    {
        return Value;
    }

    public void setValue (double value)
    {
        this.Value = value;
    }

    public double getPercent ()
    {
        return Percent;
    }

    public void setPercent (double percent)
    {
        this.Percent = percent;
    }
}
