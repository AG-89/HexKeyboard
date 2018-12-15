package edu.pnw.ece354.hexkeyboard.javafiles;

//This is a java program to check whether a Vertex lies in a polygon or not
// https://www.sanfoundry.com/java-program-check-whether-given-Vertex-lies-given-polygon/
//modified for floating point
public class PointInPolygon
{
    public static boolean onSegment(Vertex p, Vertex q, Vertex r)
    {
        if (q.getX() <= Math.max(p.getX(), r.getX()) && q.getX() >= Math.min(p.getX(), r.getX())
                && q.getY() <= Math.max(p.getY(), r.getY()) && q.getY() >= Math.min(p.getY(), r.getY()))
            return true;
        return false;
    }

    public static int orientation(Vertex p, Vertex q, Vertex r)
    {
        double val = (q.getY() - p.getY()) * (r.getX() - q.getX()) - (q.getX() - p.getX()) * (r.getY() - q.getY());

        if (Math.abs(val) < 0.0001) // ~== 0 for float
            return 0;
        return (val > 0) ? 1 : 2;
    }

    public static boolean doIntersect(Vertex p1, Vertex q1, Vertex p2, Vertex q2)
    {
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        if (o1 != o2 && o3 != o4)
            return true;

        if (o1 == 0 && onSegment(p1, p2, q1))
            return true;

        if (o2 == 0 && onSegment(p1, q2, q1))
            return true;

        if (o3 == 0 && onSegment(p2, p1, q2))
            return true;

        if (o4 == 0 && onSegment(p2, q1, q2))
            return true;

        return false;
    }

    /**
     * Check if vertex in polygon (array of vertices)
     * Uses even/odd horizontal line test
     * @param polygon array of vertices
     * @param n number of vertices
     * @param p point to check
     * @return bool result
     */
    public static boolean isInside(Vertex polygon[], int n, Vertex p)
    {
        double INF = Double.MAX_VALUE;
        if (n < 3)
            return false;

        Vertex extreme = new Vertex(INF, p.getY());

        int count = 0, i = 0;
        do
        {
            int next = (i + 1) % n;
            if (doIntersect(polygon[i], polygon[next], p, extreme))
            {
                if (orientation(polygon[i], p, polygon[next]) == 0)
                    return onSegment(polygon[i], p, polygon[next]);

                count++;
            }
            i = next;
        } while (i != 0);

        return (count & 1) == 1;
    }
}