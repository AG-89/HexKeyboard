/*
    Project:
    Android Hexagon Keyboard App

    Group Members:
    Javier Campos
    Ashlin Gibson
 */
package edu.pnw.ece354.hexkeyboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.List;

import edu.pnw.ece354.hexkeyboard.javafiles.*;

public class Main2Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Options options;
    Spinner spinner, spinner2, spinner3, spinner4, spinner5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner2.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        spinner3 = (Spinner) findViewById(R.id.spinner3);
        spinner3.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        spinner4 = (Spinner) findViewById(R.id.spinner4);
        spinner4.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        spinner5 = (Spinner) findViewById(R.id.spinner5);
        spinner5.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        List<String> categories = new ArrayList<String>();
        categories.add("Piano");
        categories.add("Harpsichord");

        List<String> categories2 = new ArrayList<String>();
        categories2.add("Wicki-Hayden");
        categories2.add("Janko");

        List<String> categories3 = new ArrayList<String>();
        categories3.add("12-EDO");
        categories3.add("Just-5lim");

        List<String> categories4 = new ArrayList<String>();
        categories4.add("Scientific");
        categories4.add("Note Only");

        List<String> categories5 = new ArrayList<String>();
        categories5.add("Black & White");
        categories5.add("Green & White");
        categories5.add("Rainbow 5ths");
        categories5.add("Random");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories2);
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories3);
        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories4);
        ArrayAdapter<String> dataAdapter5 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories5);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);
        spinner2.setAdapter(dataAdapter2);
        spinner3.setAdapter(dataAdapter3);
        spinner4.setAdapter(dataAdapter4);
        spinner5.setAdapter(dataAdapter5);

    }

    @Override
    protected void onResume() {
        super.onResume();

        options = (Options)getIntent().getSerializableExtra("options");

        //set options menu from passed options object

        //instrument
        if(options.instrument.equals("Piano"))
            spinner.setSelection(0);
        else
            spinner.setSelection(1);

        //note layout
        if(options.noteLayout.equals("WH"))
            spinner2.setSelection(0);
        else
            spinner2.setSelection(1);

        //music scale
        if(options.musicScale.equals("12EDO"))
            spinner3.setSelection(0);
        else
            spinner3.setSelection(1);

        //key display
        if(options.keyDisplay.equals("Scientific"))
            spinner4.setSelection(0);
        else
            spinner4.setSelection(1);

        //color scheme
        if(options.colorScheme.equals("B&W"))
            spinner5.setSelection(0);
        else if(options.colorScheme.equals("G&W"))
            spinner5.setSelection(1);
        else if(options.colorScheme.equals("Rainbow 5ths"))
            spinner5.setSelection(2);
        else
            spinner5.setSelection(3);
    }

     public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

         String item = parent.getItemAtPosition(position).toString();
         //def not the preferred method but it functions here at least...
         switch(item) {
             case "Harpsichord":
                 options.instrument = "Harpsichord";
                 break;
             case "Piano":
                 options.instrument = "Piano";
                 break;
             case "Wicki-Hayden":
                 options.noteLayout = "WH";
                 break;
             case "Janko":
                 options.noteLayout = "Janko";
                 break;
             case "12-EDO":
                 options.musicScale = "12EDO";
                 break;
             case "Just-5lim":
                 options.musicScale = "Just-5lim";
                 break;
             case "Scientific":
                 options.keyDisplay = "Scientific";
                 break;
             case "Note Only":
                 options.keyDisplay = "Note Only";
                 break;
             case "Black & White":
                 options.colorScheme = "B&W";
                 break;
             case "Green & White":
                 options.colorScheme = "G&W";
                 break;
             case "Rainbow 5ths":
                 options.colorScheme = "Rainbow 5ths";
                 break;
             case "Random":
                 options.colorScheme = "Random";
                 break;
             default:
                 break;
         }
     }

    // Required to implement onNothingSelected as we implement AdapterView.OnItemSelectedListener
    // otherwise class must be declared abstract
    public void onNothingSelected(AdapterView<?> arg0) {
        // Do nothing
    }

    @Override
    protected void onPause() {
        super.onPause();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("options", options);
        startActivity(intent);
    }
}
