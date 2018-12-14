/*
    Project:
    Android Hexagon Keyboard App

    Group Members:
    Javier Campos
    Ashlin Gibson
 */

package edu.pnw.ece354.hexkeyboard;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.content.Context;
//import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import edu.pnw.ece354.hexkeyboard.javafiles.*;

import android.widget.Toast;

import java.io.Serializable;

import static android.app.PendingIntent.getActivity;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    //music stuff
    MusicScale Scale_12EDO = new MusicScale(12);
    double A4 = 440.0; //A4 = 440 hz default
    double C0 = A4 * Math.pow(2.0,(-9.0 / 12.0)) / 16.0; //12edo C0 from A4
    double C0_just = A4 * (3.0/5.0) / 16.0; //just C0 from A4

    //hexagons stored in 2d array
    int numx = 10;
    int numy = 12;
    Hexagon[][] hexys = new Hexagon[numx*2+1][numy*2+1];
    float initialX, initialY; //for touch event

    //options used
    double radius = 69.0;
    double apothem = (Math.sqrt(3.0) / 2.0) * radius;
    Vertex mCenter;
    Vertex trueCenter;

    boolean panning = false;
    boolean rescaling = false;

    Options options;

    /**
     * Calculates the hexagons and their vertices but does not draw them.
     * @param ScreenCenter Coordinates for the center of the drawing area
     */
    void calcHexagonGrid(Vertex ScreenCenter)
    {
        for (int x = -numx; x <= numx; x++) {
            for (int y = -numy; y <= numy; y++) {
                int[] coords = new int[]{x, y};
                Vertex center = new Vertex(ScreenCenter.getX() + apothem * 2.0 * x - (y % 2.0) * apothem, ScreenCenter.getY() - radius * y * 1.5);
                //wicki-hayden
                int noteindex = coords[1]*6 - (coords[1]%2) + coords[0]*2;
                hexys[x + numx][y + numy] = new Hexagon(center, radius, coords, noteindex);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //setContentView(new MyView(this));

        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //toolbar.setTitle("Hexys");

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int ScreenWidth = size.x;
        int ScreenHeight = size.y - 220; //fix later
        trueCenter = new Vertex((float)ScreenWidth/2,(float)ScreenHeight/2);
        mCenter = trueCenter;

        options = (Options)getIntent().getSerializableExtra("options");
        //retrieve options from passed options object
        if(options == null) //else set default
        {
            System.out.println("reset object");
            options = new Options();
            options.volume = 100.0;
            options.instrument = "Piano";
            options.keyDisplay = "Scientific";
            options.noteLayout = "WH";
            options.colorScheme = "B&W";
            options.radius = 69.0;
            options.musicScale = "12EDO";
            options.mCenterx = trueCenter.getX();
            options.mCentery = trueCenter.getY();
        }
        System.out.println(options.instrument);
        radius = options.radius;
        apothem = (Math.sqrt(3.0) / 2.0) * radius;
        mCenter = new Vertex(options.mCenterx,options.mCentery);
        //set init coords to retrieved center
        initialX = (float)mCenter.getX();
        initialY = (float)mCenter.getY();
        //create hexagon grid
        calcHexagonGrid(mCenter);
        setContentView(new MyView(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    //: menu items
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                //settings
                Intent intent = new Intent(this, Main2Activity.class);
                intent.putExtra("options",options);
                startActivity(intent);
                break;
            case R.id.action_pan:
                //pan modez
                panning = !panning;
                rescaling = false;
                Log.d(TAG,String.format("panning set to %b",panning));
                break;
            case R.id.action_rescale:
                //rescale mode
                panning = false;
                rescaling = !rescaling;
                Log.d(TAG,String.format("rescaling set to %b",rescaling));
                break;
            default:
                // unknown error
        }
        setContentView(new MyView(this)); //redraw
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        options = (Options)getIntent().getSerializableExtra("options");
        //retrieve options from passed options object
        if(options == null) //else set default
        {
            System.out.println("reset object");
            options = new Options();
            options.volume = 100.0;
            options.instrument = "Piano";
            options.keyDisplay = "Scientific";
            options.noteLayout = "WH";
            options.colorScheme = "B&W";
            options.radius = 69.0;
            options.musicScale = "12EDO";
            options.mCenterx = trueCenter.getX();
            options.mCentery = trueCenter.getY();
        }
        System.out.println(options.instrument);
        radius = options.radius;
        apothem = (Math.sqrt(3.0) / 2.0) * radius;
        mCenter = new Vertex(options.mCenterx,options.mCentery);
        //set init coords to retrieved center
        initialX = (float)mCenter.getX();
        initialY = (float)mCenter.getY();
        //create hexagon grid
        calcHexagonGrid(mCenter);
        setContentView(new MyView(this));

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //mGestureDetector.onTouchEvent(event);

        int action = event.getActionMasked();
        boolean down = false;


        switch (action) {

            case MotionEvent.ACTION_DOWN:
                initialX = event.getX();
                initialY = event.getY(); //fix later

                Log.d(TAG, String.format("Down init coords: (%f, %f)",initialX,initialY));

                //check point inside which hexagon here
                Vertex TouchV = new Vertex((double)initialX,(double)initialY-220);
                int[] TouchHexagonCoords = new int[2]; //integer coordinates of hexagon
                boolean TouchHexagonMatch = false; //is inside ANY hexagon
                Hexagon TouchHexagon = null;
                for (Hexagon[] hexagon_row : hexys) {
                    for (Hexagon hexagon : hexagon_row) {
                        if (hexagon.pointInHexagon(TouchV)) {
                            TouchHexagonCoords = hexagon.getCoords();
                            TouchHexagonMatch = true;
                            TouchHexagon = hexagon;

                        }
                    }
                }

                if(TouchHexagonMatch && !panning && !rescaling) {
                    //show hexagon vertices for debugging
                    Vertex[] v = TouchHexagon.getVertices();
                    /*for(Vertex vv : v)
                    {
                        System.out.println(vv);
                    }*/

                    Log.d(TAG, String.format("Clicked in hexagon: (%d, %d)", TouchHexagonCoords[0], TouchHexagonCoords[1]));
                    int noteindex = TouchHexagon.getNoteIndex();
                    int notename_index = Scale_12EDO.noteIndexToScaleIndex(noteindex);
                    int notename_octave = Scale_12EDO.noteIndexOctave(noteindex);
                    Log.d(TAG, String.format("Note to play: (index %d) \'%s%d\'", noteindex, Scale_12EDO.getNoteNames()[notename_index], notename_octave));
                    Log.d(TAG,String.format("Frequency (Hz) = %f",Scale_12EDO.noteIndexPitch(noteindex,A4)));
                    //audio playback part
                    //move to separate stream so activity doesn't hang
                    System.out.println(options.instrument);
                    Thread t1 = new Thread(new Audio(Audio.getHKAudioFileFromNI(noteindex,Scale_12EDO,A4,options.instrument),this));
                    t1.start();

                }

                break;

            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();
                Log.d(TAG, String.format("init coords: (%.1f, %.1f)", initialX, initialY));
                Log.d(TAG, String.format("move coords: (%.1f, %.1f)", moveX, moveY));
                if(panning) //only pan if set
                {
                    //how much to pan
                    double panfactor = 0.5;
                    mCenter.setX(mCenter.getX() + panfactor*(moveX - initialX));
                    mCenter.setY(mCenter.getY() + panfactor*(moveY - initialY));
                    //bounds
                    if(mCenter.getX() > 5000.0)
                    {
                        mCenter.setX(5000.0);
                    }
                    if(mCenter.getX() < -5000.0)
                    {
                        mCenter.setX(-5000.0);
                    }
                    if(mCenter.getY() > 2000.0)
                    {
                        mCenter.setY(2000.0);
                    }
                    if(mCenter.getY() < -2000.0)
                    {
                        mCenter.setY(2000.0);
                    }
                    calcHexagonGrid(mCenter);
                    setContentView(new MyView(this));
                }
                else if(rescaling) //only pan if set
                {
                    //how fast to scale
                    double scalingfactor = 0.1;
                    //uses each axis seperately. Top right is increase
                    radius = radius + -scalingfactor*(moveY - initialY);
                    radius = radius + scalingfactor*(moveX - initialX);
                    //bounds
                    if(radius < 30.0)
                    {
                        radius = 30.0;
                    }
                    else if(radius > 200.0)
                    {
                        radius = 200.0;
                    }
                    apothem = (Math.sqrt(3.0) / 2.0) * radius;
                    calcHexagonGrid(mCenter);
                    setContentView(new MyView(this));
                }
                break;

            case MotionEvent.ACTION_UP:

                Log.d(TAG, "Action was UP");
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
        Paint paint;

        public MyView(Context context) {
            super(context);
            paint = new Paint();
        }


        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);
            //draw the Hexagons
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);

            for (Hexagon[] hexagon_row : hexys)
            {
                for (Hexagon hexagon : hexagon_row)
                {
                    LineSeg[] lineSegs = hexagon.getLineSegs();
                    int[] coords = hexagon.getCoords();
                    int x = coords[0];
                    int y = coords[1];

                    Path path = new Path();
                    path.setFillType(Path.FillType.EVEN_ODD);
                    Vertex[] vfirst = lineSegs[0].getVertices();
                    path.moveTo((float) vfirst[0].getX(), (float) vfirst[0].getY());
                    for (LineSeg l : lineSegs) {
                        Vertex[] v;
                        v = l.getVertices();
                        //fill
                        path.lineTo((float) v[1].getX(), (float) v[1].getY());
                        //line borders
                        paint.setColor(Color.parseColor("#000000"));
                        paint.setStrokeWidth(10);
                        canvas.drawLine((float) v[0].getX(), (float) v[0].getY(), (float) v[1].getX(), (float) v[1].getY(), paint);
                    }
                    //hash set later for extention
                    int ni = hexagon.getNoteIndex(); //note index
                    int nim = Scale_12EDO.noteIndexToScaleIndex(ni); //note index mod 12 and shifted for C
                    //12edo only for now
                    if(nim == 0 || nim == 2 || nim == 5 || nim == 7 || nim == 9) //0 is C#
                    {
                        paint.setColor(Color.parseColor("#696969"));
                    }
                    else
                    {
                        paint.setColor(Color.parseColor("#F8F8F8"));
                    }
                    canvas.drawPath(path, paint);
                    path.close();
                    //draw text
                    paint.setColor(Color.BLACK);
                    paint.setTextSize((float)(radius * 0.75));
                    //String s = String.format("(%d,%d)", x, y);
                    //nts please clean this copypaste code up later
                    //C-4 (middle C) as default note
                    int notename_octave = Scale_12EDO.noteIndexOctave(ni);
                    String s = Scale_12EDO.getNoteNames()[nim] + notename_octave;
                    canvas.drawText(s, (float) hexagon.getCenter().getX()-(float)(apothem * 3.0/4.0), (float) hexagon.getCenter().getY(), paint);
                    paint.setTextSize(100);
                    if(panning)
                    {
                        s = "PANNING MODE";
                    }
                    else if(rescaling)
                    {
                        s = "RESCALING MODE";
                    }
                    else
                    {
                        s = "";
                    }
                    canvas.drawText(s, (float)50.0, (float)900.0, paint);
                }
            }
        }
    }
}
