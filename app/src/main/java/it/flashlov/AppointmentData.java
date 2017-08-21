package it.flashlov;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karshima.flashlove.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import it.flashlov.apicall.GetData;
import it.flashlov.utility.MyUtility;
import it.flashlov.utility.PreferencesInterface;
import it.flashlov.utility.URLListner;

/**
 * Created by Karshima on 1/9/2017.
 */

public class AppointmentData extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener,View.OnClickListener,TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener{
        private Toolbar toolbar;
        private EditText e1,e2,e3;
        private String date,time,type="";
        private Button submit,cancel;
        private SharedPreferences sharedPreferences;
        private  String name;
        private  String msg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_activity);

        sharedPreferences=getSharedPreferences(PreferencesInterface.MyPREFERENCES,Context.MODE_PRIVATE);

        Spinner spinner = (Spinner) findViewById(R.id.type);
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> language = new ArrayList<String>();
        language.add("Blind");
        language.add("Regular");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, language);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        setUpview();
    }

    private void setUpview() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Launch a Flash love");



        e1=(EditText)findViewById(R.id.datentime);
        e1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (MotionEvent.ACTION_UP == event.getAction()) {

                    Calendar now = Calendar.getInstance();
                    // now.add(Calendar.DATE, 1);
                    DatePickerDialog dpd = DatePickerDialog.newInstance(
                            AppointmentData.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                    dpd.setThemeDark(true);
                    dpd.vibrate(true);
                    dpd.dismissOnPause(true);
                    dpd.setMinDate(now);
                    Calendar cal = new GregorianCalendar();
                    cal.add(Calendar.DATE, 30);
                    dpd.setMaxDate(cal);
                    dpd.showYearPickerFirst(false);
                    dpd.setAccentColor(getResources().getColor(R.color.colorPrimary));
                    dpd.setTitle("Select Date");
                    dpd.setCancelable(false);
                    dpd.show(getFragmentManager(), "Datepickerdialog");
                }

                return true;
            }
        });
        e2=(EditText)findViewById(R.id.editTextt);
        e3=(EditText)findViewById(R.id.msges);


        submit=(Button)findViewById(R.id.save);
        cancel=(Button)findViewById(R.id.cancel);

        submit.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.save:

                 date=e1.getText().toString().trim();
                 name=e2.getText().toString().trim();
                 msg=e3.getText().toString().trim();

                if(date.equalsIgnoreCase("")){

                    MyUtility.showToast("Enter Date",AppointmentData.this);

                }else if(type.equalsIgnoreCase("")){

                    MyUtility.showToast("Enter Type",AppointmentData.this);

                }else if(name.equalsIgnoreCase("")) {

                    MyUtility.showToast("Enter Secret Name", AppointmentData.this);

                 }else if(msg.equalsIgnoreCase("")){

                   MyUtility.showToast("Enter Message",AppointmentData.this);

                }else {

                    saveData();

                }


                break;

            case R.id.cancel:


                finish();

                break;

        }}

    private void saveData() {

        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.show();

        List<NameValuePair> parameters=new ArrayList<>();
        parameters.add(new BasicNameValuePair("user_id",sharedPreferences.getString(PreferencesInterface.userId,"")));
        parameters.add(new BasicNameValuePair("user_to",""));
        parameters.add(new BasicNameValuePair("date_time",date));
        parameters.add(new BasicNameValuePair("type_of_metting",type));
        parameters.add(new BasicNameValuePair("secret_name",name));
        parameters.add(new BasicNameValuePair("message",msg));
        parameters.add(new BasicNameValuePair("latitudine",String.valueOf(getIntent().getStringExtra("lat"))));
        parameters.add(new BasicNameValuePair("longitudine",String.valueOf(getIntent().getStringExtra("long"))));
        GetData sendRequest=new GetData(parameters);
        sendRequest.setResultListener(new GetData.ResultListner() {
            @Override
            public void onSuccess(JSONObject json) {

                if(dialog.isShowing()){

                    dialog.dismiss();
                }

                try{

                    int status=json.getInt("status");

                    Log.e("STATUS", status + "");

                    if(status==1){

                        MyUtility.showToast("Launched successfully",AppointmentData.this);

                        finish();

                     }else{

                        MyUtility.showToast("Failed to Submit",AppointmentData.this);
                    }

                }catch (Exception e){


                    e.printStackTrace();

                }
            }

            @Override
            public void onFailed() {

                if(dialog.isShowing()){

                    dialog.dismiss();
                }


                Log.e("Webservice","failed");

            }
        });
        sendRequest.execute(URLListner.MAIN_URL+"User/add_appointment");

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        type= parent.getItemAtPosition(position).toString();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:

                onBackPressed();

                return  true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();


        TimePickerDialog tpd = (TimePickerDialog) getFragmentManager().findFragmentByTag("Timepickerdialog");
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");


        if(tpd != null) tpd.setOnTimeSetListener(this);
        if(dpd != null) dpd.setOnDateSetListener(this);

    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {




        date =dayOfMonth+"-"+(++monthOfYear)+"-"+year;

        Calendar    selCal=Calendar.getInstance();
        selCal.set(year, monthOfYear-1, dayOfMonth);

        Calendar now = Calendar.getInstance();

        TimePickerDialog tpd = TimePickerDialog.newInstance(
                AppointmentData.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        tpd.setThemeDark(true);
        tpd.vibrate(true);
        tpd.dismissOnPause(true);
        tpd.enableSeconds(true);
        tpd.setAccentColor(getResources().getColor(R.color.colorPrimary));
        tpd.setTitle("Select Time");
        tpd.setOnTimeSetListener(this);
        tpd.setCancelable(false);
        tpd.show(getFragmentManager(), "Timepickerdialog");

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {


        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String secondString = minute < 10 ? "0"+second : ""+second;
        time =hourString+":"+minuteString+":"+secondString;


        e1.setText(date+" "+time);


    }

}
