package it.flashlov;

/**
 * Created by Karshima on 1/17/2017.
 */


    import android.app.ProgressDialog;
    import android.content.Context;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.os.Bundle;
    import android.support.design.widget.FloatingActionButton;
    import android.support.v7.app.AppCompatActivity;
    import android.support.v7.widget.Toolbar;
    import android.util.Log;
    import android.view.View;
    import android.view.Menu;
    import android.view.MenuItem;

    import com.example.karshima.flashlove.R;

    import it.flashlov.apicall.GetData;
    import it.flashlov.utility.MyUtility;
    import it.flashlov.utility.PreferencesInterface;

    import org.apache.http.NameValuePair;
    import org.apache.http.message.BasicNameValuePair;
    import org.json.JSONArray;
    import org.json.JSONObject;

    import java.util.ArrayList;
    import java.util.List;

public class ProfileActivity extends AppCompatActivity {


    String name,email,phone,age,gender;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.user_profile);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   Intent intent=new Intent(ProfileActivity.this,DashboardActivity.class);
                    startActivity(intent);
                }
            });

            userprofile();
        }

        private void userprofile() {




            Log.e("AT","onUserProfile");


            //thats all foe webservice calling and data fetching

            //you can show progress dialog before webservice execution
            //Let RUN the project

            final ProgressDialog dialog=new ProgressDialog(this);
            dialog.setMessage("Please wait...");
            dialog.show();


            //here we need to send parameters like email,phone name etc

            SharedPreferences pref=getSharedPreferences(PreferencesInterface.MyPREFERENCES, Context.MODE_PRIVATE);


            List<NameValuePair> parameters=new ArrayList<>();

            parameters.add(new BasicNameValuePair("user_id",pref.getString(PreferencesInterface.userId,"")));


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

                    try {

                        int status = json.getInt("status");


                        //webservice working successfully here you have to read all data coming from webservice
                        //if status ==1 then redirect user to login

                        if (status == 1) {

                            JSONArray jsonArray = json.getJSONArray("profile_info");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject c =jsonArray.getJSONObject(i);
                                SharedPreferences pref=getSharedPreferences(PreferencesInterface.MyPREFERENCES,Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor=pref.edit();
                                editor.putString(PreferencesInterface.userId,c.getString("Id"));
                                editor.putString(PreferencesInterface.Name,c.getString("name"));
                                editor.putString(PreferencesInterface.Email,c.getString("email"));
                               editor.commit();
                                if(!pref.getString(PreferencesInterface.userId,"").equalsIgnoreCase("")) {

                                    //here you need to access data like login activity
                                    //and set to your editext

                                    MyUtility.showToast("User Profile", ProfileActivity.this);
                                    //startActivity(new Intent(ProfileActivity.this,DashboardActivity.class));
                                }


                            }
                        }

                            Log.e("STATUS", status + "");

                        }catch(Exception e){


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
            sendRequest.execute("http://flashlov.ccube9gs.com/app/User/get_user_profile");



        }














        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_profile, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            switch (item.getItemId()){

                case android.R.id.home:

                    onBackPressed();

                    return  true;
            }

            return super.onOptionsItemSelected(item);
        }
    }


