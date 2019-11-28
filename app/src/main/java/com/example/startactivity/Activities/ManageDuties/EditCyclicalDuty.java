package com.example.startactivity.Activities.ManageDuties;

        import android.app.AlertDialog;
        import android.app.ProgressDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.os.AsyncTask;
        import android.support.design.widget.FloatingActionButton;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.CheckBox;
        import android.widget.EditText;
        import android.widget.LinearLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.JsonObjectRequest;
        import com.example.startactivity.Activities.Adapter_user;
        import com.example.startactivity.Common.Common;
        import com.example.startactivity.Common.VolleySingleton;
        import com.example.startactivity.Main.MainActivity;
        import com.example.startactivity.Models.User;
        import com.example.startactivity.R;
        import com.example.startactivity.Shopping.Shopping_fragment;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.List;

public class EditCyclicalDuty extends AppCompatActivity {

    private EditText duty_name;
    private EditText duty_description;
    private RecyclerView groupUsers;

    private TextView monday;
    private CheckBox monday_checkbox;
    private int mondayInt;
    private TextView tuesday;
    private CheckBox tuesday_checkbox;
    private int tuesdayInt;
    private TextView wednesday;
    private CheckBox wednesday_checkbox;
    private int wednesdayInt;
    private TextView thursday;
    private CheckBox thursday_checkbox;
    private int thursdayInt;
    private TextView friday;
    private CheckBox friday_checkbox;
    private int fridayInt;
    private TextView saturday;
    private CheckBox saturday_checkbox;
    private int saturdayInt;
    private TextView sunday;
    private CheckBox sunday_checkbox;
    private int sundayInt;

    private FloatingActionButton updateCyclicalDuty;
    private FloatingActionButton cancel;

    private LinearLayoutManager layoutManager;
    private Adapter_user adapter;

    public List<User> userList = new ArrayList<>();

    private String zadDlaID = "";

