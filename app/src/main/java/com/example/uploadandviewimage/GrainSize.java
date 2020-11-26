package com.example.uploadandviewimage;

public class GrainSize
{
    private double ScoreIdentification;

    private double ScoreClassification;

    private String Name;

    public double getScoreIdentification ()
    {
        return ScoreIdentification;
    }

    public void setScoreIdentification (double ScoreIdentification)
    {
        this.ScoreIdentification = ScoreIdentification;
    }

    public double getScoreClassification ()
    {
        return ScoreClassification;
    }

    public void setScoreClassification (double ScoreClassification)
    {
        this.ScoreClassification = ScoreClassification;
    }

    public String getName ()
    {
        return Name;
    }

    public void setName (String Name)
    {
        this.Name = Name;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ScoreIdentification = "+ScoreIdentification+", ScoreClassification = "+ScoreClassification+", Name = "+Name+"]";
    }
}
