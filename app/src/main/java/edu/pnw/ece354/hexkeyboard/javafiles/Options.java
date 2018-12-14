package edu.pnw.ece354.hexkeyboard.javafiles;

public class Options
{
    //types of options
    public String instrument;
    public String noteLayout;
    public String musicScale;
    public String keyDisplay;
    public String colorScheme;
    public double volume;

    //defaults
    public Options()
    {
        instrument = "Piano";
        noteLayout = "Wicki-Hayden";
        musicScale = "12EDO";
        keyDisplay = "Scientific";
        colorScheme = "B&W";
        volume = 100;
    }
}
