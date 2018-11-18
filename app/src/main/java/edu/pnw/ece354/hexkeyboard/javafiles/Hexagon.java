package edu.pnw.ece354.hexkeyboard.javafiles;

import java.lang.Math;

public class Hexagon {
    Vertex[] vertices; //6 points
    LineSeg[] lineSegs; //6 line segments
    Vertex center;
    double R, A; //Major Radius > Apothem (smaller radius)
    double rotationAngle;
    double stretchX, stretchY;
    double skewX, skewY;
    int[] coords; //2d integer coordinate
    int noteindex;

    public Hexagon()
    {
        this(new Vertex(0.0,0.0),1.0,new int[]{0,0},0);
    }
    public Hexagon(Vertex c)
    {
        this(c,1.0,new int[]{0,0},0);
    }
    public Hexagon(Vertex c, double Radius)
    {
        this(c,Radius,new int[]{0,0},0);
    }
    public Hexagon(Vertex c, double Radius, int[] co)
    {
        this(c,Radius,co,0);
    }
    public Hexagon(Vertex c, double Radius, int[] co, int ni)
    {
        center = c;
        setR(Radius);
        coords = co;
        noteindex = ni;
    }
    //add transformation constructors later

    public void setA(double a)
    {
        A = a;
        R = (2.0 / Math.sqrt(3.0)) * A;
        calcGeo();
    }
    public void setR(double r)
    {
        R = r;
        A = (Math.sqrt(3.0) / 2.0) * R;
        calcGeo();
    }
    public void setCenter(Vertex c)
    {
        center = c;
        calcGeo();
    }

    private void calcVertices()
    {
        vertices = new Vertex[6];
        /*vertices[0] = new Vertex(center.getX() + R,center.getY());
        vertices[1] = new Vertex(center.getX() + R/2,center.getY() + A);
        vertices[2] = new Vertex(center.getX() - R/2,center.getY() + A);
        vertices[3] = new Vertex(center.getX() - R,center.getY());
        vertices[4] = new Vertex(center.getX() - R/2,center.getY() - A);
        vertices[5] = new Vertex(center.getX() + R/2,center.getY() - A);*/

        //rotate 90deg
        vertices[0] = new Vertex(center.getX() + A,center.getY() + R/2);
        vertices[1] = new Vertex(center.getX(),center.getY() + R);
        vertices[2] = new Vertex(center.getX() - A,center.getY() + R/2);
        vertices[3] = new Vertex(center.getX() - A,center.getY() - R/2);
        vertices[4] = new Vertex(center.getX(),center.getY() - R);
        vertices[5] = new Vertex(center.getX() + A,center.getY() - R/2);
    }
    private void calcLineSegs()
    {
        lineSegs = new LineSeg[6];
        for(int x = 0; x <= 5; x++)
        {
            int x2 = (x >= 5) ? 0 : x+1; //wrap 6 -> 0
            lineSegs[x] = new LineSeg(vertices[x], vertices[x2]);
        }
    }
    private void calcGeo() //recalculate all the geometry
    {
        calcVertices();
        calcLineSegs();
    }

    public Vertex getCenter()
    {
        return center;
    }
    public Vertex[] getVertices()
    {
        return vertices;
    }
    public LineSeg[] getLineSegs()
    {
        return lineSegs;
    }
    public double getA()
    {
        return A;
    }
    public double getR()
    {
        return R;
    }

    public void setCoords(int a,int b)
    {
        coords = new int[]{a,b};
    }
    public int[] getCoords()
    {
        return coords;
    }
    public int getNoteIndex() {
        return noteindex;
    }
    public void setNoteIndex(int noteindex) {
        this.noteindex = noteindex;
    }

    /**
     * Checks if a point is inside the hexagon
     * @param p Point (Vertex) to check
     * @return bool result
     */
    public boolean pointInHexagon(Vertex p)
    {
        return PointInPolygon.isInside(vertices, 6, p);
    }
}
