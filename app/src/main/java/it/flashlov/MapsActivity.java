package it.flashlov;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.karshima.flashlove.R;

import it.flashlov.apicall.GetData;
import it.flashlov.model.Appointment;
import it.flashlov.utility.GeocodingLocation;
import it.flashlov.utility.MyLocation;
import it.flashlov.utility.MyUtility;
import it.flashlov.utility.PreferencesInterface;
import it.flashlov.utility.URLListner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity  implements OnMapReadyCallback,View.OnClickListener{

    private GoogleMap mMap;
    private Button search;
    private MyLocation myLocation;
    private double latitude;
    private double longitude;

    private static final String TAG_RESULTS = "results";
    private static final String TAG_GEOMETRY = "geometry";
    private static final String TAG_VIEWPORT = "viewport";
    private static final String TAG_NORTHEAST = "northeast";
    private static final String TAG_LAT = "lat";
    private static final String TAG_LNG = "lng";

    private ArrayList<Appointment> data=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_maps);

        if(!data.isEmpty()){

            data.clear();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setUpview();
    }

    private void setUpview() {

        Toolbar  toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Appointment");


        search= (Button) findViewById(R.id.search);
        search.setOnClickListener(this);



    }
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.search:

                MyUtility.hideKeyboard(v,MapsActivity.this);
                onMapSearch(v);

                break;

        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng pune=null;

        myLocation = new MyLocation(MapsActivity.this);

        // check if GPS enabled
        if(myLocation.canGetLocation()){

             latitude = myLocation.getLatitude();
             longitude = myLocation.getLongitude();

            if(latitude==0){

                  latitude=18.519069;
                  longitude=73.852665;
                 pune = new LatLng(latitude, longitude);

            }else{
                 pune = new LatLng(latitude,longitude);
            }

            mMap.addMarker(new MarkerOptions().position(pune).title("Current Location"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pune, 12.0f));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                @Override
                public boolean onMarkerClick(Marker arg0) {

                    Log.e("onMarkerClick", arg0.getTitle());

                    return true;
                }

            });

            if(!data.isEmpty()){

                data.clear();
            }

            getData();


        }else{

            myLocation.showSettingsAlert();
        }





    }


    public void onMapSearch(View view) {

        EditText locationSearch = (EditText) findViewById(R.id.editsearch);
        String address=locationSearch.getText().toString();
        address = address.replaceAll(" ", "%20");


        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.show();

        List<NameValuePair> parameters=new ArrayList<>();
        parameters.add(new BasicNameValuePair("email", ""));

        GetData sendRequest=new GetData(parameters);
        sendRequest.setResultListener(new GetData.ResultListner() {
            @Override
            public void onSuccess(JSONObject json) {

                if (dialog.isShowing()) {

                    dialog.dismiss();
                }

                //this method call if webservice success

                try {
                    Log.e("JSON", json + "");

                    JSONArray results = json.getJSONArray(TAG_RESULTS);


                    for (int i = 0; i < results.length(); i++) {

                        JSONObject r = results.getJSONObject(i);
                        JSONObject geometry = r.getJSONObject(TAG_GEOMETRY);
                        JSONObject viewport = geometry.getJSONObject(TAG_VIEWPORT);
                        JSONObject northest = viewport.getJSONObject(TAG_NORTHEAST);

                        String lat = northest.getString(TAG_LAT);
                        String lng = northest.getString(TAG_LNG);

                        Log.e("lat", lat + "");
                        Log.e("lng", lng + "");

                        latitude = Double.parseDouble(lat);
                        longitude = Double.parseDouble(lng);

                        mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))).title(r.getString("formatted_address")));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)), 12.0f));


                        if (!data.isEmpty()) {

                            data.clear();
                        }

                        getData();

                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailed() {

                if (dialog.isShowing()) {

                    dialog.dismiss();
                }


                Log.e("Webservice", "failed");

            }
        });
        sendRequest.execute("http://maps.googleapis.com/maps/api/geocode/json?address="+address+"&sensor=true");

        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      // return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.map_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_launch:

                Log.e("LAT",String.valueOf(latitude));
                Log.e("LNG",String.valueOf(longitude));

                Intent i=new Intent(MapsActivity.this,AppointmentData.class);
                i.putExtra("lat",String.valueOf(latitude));
                i.putExtra("long",String.valueOf(longitude));
                startActivity(i);

                return  true;

            case android.R.id.home:

                onBackPressed();

                return  true;
        }

        return  false;
    }


    private void getData() {

        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("Getting data...");
        dialog.show();

        List<NameValuePair> parameters=new ArrayList<>();
        parameters.add(new BasicNameValuePair("base_lat",String.valueOf(latitude)));
        parameters.add(new BasicNameValuePair("base_lng", String.valueOf(longitude)));
        GetData sendRequest=new GetData(parameters);
        sendRequest.setResultListener(new GetData.ResultListner() {
            @Override
            public void onSuccess(JSONObject json) {

                if (dialog.isShowing()) {

                    dialog.dismiss();
                }

                try {

                    int status = json.getInt("status");

                    Log.e("APPO", json + "");

                    if (status == 1) {

                        JSONArray jsonArray = json.getJSONArray("nearst_cities");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject c = jsonArray.getJSONObject(i);

                            Appointment appo = new Appointment();

                            appo.setAppo_id(c.getString("id"));
                            appo.setUser_id(c.getString("user_id"));
                            appo.setDatetime(c.getString("dataeora"));
                            appo.setType(c.getString("tipo_appuntamento"));
                            appo.setName(c.getString("indirizzo"));
                            appo.setMsg(c.getString("messaggio"));
                            appo.setLat(c.getDouble("latitudine"));
                            appo.setLng(c.getDouble("longitudine"));

                            data.add(appo);
                        }

                        if (!data.isEmpty()) {

                            for (Appointment a : data) {

                                mMap.addMarker(new MarkerOptions().position(new LatLng(a.getLat(), a.getLng())).title(a.getDatetime()));

                            }


                        }

                    }

                } catch (Exception e) {



                    e.printStackTrace();

                }
            }

            @Override
            public void onFailed() {

                if (dialog.isShowing()) {

                    dialog.dismiss();
                }


                Log.e("Webservice", "failed");

            }
        });
        sendRequest.execute(URLListner.MAIN_URL + "User/nearst_cities");

    }




}
