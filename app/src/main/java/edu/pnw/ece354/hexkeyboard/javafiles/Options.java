package edu.pnw.ece354.hexkeyboard.javafiles;

import java.io.Serializable;

public class Options implements Serializable
{
    //types of options
    public String instrument;
    public String noteLayout;
    public String musicScale;
    public String keyDisplay;
    public String colorScheme;
    public double volume;
    public double radius;
    public double mCenterx;
    public double mCentery;

    //defaults
    public Options()
    {
        instrument = "Piano";
        noteLayout = "WH";
        musicScale = "12EDO";
        keyDisplay = "Scientific";
        colorScheme = "B&W";
        volume = 100;
        radius = 69.0;
        mCenterx = 0.0;
        mCentery = 0.0;
    }

    public Options(double mCenterx, double mCentery) {
        this();
        this.mCenterx = mCenterx;
        this.mCentery = mCentery;
    }

}
