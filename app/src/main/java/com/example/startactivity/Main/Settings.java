package com.example.startactivity.Main;

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
import com.example.startactivity.Models.BCrypt;
import com.example.startactivity.Models.Email;
import com.example.startactivity.Models.Password;
import com.example.startactivity.R;
import com.example.startactivity.SignIn.SignInActivity;
import com.example.startactivity.SignUp.SignUpActivity;
import com.example.startactivity.Start.StartActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Settings extends AppCompatActivity {


    private EditText oldPassword;
    private EditText newPassword;
    private EditText repeatNewPassword;
    private EditText newEmail;
    private EditText newNick;

    private Button changePassword;
    private Button changeEmail;
    private Button changeNick;

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        oldPassword = (EditText)findViewById(R.id.oldPassword_editText_settings);
        newPassword = (EditText)findViewById(R.id.newPassword_editText_settings);
        repeatNewPassword = (EditText)findViewById(R.id.repeatNewPssword_editText_settings);
        newEmail = (EditText)findViewById(R.id.newEmail_editText_settings);
        newNick = (EditText)findViewById(R.id.newNick_editText_settings);


        changePassword = (Button)findViewById(R.id.changePassword_button_settings);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserHashPassword(Common.currentUser.getEmail());
            }
        });

        changeEmail = (Button)findViewById(R.id.changeEmail_button_setting);
        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEmail();
            }
        });

        changeNick = (Button)findViewById(R.id.changeNick_button_setting);
        changeNick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNick();
            }
        });

    }


    public String getUserHashPassword(final String email)
    {
        final String[] hashPassword = new String[1];

        //ustawienie url zgodnego z api
        String url = Common.getUrl()+"getUserHashPassword/"+email;

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
                        error.printStackTrace();
                    }
                });

        String hash = hashPassword[0];
        VolleySingleton.getInstance(Settings.this).addToRequestQueue(jsonRequest);

        return hashPassword[0];
    }


    private void isPasswordCorrect(String s) {

        if (BCrypt.checkpw(oldPassword.getText().toString(), s))
        {
            Password password1 = new Password(newPassword.getText().toString());
            Password password2 = new Password(repeatNewPassword.getText().toString());

            if( newPassword.getText().toString().equals(repeatNewPassword.getText().toString()) &&
                    password1.isPasswordCorrect() && password2.isPasswordCorrect())
            {
                String hashedPassword;
                //hashing users password using bcrypt, if contains "/" create new hash because this sign is not acceptable in our api path
                do {
                    hashedPassword = BCrypt.hashpw(newPassword.getText().toString(), BCrypt.gensalt(10));
                }
                while(hashedPassword.contains("/"));

                //update password
                updatePassword(Common.currentUser.getUzytkownikID(),hashedPassword);
            }
            else
            {
                newPassword.setError("Incorrect password");
                repeatNewPassword.setError("Incorrect password");
            }



        }
        else
            oldPassword.setError("Wrong Password");

    }

    private void updatePassword(int uzytkownikID, String hashedPassword) {

        final ProgressDialog mDialog = new ProgressDialog(Settings.this);
        mDialog.setMessage("Please wait...");
        mDialog.show();

        String url = Common.getUrl()+"changePassword/"+Common.currentUser.getUzytkownikID()+"/"+hashedPassword;

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mDialog.dismiss();

                        SharedPreferences preferences = getSharedPreferences("Login", MODE_PRIVATE);
                        preferences.edit().clear().commit();

                        Toast.makeText(Settings.this, R.string.password_changed, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Settings.this, StartActivity.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mDialog.dismiss();
                        error.printStackTrace();
                        Toast.makeText(Settings.this, R.string.toast_connection_error, Toast.LENGTH_SHORT ).show();
                        Log.d("ConnectionError", "Error: " + error.getMessage());
                    }

                });
        RequestQueue queue =  VolleySingleton.getInstance(Settings.this.getApplicationContext()).getRequestQueue();
        queue.add(jsonRequest);

    }

    private void changeEmail() {
        Email email = new Email(newEmail.getText().toString());
        if (email.isEmailCorrect())
        {
            //change emial
            final ProgressDialog mDialog = new ProgressDialog(Settings.this);
            mDialog.setMessage("Please wait...");
            mDialog.show();

            String url = Common.getUrl()+"changeEmail/"+Common.currentUser.getUzytkownikID()+"/"+newEmail.getText().toString();

            JsonObjectRequest jsonRequest = new JsonObjectRequest
                    (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            mDialog.dismiss();
                            Common.currentUser.setEmail(newEmail.getText().toString());

                            mPreferences=getSharedPreferences("Login", MODE_PRIVATE);
                            mEditor=mPreferences.edit();
                            mEditor.putString("Email",Common.currentUser.getEmail());
                            mEditor.commit();

                            Toast.makeText(Settings.this, R.string.email_changed, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Settings.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            mDialog.dismiss();
                            error.printStackTrace();
                            Toast.makeText(Settings.this, R.string.toast_connection_error, Toast.LENGTH_SHORT ).show();
                            Log.d("ConnectionError", "Error: " + error.getMessage());
                        }

                    });
            RequestQueue queue =  VolleySingleton.getInstance(Settings.this.getApplicationContext()).getRequestQueue();
            queue.add(jsonRequest);
        }
        else
        {
            newEmail.setFocusable(true);
            newEmail.setError("New email is incorrect");
        }

    }

    private void changeNick() {
        final ProgressDialog mDialog = new ProgressDialog(Settings.this);
        mDialog.setMessage("Please wait...");
        mDialog.show();

        String url = Common.getUrl()+"changeNick/"+Common.currentUser.getUzytkownikID()+"/"+newNick.getText().toString();

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mDialog.dismiss();
                        Toast.makeText(Settings.this, R.string.nick_changed, Toast.LENGTH_SHORT).show();

                        Common.currentUser.setNick(newNick.getText().toString());

                        mPreferences=getSharedPreferences("Login", MODE_PRIVATE);
                        mEditor=mPreferences.edit();
                        mEditor.putString("Nick",Common.currentUser.getNick());
                        mEditor.commit();

                        Intent intent = new Intent(Settings.this, MainActivity.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mDialog.dismiss();
                        error.printStackTrace();
                        Toast.makeText(Settings.this, R.string.toast_connection_error, Toast.LENGTH_SHORT ).show();
                        Log.d("ConnectionError", "Error: " + error.getMessage());
                    }

                });
        RequestQueue queue =  VolleySingleton.getInstance(Settings.this.getApplicationContext()).getRequestQueue();
        queue.add(jsonRequest);


    }
}
