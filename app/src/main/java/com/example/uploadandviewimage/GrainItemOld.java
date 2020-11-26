package com.example.uploadandviewimage;

public class GrainItemOld
{
    private GrainShape Shape;

    private double ScoreIdentification;

    private double ScoreClassification;

    private String Name;

    public GrainShape getShape ()
    {
        return Shape;
    }

    public void setShape (GrainShape Shape)
    {
        this.Shape = Shape;
    }

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
        return "ClassPojo [Shape = "+Shape+", ScoreIdentification = "+ScoreIdentification+", ScoreClassification = "+ScoreClassification+", Name = "+Name+"]";
    }
}