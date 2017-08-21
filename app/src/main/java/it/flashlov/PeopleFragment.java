package it.flashlov;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.karshima.flashlove.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.flashlov.adapter.FollowerAdapter;
import it.flashlov.apicall.JSONParser;
import it.flashlov.model.UserModel;
import it.flashlov.utility.GridSpacingItemDecoration;
import it.flashlov.utility.MyUtility;
import it.flashlov.utility.PreferencesInterface;
import it.flashlov.utility.RecyclerItemClickListener;
import it.flashlov.utility.URLListner;


/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleFragment extends Fragment {


    public PeopleFragment() {
    }


    private View rootView;
    private ArrayList<UserModel> userdataRandom=new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private String query="";

    private String gender="",sex="",distance="",order="",country="",city="";

    RecyclerView list;
    FollowerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_people, container, false);
        setUpViews();
        return rootView;
    }

    private void setUpViews() {

        sharedPreferences=getActivity().getSharedPreferences(PreferencesInterface.MyPREFERENCES, Context.MODE_PRIVATE);


        if(!userdataRandom.isEmpty()){

            userdataRandom.clear();
        }


        if(MyUtility.isConnected(getActivity())){

            new GetRandomUser().execute();


        }else {

            MyUtility.showToast(MyUtility.INTERNET_ERROR,getActivity());
        }


    }




    private void setUpRecycleView() {

        list =(RecyclerView)rootView.findViewById(R.id.list);
        list.setLayoutManager(new GridLayoutManager(getActivity(),2));
        list.setPadding(list.getPaddingLeft(), list.getPaddingTop(),list.getPaddingRight(), list.getPaddingBottom());
        int spanCount = 2; // 3 columns
        int spacing = 10; // 50px
        boolean includeEdge = true;
        list.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        adapter = new FollowerAdapter(getActivity(), userdataRandom);
        list.setAdapter(adapter);
        list.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {




            }
        }));


    }

    private class GetRandomUser extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;
        private JSONParser jsonParser=new JSONParser();
        private int success=0;
        private JSONArray vjsonArray;


        @Override
        protected void onPreExecute() {

            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Please wait...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(String... urls) {


            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("start", "0"));
            params.add(new BasicNameValuePair("limit", "200"));


            try {

                JSONObject json=null;

                json = jsonParser.makeHttpRequest("http://flashlov.ccube9gs.com/app/User/people", "POST", params);

                Log.d("Login Details: ", json.toString());

                success = json.getInt("status");

                if(success==1){

                    userdataRandom=new ArrayList<>();
                    JSONArray  vjsonArray=json.getJSONArray("people");

                    for(int i=0;i<vjsonArray.length();i++){

                        JSONObject jsonObject=vjsonArray.getJSONObject(i);

                        UserModel model=new UserModel();

                        model.setName(jsonObject.getString("full_name"));
                        model.setId(jsonObject.getString("id"));
                        model.setImage(jsonObject.getString("profile_picture"));

                        userdataRandom.add(model);
                    }

                }


                return true;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        }

        protected void onPostExecute(Boolean result) {


            if (dialog.isShowing()) {

                dialog.dismiss();
            }


            if (success == 1) {

                setUpRecycleView();

            } else {

                MyUtility.showToast("Failed to load data", getActivity());

            }
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.people_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_filter:

                Intent i=new Intent(getActivity(),PfilterResultActivity.class);
                startActivityForResult(i, 100);

                return true;
        }

        return  false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==getActivity().RESULT_OK){

            if(requestCode==100){


                 gender=data.getStringExtra("gender");
                sex=data.getStringExtra("sex");
                distance=data.getStringExtra("diastance");
                order=data.getStringExtra("order_by");
                country=data.getStringExtra("country");
                city=data.getStringExtra("city");

                if(MyUtility.isConnected(getActivity())){

                    if(!userdataRandom.isEmpty()){

                        userdataRandom.clear();
                    }

                    adapter.notifyDataSetChanged();
                    new GetFilterPeople().execute();
                }

                Log.e("FILTER",gender);

            }

        }


    }



    private class GetFilterPeople extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;
        private JSONParser jsonParser=new JSONParser();
        private int success=0;
        private JSONArray vjsonArray;


        @Override
        protected void onPreExecute() {

            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Please wait...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(String... urls) {


            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_id", sharedPreferences.getString(PreferencesInterface.userId,"")));
            params.add(new BasicNameValuePair("gender",gender ));
            params.add(new BasicNameValuePair("sexual_Orientation", sex));
            params.add(new BasicNameValuePair("country", country));
            params.add(new BasicNameValuePair("city", city));
            params.add(new BasicNameValuePair("diastance", distance));
            params.add(new BasicNameValuePair("order_by", order));
            params.add(new BasicNameValuePair("start", "0"));
            params.add(new BasicNameValuePair("limit", "200"));


            try {

                JSONObject json=null;

                json = jsonParser.makeHttpRequest(URLListner.MAIN_URL+"User/filter", "POST", params);

                Log.d("Login Details: ", json.toString());

                success = json.getInt("status");

                if(success==1){

                    userdataRandom=new ArrayList<>();
                    JSONArray  vjsonArray=json.getJSONArray("filter_data");

                    for(int i=0;i<vjsonArray.length();i++){

                        JSONObject jsonObject=vjsonArray.getJSONObject(i);

                        UserModel model=new UserModel();

                        model.setName(jsonObject.getString("full_name"));
                        model.setId(jsonObject.getString("id"));
                        model.setImage(jsonObject.getString("profile_picture"));

                        userdataRandom.add(model);
                    }

                }


                return true;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        }

        protected void onPostExecute(Boolean result) {


            if (dialog.isShowing()) {

                dialog.dismiss();
            }


            if (success == 1) {

               // adapter.notifyDataSetChanged();

                FollowerAdapter adapter = new FollowerAdapter(getActivity(), userdataRandom);
                list.setAdapter(adapter);
                list.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {


                    }
                }));

            } else {

                MyUtility.showToast("Failed to load data", getActivity());

            }
        }
    }

}
