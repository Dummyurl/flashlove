package it.flashlov;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.karshima.flashlove.R;

/**
 * Created by Karshima on 1/9/2017.
 */

public class PeopleActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_people);
        setUpViews();
    }

    private void setUpViews() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setTitle("People");



    }
    public void onClick(View v) {

        switch (v.getId()) {

           // case R.id.filterrrr:
               // Intent i=new Intent(PeopleActivity.this,PfilterResultActivity.class);
               // startActivity(i);
                //break;


        }}



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:

                onBackPressed();

                return  true;


        }

        return false;
    }
}

