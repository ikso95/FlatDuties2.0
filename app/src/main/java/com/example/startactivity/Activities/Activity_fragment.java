package com.example.startactivity.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.startactivity.Common.Common;
import com.example.startactivity.Common.VolleySingleton;
import com.example.startactivity.Main.MainActivity;
import com.example.startactivity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLDisplay;


public class Activity_fragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Activities_Adapter adapter;

    private EditText duty_name;
    //private ImageButton add_duty;
    private FloatingActionButton add_duty;

    public List<ListItem> listItems;
    public ListItem listItem;

    private FloatingActionButton floatingActionButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.activity_fragment,container,false);

        recyclerView = (RecyclerView) view.findViewById(R.id.duties_recyclerView_duty_fragment);
        //every item of recycler view have fixed size, zmienia wymiary dostosowywujac sie do zawartosci
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));

        listItems = new ArrayList<>();

        getDutiesData();

        //hide fab when scrolling down
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if(dy > 5){
                    floatingActionButton.hide();
                } else if(dy<-5){
                    floatingActionButton.show();
                }

                super.onScrolled(recyclerView, dx, dy);
            }
        });


        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_duty_fragment);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //listItems.clear();
                //getActivitiesData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        listItems.clear();
                        getDutiesData();
                    }
                },500);
            }
        });

        duty_name = (EditText)view.findViewById(R.id.duty_input_editText_duty_fragment);
        add_duty = (FloatingActionButton) view.findViewById(R.id.send_new_duty_button_activity_fragment);

        add_duty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (duty_name.getText().toString().trim().length()==0)
                    duty_name.setError("This field cannot be empty");
                else
                {
                    addNewDutyToDB();
                    duty_name.setText("");

                    duty_name.setVisibility(View.INVISIBLE);
                    add_duty.hide();


                    floatingActionButton.animate().rotationBy(135.0f).setDuration(500);
                    floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary,null)));
                    floatingActionButton.animate().translationX(0);
                }

            }
        });


        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(duty_name.isShown()==false)
                {
                    duty_name.setVisibility(View.VISIBLE);
                    add_duty.show();
                    floatingActionButton.animate().rotationBy(-135.0f).setDuration(500);
                    floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent,null)));
                    floatingActionButton.animate().translationX(-170);

                }
                else {
                    duty_name.setVisibility(View.INVISIBLE);
                    add_duty.hide();
                    duty_name.setText("");
                    duty_name.setError(null);


                    floatingActionButton.animate().rotationBy(135.0f).setDuration(500);
                    floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary,null)));
                    floatingActionButton.animate().translationX(0);

                }
            }
        });








        return view;
    }

    private void addNewDutyToDB() {
        final ProgressDialog mDialog = new ProgressDialog(getContext());
        mDialog.setMessage("Please wait...");
        mDialog.show();

        String url = Common.getUrl()+"addNewDuty/"+duty_name.getText().toString().trim()+"/"+
                String.valueOf(Common.currentUser.getGroupID())+"/"+String.valueOf(Common.currentUser.getUzytkownikID());



        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mDialog.dismiss();
                        error.printStackTrace();
                        Toast.makeText(getContext(), "Connection error", Toast.LENGTH_SHORT ).show();
                        Log.d("Error", error.toString());
                    }

                });
        RequestQueue queue =  VolleySingleton.getInstance(getContext()).getRequestQueue();
        queue.add(jsonRequest);


    }

    public void getDutiesData()
    {
        //ustawienie url zgodnego z api
        String url = Common.getUrl()+"getDutiesData/"+String.valueOf(Common.currentUser.getGroupID());

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

                                if(ob.isNull("Monday"))
                                {
                                    listItem = new ListItem(ob.getInt("DutyID"),ob.getString("Name"),ob.getInt("GroupID"),ob.getInt("UserID"));
                                }
                                else if(!ob.isNull("Description"))
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

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("Error", e.getMessage());
                        }

                        //
                        adapter = new Activities_Adapter(listItems,getContext());
                        recyclerView.setAdapter(adapter);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonRequest);
    }




}
