package it.flashlov;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.karshima.flashlove.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.flashlov.apicall.GetData;
import it.flashlov.model.UserModel;
import it.flashlov.utility.PreferencesInterface;
import it.flashlov.utility.RoundedImageView;

import static com.example.karshima.flashlove.R.styleable.View;
import static it.flashlov.utility.PreferencesInterface.userId;

/**
 * Created by Mahesh on 02/02/17.
 */
public class PeopleDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar;
    private Button btn,btn2;
    //private View rootView;
    private ArrayList<UserModel> userdataRandom=new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private String query="";
    private RoundedImageView img;
    String profile_id;
    String userId;

    private TextView username;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people_view);
        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        if(bundle != null) {
            userId = bundle.getString("userId");
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Friend Profile");
        btn=(Button) findViewById(R.id.request);
        btn.setOnClickListener(this);
        btn2=(Button) findViewById(R.id.messageeee);
        btn2.setOnClickListener(this);
        peopleview();
    }

    private void peopleview() {
        Log.e("AT","onPeople");



        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.show();


        //here we need to send parameters like email,phone name etc

        List<NameValuePair> parameters=new ArrayList<>();

        parameters.add(new BasicNameValuePair("profile_id",profile_id));


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

                    Log.e("STATUS",status+"");

                    if(status==1){

                        JSONArray jsonArray=json.getJSONArray("Data found!!");

                        for(int i=0;i<jsonArray.length();i++){

                            JSONObject c=jsonArray.getJSONObject(i);

                            SharedPreferences pref=getSharedPreferences(PreferencesInterface.MyPREFERENCES, Context.MODE_PRIVATE);

                            //below code isused to store data in session
                            SharedPreferences.Editor editor=pref.edit();

                            editor.putString(PreferencesInterface.userId,c.getString("id"));
                            editor.putString(PreferencesInterface.Name,c.getString("full_name"));
                            editor.putString(PreferencesInterface.Email,c.getString("email"));


                            //you can whole parameter if required

                            editor.commit();


                            if(!pref.getString(PreferencesInterface.userId,"").equalsIgnoreCase("")){

                                //the go to you main activiyt...

                                //which one is main activity.. dashboard is mainactiivty after login...rightright

                                startActivity(new Intent(PeopleDetailActivity.this,PeopleDetailActivity.class));

                            }


                        }

                    }


                    //status 1 means you logged successfully
                    //after successfully login you have stored data in session
                    //so no need to login every time
                    //for storing data in seesion for android we used SharedPreferences class
                    //better to read about sharedpreferences class

                    //open postman

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
        sendRequest.execute("http://flashlov.ccube9gs.com/app/User/get_profile_views");

    }


    @Override
    public void onClick(View v) {
    }
}
