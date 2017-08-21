package it.flashlov;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.karshima.flashlove.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static com.example.karshima.flashlove.R.id.age;

/**
 * Created by Karshima on 1/10/2017.
 */

public class PfilterResultActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, View.OnFocusChangeListener {

    private Toolbar toolbar;
    private Button cancel,save;
    private EditText gender,sex,country,city,distance,order;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pfilterresult_activity);
        setResult(RESULT_CANCELED);
        setUpview();
    }



    private void setUpview() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Filter People By");


        gender=(EditText)findViewById(R.id.gender);
        sex=(EditText)findViewById(R.id.sex);
        country=(EditText)findViewById(R.id.country);
        city=(EditText)findViewById(R.id.city);
        order=(EditText)findViewById(R.id.order);
        distance=(EditText)findViewById(R.id.distance);

        gender.setOnTouchListener(this);
        sex.setOnTouchListener(this);
        country.setOnTouchListener(this);
        city.setOnTouchListener(this);
        order.setOnTouchListener(this);
        distance.setOnTouchListener(this);

        gender.setOnFocusChangeListener(this);
        sex.setOnFocusChangeListener(this);
        country.setOnFocusChangeListener(this);
        city.setOnFocusChangeListener(this);
        order.setOnFocusChangeListener(this);
        distance.setOnFocusChangeListener(this);


        cancel=(Button)findViewById(R.id.close);
        cancel.setOnClickListener(this);
        save=(Button)findViewById(R.id.save);
        save.setOnClickListener(this);


    }
    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.close:

                setResult(RESULT_CANCELED);
                finish();

                break;

            case R.id.save:

                Intent intent = new Intent();
                intent.putExtra("gender",gender.getText().toString());
                intent.putExtra("sex",sex.getText().toString());
                intent.putExtra("country",country.getText().toString());
                intent.putExtra("city",city.getText().toString());
                intent.putExtra("diastance",distance.getText().toString());
                intent.putExtra("order_by",order.getText().toString());
                setResult(Activity.RESULT_OK, intent);
                finish();

                break;
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:


                setResult(RESULT_CANCELED);
                onBackPressed();


                return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if ((keyCode == KeyEvent.KEYCODE_BACK) ) {

            setResult(RESULT_CANCELED);
            onBackPressed();

        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (v.getId()){

            case R.id.gender:

                if(MotionEvent.ACTION_UP==event.getAction()){

                    genderDialog();
                }



                return true;

            case R.id.sex:

                if(MotionEvent.ACTION_UP==event.getAction()){
                    sexDialog();
                }





                return true;

            case R.id.country:

                if(MotionEvent.ACTION_UP==event.getAction()){

                    countryDialog();
                }




                return true;


            case R.id.city:

                if(MotionEvent.ACTION_UP==event.getAction()){

                    cityDialog();
                }




                return true;

            case R.id.order:

                if(MotionEvent.ACTION_UP==event.getAction()){

                    orderDialog();
                }



                return true;

            case R.id.distance:

                if(MotionEvent.ACTION_UP==event.getAction()){

                    distanceDialog();
                }



                return true;
        }

        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {


        switch (v.getId()){

            case R.id.gender:

                if(hasFocus){

                    genderDialog();

                }


                break;

            case R.id.sex:

                if(hasFocus){


                    sexDialog();

                }


                break;

            case R.id.country:

                if(hasFocus){


                    countryDialog();
                }



                break;


            case R.id.city:

                if(hasFocus){

                    cityDialog();

                }


                break;

            case R.id.order:

                if(hasFocus){


                    orderDialog();
                }


                break;

            case R.id.distance:

                if(hasFocus){


                    distanceDialog();
                }


                break;
        }

    }


    private void genderDialog(){

        final String[] items = new String[]{"All","Male", "Female"};


        AlertDialog.Builder ad = new AlertDialog.Builder(PfilterResultActivity.this);
        ad.setTitle("Select Gender");
        ad.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                gender.setText(items[which]);
            }
        });
        ad.show();
    }

    private void sexDialog(){

        final String[] items = new String[]{"Bisexual", "Heterosexual","Omosexual"};

        AlertDialog.Builder ad = new AlertDialog.Builder(PfilterResultActivity.this);
        ad.setTitle("Select Sexual Orientation");
        ad.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                sex.setText(items[which]);
            }
        });
        ad.show();
    }

    private void countryDialog(){


        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        String country1;
        for( Locale loc : locale ){
            country1 = loc.getDisplayCountry();
            if( country1.length() > 0 && !countries.contains(country1) ){
                countries.add( country1 );
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);

        final String[] items = countries.toArray(new String[countries.size()]);

        AlertDialog.Builder ad = new AlertDialog.Builder(PfilterResultActivity.this);
        ad.setTitle("Select Country");
        ad.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                country.setText(items[which]);
            }
        });
        ad.show();
    }

    private void cityDialog(){


        final String[] items=new String[]{"Abu Dhabi","Abuja","Accra","Addis Ababa","Algiers",
                "Bandar Seri Begawan","Bangkok","Bangui","Beijing","Bishkek","Bissau","Bogotá","Cairo","Canberra",
                "Caracas","Castries","Cayenne","Conakry","Dakar","Damascus","Dili","Djibouti","Doha","Douglas","Dublin",
                "Episkopi Cantonment","Flying Fish Cove","Freetown","Funafuti","Gaborone","George Town","Gibraltar",
                "Hagåtña","Hamilton","Hanga Roa","Hanoi","Hong Kong","Islamabad","Jakarta","Jamestown","Juba","Kabul",
                "Kampala","Kathmandu","Khartoum","Kiev","Kigali","Kingston","Kingstown","Lilongwe","Lima","Lisbon","Ljubljana",
                "London","Luanda","Lusaka","Madrid","Majuro","Malabo","Malé","Mamoudzou","Manila","Maputo","Marigot",
                "Monaco","Monrovia","Montevideo","Moroni","Moscow","Nouakchott","Nuuk","Oranjestad","Oslo","Ottawa","Pago Pago",
                "Palikir","Panama City","Papeete","Paramaribo","Paris","Phnom Penh","Quito","Rabat","Reykjavík","Riga","Riyadh",
                "Road Town","Rome","Roseau","Saipan","San José","San Juan","Taipei","Tallinn","Tarawa","Tehran","Thimphu","Tirana",
                "Ulaanbaatar","Valletta","The Valley","Vatican City","Victoria","Vienna","Warsaw","Washington, D.C.","Wellington","West Island",
                "Windhoek","Yaoundé","Yaren","Yerevan","Zagreb"};

        AlertDialog.Builder add =new AlertDialog.Builder(PfilterResultActivity.this);
        add.setTitle("Select City");
        add.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                city.setText(items[which]);
            }
        });
        add.show();
    }

    private void distanceDialog(){

        final String[] items = new String[]{"No limit", "100 meters","200 meters","500 meters","1 KM","2 KM","3 KM"};

        AlertDialog.Builder ad = new AlertDialog.Builder(PfilterResultActivity.this);
        ad.setTitle("Select Distance");
        ad.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                distance.setText(items[which]);
            }
        });
        ad.show();

    }

    private void orderDialog(){

        final String[] items = new String[]{"Newest", "Oldest","Last Online","Random","Distance"};

        AlertDialog.Builder ad = new AlertDialog.Builder(PfilterResultActivity.this);
        ad.setTitle("Select Order By");
        ad.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                order.setText(items[which]);
            }
        });
        ad.show();

    }

}
