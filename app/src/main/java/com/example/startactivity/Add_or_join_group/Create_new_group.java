package com.example.startactivity.Add_or_join_group;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.startactivity.Models.User;
import com.example.startactivity.R;
import com.example.startactivity.SignIn.SignInActivity;
import com.example.startactivity.SignUp.SignUpActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
sprawdzam czy wszystkie inputy sa ok,
czy haslo ma min 6 znakow w tym 1 cyfre, czy sa ze soba zgodne
sprawdzam czy nazwa wybrana przez uzytkownika jest wolna jak tak to
hasuje haslo
dodaje grupe do bazy w zaleznosc jakie pola sa wypelnione
    nazwa,haslo
    nazwa,haslo,miasto,ulica,nr domu
    nazwa,haslo,miasto,ulica,nr domu, nr mieszkania
przypisuje grupe do obecnego uzytkownika i aktualizuje grupaID uzytkownika z baza danych

*/
public class Create_new_group extends AppCompatActivity {

    private EditText group_name;
    private EditText group_password;
    private EditText group_repeated_password;

    private Button create_and_join_group;

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private ProgressDialog mDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_group);
        setTitle(R.string.Create_and_join);


        group_name = (EditText)findViewById(R.id.new_group_name_editText_group);
        group_password = (EditText)findViewById(R.id.new_group_password_editText_group);
        group_repeated_password = (EditText)findViewById(R.id.repeat_new_group_password_editText_group);


        create_and_join_group = (Button)findViewById(R.id.create_and_join_button_group);

        create_and_join_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog = new ProgressDialog(Create_new_group.this);
                mDialog.setMessage("Please wait...");
                mDialog.show();

                if(checkAllInputs()==true)
                {
                    isGroupNameAvailable();
                }
            }
        });


    }



    private boolean checkAllInputs() {

        int suma_zlych=0;

        //czy wszystkie inputy są wypełnione
        if(group_name.getText().toString().length()==0)
        {
            group_name.setError("This field cannot be empty");
            suma_zlych++;
        }
        if(group_password.getText().toString().length()==0)
        {
            group_password.setError("This field cannot be empty");
            suma_zlych++;
        }
        if(group_repeated_password.getText().toString().length()==0)
        {
            group_repeated_password.setError("This field cannot be empty");
            suma_zlych++;
        }

        if(new Password((group_password.getText().toString())).isPasswordCorrect()==false)
        {
            group_password.setError("Incorrect password");
            suma_zlych++;
        }
        if(new Password((group_repeated_password.getText().toString())).isPasswordCorrect()==false)
        {
            group_repeated_password.setError("Incorrect password");
            suma_zlych++;
        }
        if(!group_password.getText().toString().equals(group_repeated_password.getText().toString()))
        {
            group_password.setError("Passwords do not match");
            group_repeated_password.setError("Passwords do not match");
            suma_zlych++;
        }


        if(suma_zlych==0)
        {
            return true;
        }
        else
        {
            mDialog.dismiss();
            return false;
        }
    }

    private void isGroupNameAvailable()
    {
        //ustawienie url zgodnego z api
        String url = Common.getUrl()+"isGroupNameAvailable/"+group_name.getText().toString().trim();

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

                            if(nrOfGroupsWithThisName==0)
                            {
                                hashPassword();
                            }
                            else
                            {
                                group_name.setError("This name is occupied, please try with another one");
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

        VolleySingleton.getInstance(Create_new_group.this).addToRequestQueue(jsonRequest);

    }

    private void hashPassword()
    {
        String hashedPassword;
        //hashing users password using bcrypt, if contains "/" create new hash because this sign is not acceptable in our api path
        do {
            hashedPassword = BCrypt.hashpw(group_password.getText().toString(), BCrypt.gensalt(10));
        }
        while(hashedPassword.contains("/"));


            create_and_join_group(hashedPassword);


    }




    private void create_and_join_group(String password) {

        String url = Common.getUrl()+"createNewGroup/"+group_name.getText().toString().trim()+"/"+password;

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(Create_new_group.this, R.string.new_group_created, Toast.LENGTH_SHORT).show();
                        join_group();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mDialog.dismiss();
                        error.printStackTrace();
                        Toast.makeText(Create_new_group.this, R.string.toast_connection_error, Toast.LENGTH_SHORT ).show();
                        Log.d("ConnectionError", "Error: " + error.getMessage());
                    }

                });
        RequestQueue queue =  VolleySingleton.getInstance(Create_new_group.this.getApplicationContext()).getRequestQueue();
        queue.add(jsonRequest);
    }


    private void join_group() {

        //ustawienie url zgodnego z api
        String url = Common.getUrl()+"joinGroup/"+group_name.getText().toString().trim();

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
                            mEditor.putString("GroupName",Common.currentUser.getGroupName());
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

        VolleySingleton.getInstance(Create_new_group.this).addToRequestQueue(jsonRequest);


    }

    private void updateGroupIDToUserDB(int groupID) {

        String url = Common.getUrl()+"updateUserGroupID/"+Common.currentUser.getUzytkownikID()+"/"+groupID;

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mDialog.dismiss();
                        Toast.makeText(Create_new_group.this, R.string.new_group_created, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Create_new_group.this,MainActivity.class);
                        startActivity(intent);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mDialog.dismiss();
                        error.printStackTrace();
                        Toast.makeText(Create_new_group.this, R.string.toast_connection_error, Toast.LENGTH_SHORT ).show();
                        Log.d("ConnectionError", "Error: " + error.getMessage());
                    }

                });
        RequestQueue queue =  VolleySingleton.getInstance(Create_new_group.this.getApplicationContext()).getRequestQueue();
        queue.add(jsonRequest);


    }


}
