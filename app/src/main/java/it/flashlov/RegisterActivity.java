package it.flashlov;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.karshima.flashlove.R;

import it.flashlov.apicall.GetData;
import it.flashlov.utility.MyUtility;
import it.flashlov.utility.Validations;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static it.flashlov.utility.PreferencesInterface.Email;
import static it.flashlov.utility.PreferencesInterface.MyPREFERENCES;

/**
 * Created by Karshima on 1/2/2017.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;


    private EditText age,gender,Ename,Eemail,Ephone,Epassword;
    String d_name;
    String d_pass;
    String d_phone;
    String d_email;

    private Button register;
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Register");

        setUpViews();

    }




    private void setUpViews() {
        // TODO Auto-generated method stub

        age = (EditText) findViewById(R.id.spinner);
        gender = (EditText) findViewById(R.id.spinner1);

        age.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                if (MotionEvent.ACTION_UP == event.getAction()) {

                    ageDialog();
                }


                return true;
            }
        });


        gender.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                if (MotionEvent.ACTION_UP == event.getAction()) {

                    genderDialog();
                }


                return true;
            }
        });

        gender.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {

                    genderDialog();

                }

            }
        });

        age.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {

                    ageDialog();

                }
            }
        });


        Ename = (EditText) findViewById(R.id.name);

        Eemail = (EditText) findViewById(R.id.email);
        Epassword = (EditText) findViewById(R.id.pass);
        Ephone = (EditText) findViewById(R.id.phone);

        register = (Button) findViewById(R.id.regi);
        register.setOnClickListener(this);


    }

    private void ageDialog(){


        final String[] items = new String[92];

        for (int i = 0; i < 92; i++) {

            items[i] = String.valueOf(i + 18);
        }

        AlertDialog.Builder ad = new AlertDialog.Builder(RegisterActivity.this);
        ad.setTitle("Select Age");
        ad.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                age.setText(items[which]);
            }
        });
        ad.show();

    }

    private void genderDialog(){

        final String[] items = new String[]{"Male", "Female"};


        AlertDialog.Builder ad = new AlertDialog.Builder(RegisterActivity.this);
        ad.setTitle("Select Gender");
        ad.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                gender.setText(items[which]);
            }
        });
        ad.show();
    }




    public void onClick(View v) {
        switch (v.getId()){
            case R.id.regi:
                if(Ename.getText().toString().length()==0){
                    Ename.setError("First name not entered");
                    Ename.requestFocus();



                }else if(Eemail.getText().toString().length()==0) {
                    Eemail.setError("Email is not entered");
                    Ename.requestFocus();



                }else if (Epassword.getText().toString().length()==0){
                    Epassword.setError("Password is not entered");
                    Epassword.requestFocus();

                }


                else if(Epassword.getText().toString().length()<8) {
                    Epassword.setError("Password should be atleast of 8 characters");
                    Epassword.requestFocus();
                }
                else if(age.getText().toString().length()==0){
                    age.setError("Age is not selected");
                    age.requestFocus();

                }else if(gender.getText().toString().length()==0){
                    gender.setError("Gender is not selected");
                    gender.requestFocus();


                }else if(Ephone.getText().toString().length()!=10){
                    Ephone.setError("Invalid Phone Number");
                    Ephone.requestFocus();

                }else if(Ephone.getText().toString().length()>10){
                    Ephone.setError("Invalid Number");
                    Ephone.requestFocus();

                }else if(MyUtility.isConnected(this)){
                    register();

                }else
                    MyUtility.showToast(MyUtility.INTERNET_ERROR,this);
                break;
        }

    }




    private void register() {

        Log.e("AT","onRegister");


        //thats all foe webservice calling and data fetching

        //you can show progress dialog before webservice execution
        //Let RUN the project

        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.show();


        //here we need to send parameters like email,phone name etc

        List<NameValuePair> parameters=new ArrayList<>();
        parameters.add(new BasicNameValuePair("name",d_name));
        parameters.add(new BasicNameValuePair("email",d_email));
        parameters.add(new BasicNameValuePair("phone",d_phone));
        parameters.add(new BasicNameValuePair("password",d_pass));

        //have you understand?s
        //okay let call our GetData calls pass all info

        GetData sendRequest=new GetData(parameters);
        sendRequest.setResultListener(new GetData.ResultListner() {
            @Override
            public void onSuccess(JSONObject json) {

                if(dialog.isShowing()){

                    dialog.dismiss();
                }

                //this method call if webservice success

                try{

                    int status=json.getInt("status");

                    //webservice working successfully here you have to read all data coming from webservice
                    //if status ==1 then redirect user to login

                    if(status==1){

                        MyUtility.showToast("Registered Successfully...Login here",RegisterActivity.this);
                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));


                    }

                    Log.e("STATUS",status+"");

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
        sendRequest.execute("http://flashlov.ccube9gs.com/app/User/register_customer");


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:


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

            onBackPressed();

        }
        return super.onKeyDown(keyCode, event);
    }




    /********************UPLOAD FILES TO SERVER**********************/

}
