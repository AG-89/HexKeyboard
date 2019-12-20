/*
    Project:
    Android Hexagon Keyboard App

    Group Members:
    Javier Campos
    Ashlin Gibson
 */

package edu.pnw.ece354.hexkeyboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

import edu.pnw.ece354.hexkeyboard.javafiles.*;


public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    //music stuff
    MusicScale Scale_12EDO = new MusicScale(12);
    MusicScale Scale_Just5lim = new MusicScale(new String[]{"Db","D-","Eb","E-","F-","F#","G-","Ab","A-","Bb","B-","C-"},
                                            new double[]{16.0/15,9.0/8,6.0/5,5.0/4,4.0/3,45.0/32,3.0/2,8.0/5,5.0/3,16.0/9,15.0/8,2.0/1});

    //reference pitches
    double A4 = 440.0; //A4 = 440 hz default
    double C0 = A4 * Math.pow(2.0,(-9.0 / 12.0)) / 16.0; //12edo C0 from A4
    //double C0_just = A4 * (3.0/5.0) / 16.0; //just C0 from A4

    //hexagons stored in 2d array
    int numx = 20;
    int numy = 10;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int ScreenWidth = size.x;
        int ScreenHeight = size.y - 220; //fix later
        trueCenter = new Vertex((float)ScreenWidth/2,(float)ScreenHeight/2);
        mCenter = trueCenter;
        options = new Options(trueCenter.getX(), trueCenter.getY());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean auto_save_settings = sharedPreferences.getBoolean("key_auto_save",false);
        options.instrument = sharedPreferences.getString("key_instrument", "");
        options.noteLayout = sharedPreferences.getString("key_keyboard_layout", "");
        options.musicScale = sharedPreferences.getString("key_music_scale", "");
        options.keyDisplay = sharedPreferences.getString("key_display", "");
        options.colorScheme = sharedPreferences.getString("key_color_scheme", "");

        apothem = (Math.sqrt(3.0) / 2.0) * options.radius;
        mCenter = new Vertex(options.mCenterx,options.mCentery);

        //create hexagon grid
        calcHexagonGrid(mCenter,options.noteLayout);
        setContentView(new MyView(this));
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        options.instrument = sharedPreferences.getString("key_instrument", "");
        options.noteLayout = sharedPreferences.getString("key_keyboard_layout", "");
        options.musicScale = sharedPreferences.getString("key_music_scale", "");
        options.keyDisplay = sharedPreferences.getString("key_display", "");
        options.colorScheme = sharedPreferences.getString("key_color_scheme", "");

        apothem = (Math.sqrt(3.0) / 2.0) * options.radius;
        mCenter = new Vertex(options.mCenterx,options.mCentery);

        //create hexagon grid
        calcHexagonGrid(mCenter,options.noteLayout);
        setContentView(new MyView(this));

    }

    @Override
    protected void onPause() {
        super.onPause();
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
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

    /**
     * Calculates the hexagons and their vertices but does not draw them.
     * @param ScreenCenter Coordinates for the center of the drawing area
     */
    void calcHexagonGrid(Vertex ScreenCenter, String noteLayout)
    {
        for (int x = -numx; x <= numx; x++) {
            for (int y = -numy; y <= numy; y++) {
                int[] coords = new int[]{x, y};
                Vertex center = new Vertex(ScreenCenter.getX() + apothem * 2.0 * x - (y % 2) * apothem, ScreenCenter.getY() - radius * y * 1.5);
                int noteindex;
                if(noteLayout.equals("Janko")) { //janko
                    if(y >= 0) {
                        noteindex = coords[1]*6 - 7*(coords[1]%2) + coords[0] * 2;
                    }
                    else {
                        noteindex = coords[1]*6 + 5*(coords[1]%2) + coords[0] * 2;
                    }
                }
                else { //wicki-hayden
                    noteindex = coords[1]*6 - (coords[1]%2) + coords[0]*2;
                }
                hexys[x + numx][y + numy] = new Hexagon(center, radius, coords, noteindex);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //mGestureDetector.onTouchEvent(event);

        int action = event.getActionMasked();
        //boolean down = false;

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
                    //Vertex[] v = TouchHexagon.getVertices();

                    Log.d(TAG, String.format("Clicked in hexagon: (%d, %d)", TouchHexagonCoords[0], TouchHexagonCoords[1]));
                    int noteindex = TouchHexagon.getNoteIndex();
                    int notename_index = Scale_12EDO.noteIndexToScaleIndex(noteindex);
                    int notename_octave = Scale_12EDO.noteIndexOctave(noteindex);
                    Log.d(TAG, String.format("Note to play: (index %d) \'%s%d\'", noteindex, Scale_12EDO.getNoteNames()[notename_index], notename_octave));
                    Log.d(TAG,String.format("Frequency (Hz) = %f",Scale_12EDO.noteIndexPitch(noteindex,A4)));
                    //audio playback part
                    //move to separate stream so activity doesn't hang

                    MusicScale scaletoplay;
                    if(options.musicScale.equals("Just-5lim")) {
                        scaletoplay = Scale_Just5lim;
                    }
                    else {
                        scaletoplay = Scale_12EDO;
                    }
                    Thread t1 = new Thread(new Audio(Audio.getHKAudioFileFromNI(noteindex,scaletoplay,A4,options.instrument),this));
                    t1.start();

                }

                break;

            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();
                Log.d(TAG, String.format("init coords: (%.1f, %.1f)", initialX, initialY));
                Log.d(TAG, String.format("move coords: (%.1f, %.1f)", moveX, moveY));
                if(panning) { //only pan if set
                    //how much to pan
                    double panfactor = 0.15;
                    mCenter.setX(mCenter.getX() + panfactor*(moveX - initialX));
                    mCenter.setY(mCenter.getY() + panfactor*(moveY - initialY));
                    //bounds
                    if(mCenter.getX() > 5000.0) {
                        mCenter.setX(5000.0);
                    }
                    if(mCenter.getX() < -5000.0) {
                        mCenter.setX(-5000.0);
                    }
                    if(mCenter.getY() > 2000.0) {
                        mCenter.setY(2000.0);
                    }
                    if(mCenter.getY() < -2000.0) {
                        mCenter.setY(2000.0);
                    }
                    Log.d(TAG, String.format("new Center: (%.1f, %.1f)", mCenter.getX(), mCenter.getY()));
                    calcHexagonGrid(mCenter,options.noteLayout);
                    setContentView(new MyView(this));
                }
                else if(rescaling) { //only pan if set
                    //how fast to scale
                    double scalingfactor = 0.05;
                    //uses each axis seperately. Top right is increase
                    radius = radius + -scalingfactor*(moveY - initialY);
                    radius = radius + scalingfactor*(moveX - initialX);
                    //bounds
                    if(radius < 40.0) {
                        radius = 40.0;
                    }
                    else if(radius > 200.0) {
                        radius = 200.0;
                    }
                    Log.d(TAG, String.format("new radius: %.1f", radius));
                    apothem = (Math.sqrt(3.0) / 2.0) * radius;
                    calcHexagonGrid(mCenter,options.noteLayout);
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

    //view to draw: hex grid
    public class MyView extends View {
        Paint paint;
        Random random = new Random();
        //tertiary rainbow colors (12 = 3*2*2)
        String[] c_rainbow = {"#FF0000", "#FF8000", "#FFFF00", "#80FF00", "#00FF00", "#00FF80",
                                "#00FFFF", "#0080FF", "#0000FF", "#8000FF", "#FF00FF", "#FF0080"};
        Path path = new Path();

        public MyView(Context context) {
            super(context);
            paint = new Paint();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            //draw the Hexagons
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);
            boolean draw = true;

            int rand1 = random.nextInt(256 * 256 * 256);

            for (Hexagon[] hexagon_row : hexys) {
                for (Hexagon hexagon : hexagon_row) {
                    LineSeg[] lineSegs = hexagon.getLineSegs();
                    int[] coords = hexagon.getCoords();
                    int x = coords[0];
                    int y = coords[1];
                    draw = true;
                    Vertex hexcenter = hexagon.getCenter();
                    double distance = Math.sqrt(Math.pow(mCenter.getX() - hexcenter.getX(),2) + Math.pow(mCenter.getY() - hexcenter.getY(),2));
                    //hardcoded value
                    if(distance > 4000.0) {
                        draw = false;
                    }

                    if(draw) { //don't draw if the hexagon is entirely offscreen
                        path.reset();
                        path.setFillType(Path.FillType.EVEN_ODD); //not important for this application
                        Vertex[] vfirst = lineSegs[0].getVertices();
                        path.moveTo((float) vfirst[0].getX(), (float) vfirst[0].getY());
                        for (LineSeg l : lineSegs) {
                            Vertex[] v;
                            v = l.getVertices();
                            //fill
                            path.lineTo((float) v[1].getX(), (float) v[1].getY());
                            //line borders
                            paint.setStrokeWidth(10);
                            canvas.drawLine((float) v[0].getX(), (float) v[0].getY(), (float) v[1].getX(), (float) v[1].getY(), paint);
                        }
                        //hash set later for extension
                        int ni = hexagon.getNoteIndex(); //note index
                        int nim = Scale_12EDO.noteIndexToScaleIndex(ni); //note index mod 12 and shifted for C
                        //12edo only for now

                        String c1, c2;


                        // Keyboard Color
                        switch (options.colorScheme) {
                            case "Black & White": //black & white
                                c1 = "#696969";
                                c2 = "#F8F8F8";
                                if (nim == 0 || nim == 2 || nim == 5 || nim == 7 || nim == 9) {
                                    paint.setColor(Color.parseColor(c1));
                                }
                                else {
                                    paint.setColor(Color.parseColor(c2));
                                }
                                break;
                            case "Green & White": //green & white
                                c1 = "#66ff33";
                                c2 = "#F8F8F8";
                                if (nim == 0 || nim == 2 || nim == 5 || nim == 7 || nim == 9) {
                                    paint.setColor(Color.parseColor(c1));
                                }
                                else {
                                    paint.setColor(Color.parseColor(c2));
                                }
                                break;
                            case "Rainbow 5ths": //rainbow 5ths
                                paint.setColor(Color.parseColor(c_rainbow[(((nim + 1) % 12) * 7) % 12]));
                                break;
                            case "Random": //random colors
                                c1 = String.format("#%06x", rand1);
                                c2 = "#F8F8F8";
                                paint.setColor(Color.parseColor(c1));
                                if (nim == 0 || nim == 2 || nim == 5 || nim == 7 || nim == 9) {
                                    paint.setColor(Color.parseColor(c1));
                                }
                                else {
                                    paint.setColor(Color.parseColor(c2));
                                }
                                break;
                            default: //default all white just in case
                                c1 = "#FFFFFF";
                                paint.setColor(Color.parseColor(c1));
                                break;
                        }

                        canvas.drawPath(path, paint);
                        path.close();
                        paint.setColor(Color.BLACK);
                        //scale text based on radius
                        paint.setTextSize((float) (radius * 0.75));

                        //Note name Display
                        int notename_octave = Scale_12EDO.noteIndexOctave(ni);
                        String s = Scale_12EDO.getNoteNames()[nim];

                        //Current options implemented: Note only & Scientific
                        //Always label note, but Scientific adds octave number as well
                        if (options.keyDisplay.equals("Scientific")) {
                            s += notename_octave;
                            canvas.drawText(s, (float) hexagon.getCenter().getX() - (float) (apothem * 3.0 / 4.0), (float) hexagon.getCenter().getY(), paint);
                        }
                        else { //note only
                            canvas.drawText(s, (float) hexagon.getCenter().getX() - (float) (apothem * 2.0 / 4.0), (float) hexagon.getCenter().getY(), paint);
                        }
                    }
                }
            }

            String s;
            //pan & resize mode notifier
            paint.setTextSize(50);
            if(panning)
                s = "PANNING MODE";
            else if(rescaling)
                s = "RESCALING MODE";
            else
                s = "";

            canvas.drawText(s, (float)50.0, (float)900.0, paint);
        }
    }
}
