package com.example.uploadandviewimage;

public class GrainShape
{
    private double Left;

    private double Top;

    private double Height;

    private double Width;

    public double getLeft ()
    {
        return Left;
    }

    public void setLeft (double Left)
    {
        this.Left = Left;
    }

    public double getTop ()
    {
        return Top;
    }

    public void setTop (double Top)
    {
        this.Top = Top;
    }

    public double getHeight ()
    {
        return Height;
    }

    public void setHeight (double Height)
    {
        this.Height = Height;
    }

    public double getWidth ()
    {
        return Width;
    }

    public void setWidth (double Width)
    {
        this.Width = Width;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Left = "+Left+", Top = "+Top+", Height = "+Height+", Width = "+Width+"]";
    }
}