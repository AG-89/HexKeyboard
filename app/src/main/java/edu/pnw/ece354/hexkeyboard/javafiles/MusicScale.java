package edu.pnw.ece354.hexkeyboard.javafiles;

import java.lang.Math;

public class MusicScale {

    /*
        pitch format:
        beginning 1/1 is implied, starts instead on second note
        last pitch is generator (usually: octave = 2.0)
     */
    private double[] pitches;
    private String[] notenames;
    private int notenum;

    //specified
    public MusicScale(String[] nn, double[] p)
    {
        notenames = nn;
        pitches = p;
    }
    //generate EDO
    public MusicScale(int edonum)
    {
        if(edonum < 1)
        {
            throw new IllegalArgumentException("EDO num must be >= 1");
        }
        //generate pitches
        pitches = new double[edonum];
        for (int x = 1; x <= edonum; x++) {
            pitches[x-1] = Math.pow(2.0, ((double) x / (double) edonum));
        }
        //generate note names
        switch(edonum)
        {
            case 12:
                //using B major aka all sharps for note names
                notenames = new String[] {"C#","D-","D#","E-","F-","F#","G-","G#","A-","A#","B-","C-"};
                break;
            default:
                notenames = new String[edonum];
                for (int x = 0; x < edonum; x++) {
                    notenames[x] = "N" + Integer.toString(x % edonum) + "-";
                }
                break;
        }
    }

    public String[] getNoteNames()
    {
        return notenames;
    }
    public double[] getPitches()
    {
        return pitches;
    }

    //misc. static methods
    /**
     * Octave reduces a ratio to range [1,2)
     * @param f as a ratio
     * @return Octave reduced f in range [1,2)
     */
    public static double octaveReduce(double f)
    {
        return Math.exp(Math.log(f) % Math.log(2.0));
    }

    /**
     * Converts a frequency from Hz to cents
     * @param Hz in Hz
     * @param C0 in Hz as base reference note (usually derived from A4)
     * @return cents
     */
    public static double Hz2Cents(double Hz, double C0)
    {
        return 1200.0 * Math.log(Hz/C0) / Math.log(2.0);
    }

    /**
     * Converts a frequency from cents to Hz
     * @param cents in cents
     * @param C0 in Hz as a base reference note (usually derived from A4)
     * @return Hz
     */
    public static double Cents2Hz(double cents, double C0)
    {
        return Math.pow(2.0, (cents / 1200.0) * C0);
    }

    /**
     * Gets the octave number based of a frequency in Hz
     * @param Hz in Hz
     * @param C0 in Hz as a base reference note (usually derived from A4)
     * @return Octave number as integer
     */
    public static int getOctaveNumber(double Hz, double C0)
    {
        return (int)Math.floor(Hz2Cents(Hz,C0)/1200.0);
    }
}
