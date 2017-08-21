package it.flashlov;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.karshima.flashlove.R;

import it.flashlov.apicall.GetData;
import it.flashlov.utility.MyUtility;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karshima on 1/2/2017.
 */

public class ForgotActivity extends AppCompatActivity implements  View.OnClickListener{

    private EditText e1;
    private Button btn;
  private Toolbar toolbar;
    String myemaile;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_activity);
        setUpview();

    }


    private void setUpview() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Forgot Password");

        e1 = (EditText) findViewById(R.id.enterp);

        btn = (Button) findViewById(R.id.submit);
        btn.setOnClickListener(this);


    }
    public void onClick(View v){


        switch (v.getId()){
            case  R.id.submit:


                myemaile = e1.getText().toString().trim();


                if (myemaile.equalsIgnoreCase("")) {

                    Toast.makeText(ForgotActivity.this, "Enter Email", Toast.LENGTH_LONG).show();

                }else {

                    //Toast.makeText(ForgotActivity.this, "new user?", Toast.LENGTH_LONG).show();

                    forgot();

                }

        }
    }


    private void forgot() {


        Log.e("AT","onForgot");


        //thats all foe webservice calling and data fetching

        //you can show progress dialog before webservice execution
        //Let RUN the project

        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.show();


        //here we need to send parameters like email,phone name etc

        List<NameValuePair> parameters=new ArrayList<>();


        parameters.add(new BasicNameValuePair("email",myemaile));


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

                        MyUtility.showToast("Forgot your password...Reset here",ForgotActivity.this);
                        startActivity(new Intent(ForgotActivity.this,LoginActivity.class));


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
        sendRequest.execute("http://flashlov.ccube9gs.com/app/User/forgot_password");

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


}

