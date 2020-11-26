package com.example.uploadandviewimage;

public class GrainItem
{
    private GrainSize GrainSize;

    private GrainShape Shape;

    private GrainType GrainType;

    public GrainSize getGrainSize ()
    {
        return GrainSize;
    }

    public void setGrainSize (GrainSize GrainSize)
    {
        this.GrainSize = GrainSize;
    }

    public GrainShape getShape ()
    {
        return Shape;
    }

    public void setShape (GrainShape Shape)
    {
        this.Shape = Shape;
    }

    public GrainType getGrainType ()
    {
        return GrainType;
    }

    public void setGrainType (GrainType GrainType)
    {
        this.GrainType = GrainType;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [GrainSize = "+GrainSize+", Shape = "+Shape+", GrainType = "+GrainType+"]";
    }
}