    private String name,description, dutyForUserID;
    private int dutyID, dutyMonday,dutyTuesday, dutyWednesday,dutyThursday,dutyFriday,dutySaturday,dutySunday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cyclical_duty);
        setTitle(R.string.Edit_duty);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        monday_checkbox = (CheckBox)findViewById(R.id.monday_checkbox_new_cyclical_duty);
        monday = (TextView)findViewById(R.id.monday_textView_new_cyclical_duty);
        monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(monday_checkbox.isChecked()==false)
                    monday_checkbox.setChecked(true);
                else
                    monday_checkbox.setChecked(false);
            }
        });

        tuesday_checkbox = (CheckBox)findViewById(R.id.tuesday_checkbox_new_cyclical_duty);
        tuesday = (TextView)findViewById(R.id.tuesday_textView_new_cyclical_duty);
        tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tuesday_checkbox.isChecked()==false)
                    tuesday_checkbox.setChecked(true);
                else
                    tuesday_checkbox.setChecked(false);
            }
        });

        wednesday_checkbox = (CheckBox)findViewById(R.id.wednesday_checkbox_new_cyclical_duty);
        wednesday = (TextView)findViewById(R.id.wednesday_textView_new_cyclical_duty);
        wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wednesday_checkbox.isChecked()==false)
                    wednesday_checkbox.setChecked(true);
                else
                    wednesday_checkbox.setChecked(false);
            }
        });

        thursday_checkbox = (CheckBox)findViewById(R.id.thursday_checkbox_new_cyclical_duty);
        thursday = (TextView)findViewById(R.id.thursday_textView_new_cyclical_duty);
        thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(thursday_checkbox.isChecked()==false)
                    thursday_checkbox.setChecked(true);
                else
                    thursday_checkbox.setChecked(false);
            }
        });

        friday_checkbox = (CheckBox)findViewById(R.id.friday_checkbox_new_cyclical_duty);
        friday = (TextView)findViewById(R.id.friday_textView_new_cyclical_duty);
        friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(friday_checkbox.isChecked()==false)
                    friday_checkbox.setChecked(true);
                else
                    friday_checkbox.setChecked(false);
            }
        });

        saturday_checkbox = (CheckBox)findViewById(R.id.saturday_checkbox_new_cyclical_duty);
        saturday = (TextView)findViewById(R.id.saturday_textView_new_cyclical_duty);
        saturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(saturday_checkbox.isChecked()==false)
                    saturday_checkbox.setChecked(true);
                else
                    saturday_checkbox.setChecked(false);
            }
        });

        sunday_checkbox = (CheckBox)findViewById(R.id.sunday_checkbox_new_cyclical_duty);
        sunday = (TextView)findViewById(R.id.sunday_textView_new_cyclical_duty);
        sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sunday_checkbox.isChecked()==false)
                    sunday_checkbox.setChecked(true);
                else
                    sunday_checkbox.setChecked(false);
            }
        });



        groupUsers = (RecyclerView)findViewById(R.id.to_do_for_recycler_cyclical_duty);

        //group users checkboxes list
        layoutManager = new LinearLayoutManager(this, LinearLayout.VERTICAL,false);
        groupUsers.setLayoutManager(layoutManager);
        adapter = new Adapter_user();
        groupUsers.setAdapter(adapter);
        getGroupUsersFromDB();


        duty_name = (EditText)findViewById(R.id.name_editText_new_cyclical_duty);
        duty_description = (EditText)findViewById(R.id.description_editText_new_cyclical_duty);

        cancel = (FloatingActionButton)findViewById(R.id.cancel_duty_floating_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertDialog = new AlertDialog.Builder(EditCyclicalDuty.this)
                        .setIcon(R.drawable.alert)
                        .setTitle("Are you sure to Exit?")
                        .setMessage("New data will not be saved")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what would happen when positive button is clicked
                                finish();
                            }
                        })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what should happen when negative button is clicked
                            }
                        })
                        .show();


            }
        });


        updateCyclicalDuty = (FloatingActionButton)findViewById(R.id.add_cyclical_duty_floating_button);
        updateCyclicalDuty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkInput()==true)
                {
                    createForIDString();

                    new upadateDutyData().execute();
                    Intent intent = new Intent(EditCyclicalDuty.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    //updateCyclicalDutyData();
                }

            }
        });

        loadDutiesData();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            AlertDialog alertDialog = new AlertDialog.Builder(EditCyclicalDuty.this)
                    .setIcon(R.drawable.alert)
                    .setTitle("Are you sure to Exit?")
                    .setMessage("Data will not be saved")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //set what would happen when positive button is clicked
                            finish();
                        }
                    })

                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //set what should happen when negative button is clicked
                        }
                    })
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }


    private void loadDutiesData()
    {
        Bundle extras = getIntent().getExtras();
        dutyID = extras.getInt("dutyID",0);
        name= extras.getString("dutyName");
        description = extras.getString("dutyDesc");
        dutyForUserID = extras.getString("dutyForUserID");
        dutyMonday = extras.getInt("dutyMonday",0);
        dutyTuesday = extras.getInt("dutyTuesday",0);
        dutyWednesday = extras.getInt("dutyWednesday",0);
        dutyThursday = extras.getInt("dutyThursday",0);
        dutyFriday = extras.getInt("dutyFriday",0);
        dutySaturday = extras.getInt("dutySaturday",0);
        dutySunday = extras.getInt("dutySunday",0);
        dutyForUserID = extras.getString("dutyForUserID");


        duty_name.setText(name);
        duty_description.setText(description);
        if(dutyMonday==1){
            monday_checkbox.setChecked(true);
        }
        if(dutyTuesday==1){
            tuesday_checkbox.setChecked(true);
        }
        if(dutyWednesday==1){
            wednesday_checkbox.setChecked(true);
        }
        if(dutyThursday==1){
            thursday_checkbox.setChecked(true);
        }
        if(dutyFriday==1){
            friday_checkbox.setChecked(true);
        }
        if(dutySaturday==1){
            saturday_checkbox.setChecked(true);
        }
        if(dutySunday==1){
            sunday_checkbox.setChecked(true);
        }


    }





    private void createForIDString() {
        //tworzymy stringa z id uzytkownikow dla ktorych przeznaczone jest zadanie
        for(int i=0;i<userList.size();i++)
        {
            if(userList.get(i).getChecked()==true)
            {
                zadDlaID=zadDlaID+String.valueOf(userList.get(i).getUzytkownikID())+" ";
            }
        }
    }




    private boolean checkInput() {

        if(duty_name.getText().toString().trim().length()==0)
            duty_name.setError("This field cannot be empty");



        int user_count = userList.size();
        int checked_users=0;

        for(int i=0;i<user_count;i++)
        {
            if(userList.get(i).getChecked()==true)
            {
                checked_users++;
            }
        }

        int checked_days=0;

        if(monday_checkbox.isChecked()==true)
        {
            mondayInt=1;
            checked_days++;
        }

        if(tuesday_checkbox.isChecked()==true)
        {
            tuesdayInt=1;
            checked_days++;
        }

        if(wednesday_checkbox.isChecked()==true)
        {
            wednesdayInt=1;
            checked_days++;
        }

        if(thursday_checkbox.isChecked()==true)
        {
            thursdayInt=1;
            checked_days++;
        }

        if(friday_checkbox.isChecked()==true)
        {
            fridayInt=1;
            checked_days++;
        }

        if(saturday_checkbox.isChecked()==true)
        {
            saturdayInt=1;
            checked_days++;
        }

        if(sunday_checkbox.isChecked()==true)
        {
            sundayInt=1;
            checked_days++;
        }





        if(duty_name.getText().toString().trim().length()!=0 && checked_users!=0 && checked_days!=0)
            return true;
        else
        {
            if(checked_users==0)
            {
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setIcon(R.drawable.alert)
                        .setTitle("Please select the person")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what would happen when positive button is clicked

                            }
                        })
                        .show();
            }
            return false;

        }


    }

    private void getGroupUsersFromDB() {
        //ustawienie url zgodnego z api
        String url = Common.getUrl()+"getGroupUsers/"+String.valueOf(Common.currentUser.getGroupID());

        //pobranie danych z bazy w formie jsona
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray arry = response.getJSONArray("response");
                            for (int i=0; i<arry.length(); i++)
                            {
                                JSONObject ob = arry.getJSONObject(i);
                                User user = new User(ob.getInt("UserID"),ob.getString("Nick"),i);
                                userList.add(user);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("Error", e.getMessage());
                        }
                        adapter.loadItems(userList);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(EditCyclicalDuty.this, "Connection error", Toast.LENGTH_SHORT).show();
                    }
                });
        VolleySingleton.getInstance(EditCyclicalDuty.this).addToRequestQueue(jsonRequest);
    }


    public class upadateDutyData extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {

            if(duty_description.getText().toString().equals(""))
            {
                Log.d("qwert","empty");
                updateCyclicalDutyDataWithoutDesc();
            }
            else
            {
                Log.d("qwert","not empty");
                updateCyclicalDutyData();
            }

            return null;
        }
    }

    private void updateCyclicalDutyData() {

        String url = Common.getUrl()+"updateCyclicalDutyData/"+String.valueOf(dutyID)+"/"+duty_name.getText().toString().trim()+"/"+
                duty_description.getText().toString().trim()+"/"+String.valueOf(Common.currentUser.getGroupID())+"/"+
                String.valueOf(Common.currentUser.getUzytkownikID())+"/"+zadDlaID+"/"+ mondayInt+"/"+
                tuesdayInt+"/"+wednesdayInt+"/"+thursdayInt+"/"+fridayInt+"/"+saturdayInt+"/"+sundayInt;

        zadDlaID="";

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                        Toast.makeText(EditCyclicalDuty.this, "Connection error", Toast.LENGTH_SHORT ).show();
                        Log.d("Error", error.toString());
                    }

                });
        RequestQueue queue =  VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        queue.add(jsonRequest);

    }

    private void updateCyclicalDutyDataWithoutDesc() {

        String url = Common.getUrl()+"updateCyclicalDutyDataWithoutDesc/"+String.valueOf(dutyID)+"/"+duty_name.getText().toString().trim()+"/"+
                String.valueOf(Common.currentUser.getGroupID())+"/"+
                String.valueOf(Common.currentUser.getUzytkownikID())+"/"+zadDlaID+"/"+ mondayInt+"/"+
                tuesdayInt+"/"+wednesdayInt+"/"+thursdayInt+"/"+fridayInt+"/"+saturdayInt+"/"+sundayInt;

        zadDlaID="";

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(EditCyclicalDuty.this, "Connection error", Toast.LENGTH_SHORT ).show();
                        Log.d("Error", error.toString());
                    }

                });
        RequestQueue queue =  VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        queue.add(jsonRequest);

    }


}
