package edu.pnw.ece354.hexkeyboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner2.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        Spinner spinner3 = (Spinner) findViewById(R.id.spinner3);
        spinner3.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        List<String> categories = new ArrayList<String>();
        categories.add("Piano");
        categories.add("Harpischord");
        categories.add("Orchestra");
        categories.add("more coming soon...");

        List<String> categories2 = new ArrayList<String>();
        categories2.add("Wicki-Hayden");
        categories2.add("more coming soon... maybe");

        List<String> categories3 = new ArrayList<String>();
        categories3.add("12-EDO");
        categories3.add("work in progress");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories2);
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories3);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);
        spinner2.setAdapter(dataAdapter2);
        spinner3.setAdapter(dataAdapter3);
    }

     public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
     {
         String item = parent.getItemAtPosition(position).toString();
         Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();

         i = new Intent(this, MainActivity.class);

         //Bundle bundle = new Bundle();
         //bundle.putString("AnInstrument", item);

         i.putExtra("AnInstrument", item);
     }

    // Required to implement onNothingSelected as we implement AdapterView.OnItemSelectedListener
    // otherwise class must be declared abstract
    public void onNothingSelected(AdapterView<?> arg0)
    {
        // Do nothing
    }

    @Override
    protected void onPause() {
        super.onPause();
        startActivity(i);
    }
}
