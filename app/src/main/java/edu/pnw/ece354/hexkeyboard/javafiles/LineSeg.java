package edu.pnw.ece354.hexkeyboard.javafiles;

import java.lang.Math;

public class LineSeg {
    private Vertex a,b;

    public LineSeg()
    {
        this(new Vertex(0.0,0.0),new Vertex(0.0,0.0));
    }
    public LineSeg(Vertex aa, Vertex bb)
    {
        a = aa;
        b = bb;
    }

    public Vertex[] getVertices()
    {
        Vertex[] v = new Vertex[2];
        v[0] = a;
        v[1] = b;
        return v;
    }
    public void setVertices(Vertex aa, Vertex bb)
    {
        a = aa;
        b = bb;
    }

    public double distance()
    {
        double ypart = (b.getY() - a.getY());
        double xpart = (b.getX() - a.getX());
        ypart *= ypart;
        xpart *= xpart;
        return Math.sqrt(ypart + xpart);
    }
}