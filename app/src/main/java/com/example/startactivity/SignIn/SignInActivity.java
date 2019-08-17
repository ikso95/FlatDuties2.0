package com.example.startactivity.SignIn;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.startactivity.Common.Common;
import com.example.startactivity.Common.VolleySingleton;
import com.example.startactivity.DBConnection.DB_Query;
import com.example.startactivity.Main.MainActivity;
import com.example.startactivity.Models.BCrypt;
import com.example.startactivity.Models.Email;
import com.example.startactivity.Models.Password;
import com.example.startactivity.Models.User;
import com.example.startactivity.R;
import com.example.startactivity.SignUp.SignUpActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SignInActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button sign_in_button;
    private Button forgot_password_button;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        email = (EditText)findViewById(R.id.email_editText_sign_in);
        password = (EditText)findViewById(R.id.password_editText_sign_in);

        sign_in_button = (Button)findViewById(R.id.sign_in_button_sign_in);
        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               //pobranie hashPassword konta o podanym mailu
                getUserHashPassword(email.getText().toString());

            }
        });

        forgot_password_button = (Button)findViewById(R.id.forgot_password_button_sign_in);
        forgot_password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sprawdzenie czy jest wpisany email
                Email mail = new Email(email.getText().toString());
                if (!mail.isEmailCorrect())
                {
                    Toast.makeText(SignInActivity.this,"Enter correct email",Toast.LENGTH_LONG).show();
                    email.setFocusable(true);
                }
                else
                {
                    //wysłać maila z możliwośćią resetu hasła
                }
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
        VolleySingleton.getInstance(SignInActivity.this).addToRequestQueue(jsonRequest);

        return hashPassword[0];
    }

    private void isPasswordCorrect(String s) {

        if (BCrypt.checkpw(password.getText().toString(), s))
        {
            signIn(email.getText().toString());
        }
        else
            password.setError("Wrong Password");

    }

    private void signIn(String email) {

        //ustawienie url zgodnego z api
        String url = Common.getUrl()+"signIn/"+email;

        //pobranie danych z bazy w formie jsona
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("response");
                            JSONObject jsonObject = jsonArray.getJSONObject(0);

                            int userID = jsonObject.getInt("UserID");
                            String mail = jsonObject.getString("Email");
                            String nick = jsonObject.getString("Nick");
                            int groupID = jsonObject.getInt("GroupID");

                            Common.currentUser = new User(userID,mail,nick,groupID);

                            mPreferences=getSharedPreferences("Login", MODE_PRIVATE);
                            mEditor=mPreferences.edit();

                            mEditor.putInt("UserID",Common.currentUser.getUzytkownikID());
                            mEditor.putString("Email",Common.currentUser.getEmail());
                            mEditor.putString("Nick",Common.currentUser.getNick());
                            mEditor.putInt("GroupID",Common.currentUser.getGroupID());
                            //zapisanie danych
                            mEditor.commit();

                            Toast.makeText(SignInActivity.this, "Log in succesful", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(intent);

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

        VolleySingleton.getInstance(SignInActivity.this).addToRequestQueue(jsonRequest);

    }
}
