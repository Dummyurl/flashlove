package it.flashlov;

import android.content.Intent;
import android.os.Bundle;



import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import com.example.karshima.flashlove.R;

public class MainActivity extends Activity implements OnItemSelectedListener,View.OnClickListener{
    private Button btn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpview();
    }

    private void setUpview() {

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        List<String> language = new ArrayList<String>();
        language.add("English");
        language.add("Italic");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, language);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        btn=(Button)findViewById(R.id.confirm);
        btn.setOnClickListener(this);
    }
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.confirm:
                Intent i=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
                break;


        }}



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        // Showing selected spinner item
       // Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }


}