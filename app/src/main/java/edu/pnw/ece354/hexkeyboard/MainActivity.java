package edu.pnw.ece354.hexkeyboard;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
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
            paint.setColor(Color.parseColor("#FF0000"));

            Paint p = new Paint();
            p.setColor(Color.BLACK);

            p.setStrokeWidth(10);

            //draw test
//            Vertex va = new Vertex(0.0,0.0);
//            Vertex vb = new Vertex(500.0,500.0);
//            canvas.drawLine((float)va.getX(),(float)va.getX(),(float)vb.getY(),(float)vb.getY(),p);
//

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
                    Vertex center = new Vertex(start + radius*x*1.5, start + apothem*2.0*y - (x%2.0)*apothem);

                    hexys[x][y] = new Hexagon(center,radius);
                    LineSeg[] lineSegs = hexys[x][y].getLineSegs();
                    for(LineSeg l : lineSegs)
                    {
                        Vertex[] v;
                        v = l.getVertices();
                        canvas.drawLine((float)v[0].getX(),(float)v[0].getY(),(float)v[1].getX(),(float)v[1].getY(),p);

                    }
                }
            }
        }
    }
}
