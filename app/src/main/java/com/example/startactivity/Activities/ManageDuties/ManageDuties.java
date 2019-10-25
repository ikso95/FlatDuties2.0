package com.example.startactivity.Activities.ManageDuties;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.startactivity.Activities.Activities_Adapter;
import com.example.startactivity.Activities.Activity_fragment;
import com.example.startactivity.Activities.ListItem;
import com.example.startactivity.Common.Common;
import com.example.startactivity.Common.VolleySingleton;
import com.example.startactivity.R;
import com.example.startactivity.Shopping.ShoppingItem;
import com.example.startactivity.Shopping.Shopping_Adapter;
import com.example.startactivity.Shopping.Shopping_fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ManageDuties extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    public List<ListItem> listItems;
    public ListItem listItem;
    private ManageDuties_adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_duties);
        setTitle(R.string.manage_duties);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listItems = new ArrayList<>();

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh_manage_duties);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        //liczba=listItems.size(); //liczba dla notification
                        listItems.clear();
                        new ManageDuties.getCyclicalDuties().execute();
                    }
                },500);
            }
        });


        recyclerView = (RecyclerView)findViewById(R.id.cyclical_duties_recyclerView_manage_duties);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new ManageDuties.getCyclicalDuties().execute();


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getCyclicalDuties()
    {
        //ustawienie url zgodnego z api
        String url = Common.getUrl()+"getCyclicalDuties/"+String.valueOf(Common.currentUser.getGroupID());

        //pobranie danych z bazy w formie jsona
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray arry = response.getJSONArray("response");


                            for (int i=0; i<arry.length(); i++)
                            {
                                JSONObject ob = arry.getJSONObject(i);


                                if(!ob.isNull("Description"))
                                {
                                    listItem = new ListItem(ob.getInt("DutyID"),ob.getString("Name"),ob.getString("Description"),
                                            ob.getInt("GroupID"),ob.getInt("UserID"),ob.getString("ForUserID"),ob.getInt("Monday"),
                                            ob.getInt("Tuesday"),ob.getInt("Wednesday"),ob.getInt("Thursday"),ob.getInt("Friday"),
                                            ob.getInt("Saturday"),ob.getInt("Sunday"));
                                }
                                else
                                {
                                    listItem = new ListItem(ob.getInt("DutyID"),ob.getString("Name"),
                                            ob.getInt("GroupID"),ob.getInt("UserID"),ob.getString("ForUserID"),ob.getInt("Monday"),
                                            ob.getInt("Tuesday"),ob.getInt("Wednesday"),ob.getInt("Thursday"),ob.getInt("Friday"),
                                            ob.getInt("Saturday"),ob.getInt("Sunday"));
                                }
                                listItems.add(listItem);

                            }
                            //adapter = new Shopping_Adapter(listItems,getContext());
                            //recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("Error", e.getMessage());
                        }

                        adapter = new ManageDuties_adapter(listItems,getBaseContext());
                        recyclerView.setAdapter(adapter);


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        VolleySingleton.getInstance(this).addToRequestQueue(jsonRequest);
    }


    //async task for updating  duties list
    public class getCyclicalDuties extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {

            getCyclicalDuties();
            return null;
        }
    }




}
