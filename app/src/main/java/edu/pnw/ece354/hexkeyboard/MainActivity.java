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
            int x = getWidth();
            int y = getHeight();
            int radius;
            radius = 100;
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);
            // Use Color.parseColor to define HTML colors
            paint.setColor(Color.parseColor("#FF0000"));

            Paint p = new Paint();
            p.setColor(Color.BLACK);

            //draw test
            Vertex va = new Vertex(0.0,0.0);
            Vertex vb = new Vertex(500.0,500.0);
            canvas.drawLine((float)va.getX(),(float)va.getX(),(float)vb.getX(),(float)vb.getY(),p);

            canvas.drawLine(
                    (float)50.0, // startX
                    canvas.getHeight() / 2, // startY
                    canvas.getWidth() - (float)50.0, // stopX
                    canvas.getHeight() / 2, // stopY
                    p // Paint
            );
        }
    }
}
