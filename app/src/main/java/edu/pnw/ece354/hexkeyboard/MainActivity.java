package edu.pnw.ece354.hexkeyboard;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.graphics.Path;
        import android.content.Context;
//import android.os.Bundle;
        import android.view.View;
        import edu.pnw.ece354.hexkeyboard.javafiles.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(new MyView(this));
    }

    public class MyView extends View {
        Paint paint = null;

        public MyView(Context context) {
            super(context);
            paint = new Paint();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
//            int x = getWidth();
//            int y = getHeight();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);
            // Use Color.parseColor to define HTML colors
            paint.setColor(Color.parseColor("#80FF00"));

            Paint p = new Paint();
            p.setColor(Color.BLACK);
            p.setStrokeWidth(10);

            int numx = 20;
            int numy = 20;
            Hexagon[][] hexys = new Hexagon[numx][numy];
            double start = 100.0;
            double radius = 69.0;
            double apothem = (Math.sqrt(3.0) / 2.0) * radius;
            for(int x = 0; x < numx; x++)
            {
                for(int y = 0; y < numy; y++)
                {
                    Vertex center = new Vertex(start + apothem*2.0*x - (y%2.0)*apothem,start + radius*y*1.5);

                    hexys[x][y] = new Hexagon(center,radius);
                    LineSeg[] lineSegs = hexys[x][y].getLineSegs();
                    //draw filled in 6 triangles = 1 hexagon

                    //draw lines & fill w/ path
                    Path path = new Path();
                    path.setFillType(Path.FillType.EVEN_ODD);
                    Vertex[] vfirst = lineSegs[0].getVertices();
                    path.moveTo((float)vfirst[0].getX(),(float)vfirst[0].getY());
                    for(LineSeg l : lineSegs)
                    {
                        Vertex[] v;
                        v = l.getVertices();
                        //fill
                        path.lineTo((float)v[1].getX(),(float)v[1].getY());
                        //line borders
                        canvas.drawLine((float)v[0].getX(),(float)v[0].getY(),(float)v[1].getX(),(float)v[1].getY(),p);
                    }
                    canvas.drawPath(path, paint);
                    path.close();
                }
            }
        }
    }
}
