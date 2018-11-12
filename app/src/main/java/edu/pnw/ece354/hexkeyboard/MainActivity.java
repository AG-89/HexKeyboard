package edu.pnw.ece354.hexkeyboard;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.content.Context;
//import android.os.Bundle;
        import android.util.Log;
        import android.view.MotionEvent;
        import android.view.View;
        import edu.pnw.ece354.hexkeyboard.javafiles.*;
        import android.gesture.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(new MyView(this));

    }

    private String TAG = MainActivity.class.getSimpleName();
    float initialX, initialY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //mGestureDetector.onTouchEvent(event);

        int action = event.getActionMasked();

        switch (action) {

            case MotionEvent.ACTION_DOWN:
                initialX = event.getX();
                initialY = event.getY();

                Log.d(TAG, "Action was DOWN");
                break;

            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "Action was MOVE");
                break;

            case MotionEvent.ACTION_UP:
                float finalX = event.getX();
                float finalY = event.getY();

                Log.d(TAG, "Action was UP");

                if (initialX < finalX) {
                    Log.d(TAG, "Left to Right swipe performed");
                }

                if (initialX > finalX) {
                    Log.d(TAG, "Right to Left swipe performed");
                }

                if (initialY < finalY) {
                    Log.d(TAG, "Up to Down swipe performed");
                }

                if (initialY > finalY) {
                    Log.d(TAG, "Down to Up swipe performed");
                }

                break;

            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG,"Action was CANCEL");
                break;

            case MotionEvent.ACTION_OUTSIDE:
                Log.d(TAG, "Movement occurred outside bounds of current screen element");
                break;
        }

        return super.onTouchEvent(event);
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

            Hexagon[][] hexys = new Hexagon[10][10];
            double radius = 69.0;
            for(int x = 0; x < 10; x++)
            {
                for(int y = 0; y < 10; y++)
                {
                    Vertex center = new Vertex(radius + radius*x*2, radius+ radius * 2*y);
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
