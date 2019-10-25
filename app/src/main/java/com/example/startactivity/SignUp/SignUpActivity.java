package com.example.startactivity.SignUp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.startactivity.Activities.NewCyclicalDuty;
import com.example.startactivity.Common.Common;
import com.example.startactivity.Common.VolleySingleton;
import com.example.startactivity.Models.BCrypt;
import com.example.startactivity.Models.Email;
import com.example.startactivity.Models.Password;
import com.example.startactivity.R;
import com.example.startactivity.SignIn.SignInActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/*
sprawdzam czy email zawiera @, czy haslo ma 6 znakow w tym 1 liczbe i czy wprowadzone hasla sa identyczne
jezeli tak
    hashuje haslo
    sprawdzam czy w bazie nie ma uzytkownika o podanym email
    jezeli nie
        rejestruje nowego uzytkownika
    jezeli tak
        zwracam informacje ze email jest w uzyciu
jezeli nie
    informuje o bledach

*/
public class SignUpActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText repeat_password;
    private EditText nick;
    private Button sign_up_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle(R.string.Sign_Up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        email = (EditText)findViewById(R.id.email_editText_sign_up);
        password = (EditText)findViewById(R.id.password_editText_sign_up);
        repeat_password = (EditText)findViewById(R.id.repeated_password_edittext_sign_up);
        nick = (EditText)findViewById(R.id.nick_editText_sign_up);

        //registration process, check data and if good register
        sign_up_button = (Button)findViewById(R.id.sign_up_button_sign_up);
        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Password pass = new Password(password.getText().toString());
                Password rpass = new Password(repeat_password.getText().toString());
                final Email mail = new Email(email.getText().toString().trim());

                if(mail.isEmailCorrect() && pass.isPasswordCorrect() && rpass.isPasswordCorrect()
                        && nick.getText()!=null && password.getText().toString().equals(repeat_password.getText().toString()))
                {
                    //Toast.makeText(SignUpActivity.this,"Signing up...",Toast.LENGTH_LONG).show();

                    String hashedPassword;
                    //hashing users password using bcrypt, if contains "/" create new hash because this sign is not acceptable in our api path
                    do {
                         hashedPassword = BCrypt.hashpw(pass.getPassword(), BCrypt.gensalt(10));
                    }
                    while(hashedPassword.contains("/"));

                    // register New User

                    isUserRegistered(hashedPassword);



                }
                else
                {

                    if(!mail.isEmailCorrect()){email.setError("Incorrect Email");}
                    if(!pass.isPasswordCorrect()){password.setError("Incorrect Password");}
                    if(!rpass.isPasswordCorrect()){repeat_password.setError("Incorrect Password");}
                    if(!password.getText().toString().equals(repeat_password.getText().toString()))
                    {
                        password.setError("Passwords do not match");
                        repeat_password.setError("Passwords do not match");
                    }
                    if(nick.length()==0){nick.setError("Incorrect Nick");}

                    email.setFocusable(true);
                }

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void isUserRegistered(final String hashedPassword) {

        //ustawienie url zgodnego z api
        String url = Common.getUrl()+"isUserRegistered2/"+email.getText().toString().trim();

        //pobranie danych z bazy w formie jsona
        final JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("response");
                            JSONObject jsonObject = jsonArray.getJSONObject(0);

                            int i = jsonObject.getInt("COUNT(UserID)");
                            if(i==0)
                            {
                                registerNewUser(email.getText().toString().trim(),hashedPassword,nick.getText().toString().trim());
                            }
                            else
                            {
                                email.setError("Email is in use");
                            }

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


        VolleySingleton.getInstance(SignUpActivity.this).addToRequestQueue(jsonRequest);

    }


    public void registerNewUser(String mail, String hashedPassword, String nick){
        final ProgressDialog mDialog = new ProgressDialog(SignUpActivity.this);
        mDialog.setMessage("Please wait...");
        mDialog.show();

        String url = Common.getUrl()+"signup/"+mail+"/"+hashedPassword+"/"+nick;

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, R.string.toast_user_registered, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mDialog.dismiss();
                        error.printStackTrace();
                        Toast.makeText(SignUpActivity.this, R.string.toast_connection_error, Toast.LENGTH_SHORT ).show();
                        Log.d("ConnectionError", "Error: " + error.getMessage());
                    }

                });
        RequestQueue queue =  VolleySingleton.getInstance(SignUpActivity.this.getApplicationContext()).getRequestQueue();
        queue.add(jsonRequest);
    }

}
