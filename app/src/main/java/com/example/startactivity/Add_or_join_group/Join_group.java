package com.example.startactivity.Add_or_join_group;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.startactivity.Common.Common;
import com.example.startactivity.Common.VolleySingleton;
import com.example.startactivity.Main.MainActivity;
import com.example.startactivity.Models.BCrypt;
import com.example.startactivity.Models.Password;
import com.example.startactivity.R;
import com.example.startactivity.SignIn.SignInActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Join_group extends AppCompatActivity {

    private EditText group_name;
    private EditText password1;
    private EditText city;
    private EditText street;
    private EditText houseNr;
    private EditText flatNr;
    private EditText password2;

    private Button join1;
    private Button join2;

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
        setTitle(R.string.Join_group);

        group_name = (EditText)findViewById(R.id.group_name_editText_join_group);
        password1 = (EditText)findViewById(R.id.group_password_editText_join_group);
        join1 = (Button)findViewById(R.id.join_group_button_join_group);

        join1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(group_name.getText().toString().length()!=0 && new Password(password1.getText().toString()).isPasswordCorrect()==true)
                {
                    mDialog = new ProgressDialog(Join_group.this);
                    mDialog.setMessage("Please wait...");
                    mDialog.show();
                    //if group name exist
                    ifGroupNameExists();
                    //check password
                    //join if password correct
                }
                else
                {
                    if(group_name.getText().toString().length()==0)
                    {
                        group_name.setError("This field cannot be empty");
                    }
                    if(new Password(password1.getText().toString()).isPasswordCorrect()==false)
                    {
                        password1.setError("Wrong password");
                    }
                }
            }
        });

        city = (EditText)findViewById(R.id.city_editText_join_group);
        street = (EditText)findViewById(R.id.street_editText_join_group);
        houseNr = (EditText)findViewById(R.id.house_nr_editText_join_group);
        flatNr = (EditText)findViewById(R.id.flat_number_editText_join_group);
        password2 = (EditText)findViewById(R.id.group_password_2_editText_join_group);
        join2 = (Button)findViewById(R.id.join_2_button_join_group);

        join2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


    private void ifGroupNameExists() {

        //ustawienie url zgodnego z api
        String url = Common.getUrl()+"isGroupNameAvailable/"+group_name.getText().toString();

        //pobranie danych z bazy w formie jsona
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("response");
                            JSONObject jsonObject = jsonArray.getJSONObject(0);

                            int nrOfGroupsWithThisName = jsonObject.getInt("COUNT(GroupID)");

                            if(nrOfGroupsWithThisName!=0)
                            {
                                hashPassword();
                            }
                            else
                            {
                                group_name.setError("There is no group named like this");
                                mDialog.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("Error", e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mDialog.dismiss();
                        error.printStackTrace();
                    }
                });

        VolleySingleton.getInstance(Join_group.this).addToRequestQueue(jsonRequest);
    }

    private void hashPassword() {
        String hashedPassword;
        //hashing users password using bcrypt, if contains "/" create new hash because this sign is not acceptable in our api path
        do {
            hashedPassword = BCrypt.hashpw(password1.getText().toString(), BCrypt.gensalt(10));
        }
        while(hashedPassword.contains("/"));

        checkPassword();
    }

    private void checkPassword() {

        final String[] hashPassword = new String[1];

        //ustawienie url zgodnego z api
        String url = Common.getUrl()+"getGroupHashPassword/"+group_name.getText().toString();

        //pobranie danych z bazy w formie jsona
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("response");
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            hashPassword[0] = jsonObject.getString("Password");
                            //Toast.makeText(context,hashPassword[0],Toast.LENGTH_LONG).show();

                            isPasswordCorrect(hashPassword[0]);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("Error", e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mDialog.dismiss();
                        error.printStackTrace();
                    }
                });


        VolleySingleton.getInstance(Join_group.this).addToRequestQueue(jsonRequest);

    }

    private void isPasswordCorrect(String s) {


        if (BCrypt.checkpw(password1.getText().toString(), s))
        {
            joinGroup();
        }
        else
        {
            mDialog.dismiss();
            password1.setError("Wrong Password");
        }


    }

    private void joinGroup() {

        //ustawienie url zgodnego z api
        String url = Common.getUrl()+"joinGroup/"+group_name.getText().toString();

        //pobranie danych z bazy w formie jsona
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("response");
                            JSONObject jsonObject = jsonArray.getJSONObject(0);

                            int groupID = jsonObject.getInt("GroupID");

                            Common.currentUser.setGroupID(groupID);
                            Common.currentUser.setGroupName(group_name.getText().toString());

                            mPreferences=getSharedPreferences("Login", MODE_PRIVATE);
                            mEditor=mPreferences.edit();
                            mEditor.putInt("GroupID",Common.currentUser.getGroupID());
                            //zapisanie danych
                            mEditor.commit();

                            updateGroupIDToUserDB(groupID);

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

        VolleySingleton.getInstance(Join_group.this).addToRequestQueue(jsonRequest);
    }

    private void updateGroupIDToUserDB(int groupID) {

        String url = Common.getUrl()+"updateUserGroupID/"+Common.currentUser.getUzytkownikID()+"/"+groupID;

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mDialog.dismiss();
                        //Toast.makeText(Join_group.this, R.string.new_group_created, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Join_group.this, MainActivity.class);
                        startActivity(intent);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mDialog.dismiss();
                        error.printStackTrace();
                        Toast.makeText(Join_group.this, R.string.toast_connection_error, Toast.LENGTH_SHORT ).show();
                        Log.d("ConnectionError", "Error: " + error.getMessage());
                    }

                });
        RequestQueue queue =  VolleySingleton.getInstance(Join_group.this.getApplicationContext()).getRequestQueue();
        queue.add(jsonRequest);


    }


}
