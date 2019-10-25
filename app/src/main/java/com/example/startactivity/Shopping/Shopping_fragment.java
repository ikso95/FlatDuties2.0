package com.example.startactivity.Shopping;

import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.startactivity.Activities.Activities_Adapter;
import com.example.startactivity.Activities.Activity_fragment;
import com.example.startactivity.Activities.ListItem;
import com.example.startactivity.Common.Common;
import com.example.startactivity.Common.VolleySingleton;
import com.example.startactivity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Shopping_fragment extends Fragment {

    private FloatingActionButton plus_fab;
    private FloatingActionButton send_fab;
    private EditText item_name_input;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    public List<ShoppingItem> listItems;
    public ShoppingItem listItem;

    private Shopping_Adapter adapter;

    private boolean first_run=true;
    private int liczba;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.shopping_fragment,container,false);

        plus_fab= (FloatingActionButton)view.findViewById(R.id.new_item_floating_action_button_shopping_fragment);
        send_fab = (FloatingActionButton)view.findViewById(R.id.send_new_shopping_item_button_shopping_fragment);
        item_name_input = (EditText)view.findViewById(R.id.shopping_item_input_editText_shopping_fragment);

        listItems = new ArrayList<>();

        recyclerView = (RecyclerView)view.findViewById(R.id.shopping_recyclerView_shopping_fragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));


        refresh();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if(dy > 5){
                    if(send_fab.getVisibility()==View.VISIBLE)
                    {
                        plus_fab.hide();
                        plus_fab.animate().rotationBy(135.0f).setDuration(500);
                        plus_fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary,null)));
                        plus_fab.animate().translationX(0);
                        send_fab.hide();
                        item_name_input.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        plus_fab.hide();
                    }

                } else if(dy<-5){
                    plus_fab.show();
                }


                //if cant scroll down, list reaches top show our dab
                if(!recyclerView.canScrollVertically(-1))
                {
                    plus_fab.show();
                }

                super.onScrolled(recyclerView, dx, dy);
            }
        });


        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_shopping_fragment);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        liczba=listItems.size(); //liczba dla notification
                        listItems.clear();
                        new Shopping_fragment.listUpdate().execute();
                    }
                },500);
            }
        });


        send_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item_name_input.getText().toString().trim().length()==0)
                    item_name_input.setError("This field cannot be empty");
                else
                {
                    new Shopping_fragment.AddShoppingItem().doInBackground();

                    item_name_input.setText("");
                    item_name_input.setVisibility(View.INVISIBLE);
                    send_fab.hide();


                    plus_fab.animate().rotationBy(135.0f).setDuration(500);
                    plus_fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary,null)));
                    plus_fab.animate().translationX(0);
                }

            }
        });

        plus_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item_name_input.isShown()==false)
                {
                    item_name_input.setVisibility(View.VISIBLE);
                    item_name_input.requestFocus();
                    send_fab.show();

                    plus_fab.animate().rotationBy(-135.0f).setDuration(500);
                    plus_fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent,null)));
                    plus_fab.animate().translationX(-170);

                }
                else {
                    item_name_input.setVisibility(View.INVISIBLE);
                    item_name_input.setText("");
                    item_name_input.setError(null);
                    send_fab.hide();

                    plus_fab.animate().rotationBy(135.0f).setDuration(500);
                    plus_fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary,null)));
                    plus_fab.animate().translationX(0);

                }
            }
        });



        return view;
    }

    public void refresh(){

        final Handler refreshHandler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {


                if(first_run==false)
                {
                    liczba=listItems.size();
                }

                listItems.clear();
                new listUpdate().execute();


                //odswizanie co ... sekund
                refreshHandler.postDelayed(this,  20*1000);
            }
        };
        //czas pierwszego wykonania funkcji run()
        refreshHandler.postDelayed(runnable, 0);
    }

    //async task for updating  duties list
    public class listUpdate extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {

            getShoppingData();
            return null;
        }
    }


    public void getShoppingData()
    {
        //ustawienie url zgodnego z api
        String url = Common.getUrl()+"getShoppingItemData/"+String.valueOf(Common.currentUser.getGroupID());

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


                                listItem = new ShoppingItem(ob.getInt("ShoppingID"),ob.getInt("GroupID"),ob.getString("Name"));

                                listItems.add(listItem);

                            }
                            adapter = new Shopping_Adapter(listItems,getContext());
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("Error", e.getMessage());
                        }




                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonRequest);
    }



    public class AddShoppingItem extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            addNewShoppingItemToDB();
            return null;
        }
    }

    private void addNewShoppingItemToDB() {
        /*final ProgressDialog mDialog = new ProgressDialog(getContext());
        mDialog.setMessage("Please wait...");
        mDialog.show();*/

        String url = Common.getUrl()+"postShoppingItem/"+item_name_input.getText().toString().trim()+"/"+
                String.valueOf(Common.currentUser.getGroupID());


        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        listItems.add(new ShoppingItem());  //dodanie nowego obiektu żeby liczba się zgadzała, i nie było notofication rzy dodawaniu własnego duty
                        liczba=listItems.size();
                        //mDialog.dismiss();
                        listItems.clear();
                        new Shopping_fragment.listUpdate().execute();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //mDialog.dismiss();
                        error.printStackTrace();
                        Toast.makeText(getContext(), "Connection error", Toast.LENGTH_SHORT ).show();
                        Log.d("Error", error.toString());
                    }

                });
        RequestQueue queue =  VolleySingleton.getInstance(getContext()).getRequestQueue();
        queue.add(jsonRequest);


    }



}
