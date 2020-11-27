package com.example.uploadandviewimage;

import java.io.Serializable;

public class GrainPie implements Serializable {
    private String Name;
    private double Value;
    private double Percent;

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
