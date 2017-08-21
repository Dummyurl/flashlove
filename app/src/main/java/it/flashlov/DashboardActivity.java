package it.flashlov;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.karshima.flashlove.R;

import it.flashlov.utility.MyUtility;
import it.flashlov.utility.PreferencesInterface;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG ="TAG";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("People");

         sharedPreferences = getSharedPreferences(PreferencesInterface.MyPREFERENCES, Context.MODE_PRIVATE);

       // MyUtility.showToast(sharedPreferences.getString(PreferencesInterface.Name, ""), this);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        PeopleFragment peopleFragment=new PeopleFragment();
        FragmentManager manager=getSupportFragmentManager();
        manager.beginTransaction().replace(R.id .relativelayout_for_fragment,peopleFragment,peopleFragment.getTag()).commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
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
            Intent intentt = new Intent(DashboardActivity.this,
                    ProfileActivity.class);
            startActivity(intentt);
        } else {
            if (id == R.id.logout) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Log.d(TAG, "Now log out and start the activity login");
                Intent intent = new Intent(DashboardActivity.this,
                        LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        }



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.people) {
             PeopleFragment peopleFragment=new PeopleFragment();
            FragmentManager manager=getSupportFragmentManager();
            manager.beginTransaction().replace(R.id .relativelayout_for_fragment,peopleFragment,peopleFragment.getTag()).commit();
            // Handle the camera action
        } else if (id == R.id.love_game) {

        } else if (id == R.id.appoinment) {


            startActivity(new Intent(DashboardActivity.this,MapsActivity.class));

           // MapsActivity appointFragment=new MapsActivity();
           // FragmentManager manager=getSupportFragmentManager();
           // manager.beginTransaction().replace(R.id .relativelayout_for_fragment,appointFragment,appointFragment.getTag()).commit();

        } else if (id == R.id.messages) {

        } else if (id == R.id.profilev) {

        } else if (id == R.id.profilel) {

        }else  if (id == R.id.liked) {

        }else  if (id == R.id.prefferd){

        }else  if (id == R.id.upgrades){

        }else  if (id == R.id.friends){

        }else  if (id == R.id.setting){

        }else  if (id == R.id.contacts){

        }else  if (id == R.id.trms){

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
