package it.flashlov.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.karshima.flashlove.R;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import it.flashlov.DashboardActivity;
import it.flashlov.PeopleDetailActivity;
import it.flashlov.apicall.GetData;
import it.flashlov.model.UserModel;
import it.flashlov.utility.PreferencesInterface;
import it.flashlov.utility.URLListner;

/**
 * Created by Mahesh on 28/04/16.
 */
public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.CustomViewHolder> {


    private ArrayList<UserModel> slist;
    private Context mycontext;
    private SharedPreferences sharedPreferences;


    public FollowerAdapter(Context context, ArrayList<UserModel> object) {

        this.slist = object;
        this.mycontext = context;
        this.sharedPreferences=mycontext.getSharedPreferences(PreferencesInterface.MyPREFERENCES,Context.MODE_PRIVATE);

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.follower_grid_item, null);
        CustomViewHolder mh = new CustomViewHolder(v);

        return mh;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {

        final UserModel item=slist.get(position);


        if(!item.getImage().equalsIgnoreCase("")){

            Picasso.with(mycontext).load(item.getImage()).resize(100,100).centerCrop().into(holder.user_image);

        }else{

            Picasso.with(mycontext).load(R.drawable.user_placeholder).resize(100,100).centerCrop().into(holder.user_image);
        }

        holder.user_name.setText(item.getName());

        holder.user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // go to people detail view

                Intent i=new Intent(mycontext, PeopleDetailActivity.class);
                i.putExtra("id",item.getId());
                mycontext.startActivity(i);

            }
        });

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog dialog=new ProgressDialog(mycontext);
                dialog.setMessage("Please wait...");
                dialog.show();

                List<NameValuePair> parameters=new ArrayList<>();
                parameters.add(new BasicNameValuePair("profile_id",item.getId()));
                parameters.add(new BasicNameValuePair("viewer_id",sharedPreferences.getString(PreferencesInterface.userId,"")));

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

                                holder.like.setImageResource(R.drawable.heart_fill);

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
                sendRequest.execute(URLListner.MAIN_URL+"User/post_profile_likes");


            }
        });

        holder.star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog dialog=new ProgressDialog(mycontext);
                dialog.setMessage("Please wait...");
                dialog.show();

                List<NameValuePair> parameters=new ArrayList<>();
                parameters.add(new BasicNameValuePair("profile_id",item.getId()));
                parameters.add(new BasicNameValuePair("viewer_id",sharedPreferences.getString(PreferencesInterface.userId,"")));

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

                                holder.star.setImageResource(R.drawable.star_fill);

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
                sendRequest.execute(URLListner.MAIN_URL+"User/profile_preferences");


            }
        });

    }

    @Override
    public int getItemCount() {

        return (null != slist ? slist.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected ImageView user_image;
        protected TextView user_name;
        protected ImageView like,star;
        protected TextView photo_count;

        public CustomViewHolder(View v) {
            super(v);

            this.user_image=(ImageView)v.findViewById(R.id.user_image);
            this.user_name=(TextView)v.findViewById(R.id.user_name);
            this.like=(ImageView)v.findViewById(R.id.like);
            this.star=(ImageView)v.findViewById(R.id.star);
            this.photo_count=(TextView)v.findViewById(R.id.photo_count);

        }


    }



}
