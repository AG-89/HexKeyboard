public class Vertex {
    private double x, y;

    public Vertex() {
        x = 0.0;
        y = 0.0;
    }

    public Vertex(double ax, double ay) {
        x = ax;
        y = ay;
    }

    public double getX()
    {
        return x;
    }
    public double getY()
    {
        return y;
    }
    public double[] getCoords()
    {
        double[] coords = new double[2];
        coords[0] = getX();
        coords[1] = getY();
        return coords;
    }
    public void setX(double ax)
    {
        x = ax;
    }
    public void setY(double ay)
    {
        y = ay;
    }
    public void setCoords(double ax, double ay)
    {
        setX(ax);
        setY(ay);
    }
}
