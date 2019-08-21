package com.example.startactivity.SignIn;

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
import com.example.startactivity.Add_or_join_group.First_use_add_or_join_group;
import com.example.startactivity.Common.Common;
import com.example.startactivity.Common.VolleySingleton;
import com.example.startactivity.Main.MainActivity;
import com.example.startactivity.Models.BCrypt;
import com.example.startactivity.Models.Email;
import com.example.startactivity.Models.Password;
import com.example.startactivity.Models.User;
import com.example.startactivity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/*
po kliknieciu zaloguj pobieram z bazy hash hasła przypisanego do podanego emaila
porównuje z hashem wpisanego przez urzytkownika hasła
jezeli jest ok pobieram reszte danych z bazy zapisuje do sharedprefferences i loguje


*/
public class SignInActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button sign_in_button;
    private Button forgot_password_button;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    public ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        setTitle(R.string.Sign_In);

        email = (EditText)findViewById(R.id.email_editText_sign_in);
        password = (EditText)findViewById(R.id.password_editText_sign_in);

        sign_in_button = (Button)findViewById(R.id.sign_in_button_sign_in);
        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               //pobranie hashPassword konta o podanym mailu
                getUserHashPassword(email.getText().toString().trim());

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
                    mDialog = new ProgressDialog(SignInActivity.this);
                    mDialog.setMessage("Please wait...");
                    mDialog.show();

                    //czy istenieje taki adres email w bazie
                    isUserRegistered();
                    //generowanie nowego hasla
                    //przypisanie do maila nowego hasla
                    //wyslanie maila z nowym haslem

                }
             }});

    }

    private void isUserRegistered() {

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
                                email.setError("No account with this email");
                            }
                            else
                            {
                                generateNewTempPassword();
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


        VolleySingleton.getInstance(SignInActivity.this).addToRequestQueue(jsonRequest);

    }

    private void generateNewTempPassword() {

        Password pass = new Password();

        pass.hashPassword();

        Toast.makeText(SignInActivity.this,pass.getHashedPassword(),Toast.LENGTH_LONG).show();

        updateNewPasswordToDB(pass.getHashedPassword(),pass.getPassword());

    }

    private void updateNewPasswordToDB(final String hashedPassword, final String tempPassword) {
        String url = Common.getUrl()+"changePassword2/"+email.getText().toString().trim()+"/"+hashedPassword;

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        sendEmail(tempPassword);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                        Log.d("ConnectionError", "Error: " + error.getMessage());
                    }

                });
        RequestQueue queue =  VolleySingleton.getInstance(SignInActivity.this.getApplicationContext()).getRequestQueue();
        queue.add(jsonRequest);
    }

    private void sendEmail(final String tempPassword) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender("lisuoskar@gmail.com",
                            "Defekacja2");
                    sender.sendMail(getBaseContext().getString(R.string.Email_title),               //title
                            getBaseContext().getString(R.string.Email_body)+tempPassword,    //body message
                            "lisuoskar@gmail.com",                                           //sender
                            email.getText().toString().trim());                                     //recipent
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }
        }).start();
        mDialog.dismiss();
        Toast.makeText(SignInActivity.this,getBaseContext().getString(R.string.forget_password_toast)+email.getText().toString().trim(),Toast.LENGTH_LONG).show();
    }


    public void getUserHashPassword(final String email)
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


        VolleySingleton.getInstance(SignInActivity.this).addToRequestQueue(jsonRequest);
    }

    private void isPasswordCorrect(String s) {

        if (BCrypt.checkpw(password.getText().toString(), s))
        {
            signIn();
        }
        else
            password.setError("Wrong Password");

    }

    private void signIn() {

        //ustawienie url zgodnego z api
        String url = Common.getUrl()+"signIn/"+email.getText().toString().trim();

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
                            String groupName = jsonObject.getString("Name");


                            Common.currentUser = new User(userID,mail,nick,groupID,groupName);

                            mPreferences=getSharedPreferences("Login", MODE_PRIVATE);
                            mEditor=mPreferences.edit();

                            mEditor.putInt("UserID",Common.currentUser.getUzytkownikID());
                            mEditor.putString("Email",Common.currentUser.getEmail());
                            mEditor.putString("Nick",Common.currentUser.getNick());
                            mEditor.putInt("GroupID",Common.currentUser.getGroupID());
                            mEditor.putString("GroupName",Common.currentUser.getGroupName());
                            //zapisanie danych
                            mEditor.commit();

                            Toast.makeText(SignInActivity.this, "Log in succesful", Toast.LENGTH_SHORT).show();

                            //jezeli pierwsze logowanie przenies do activity wyboru grupy
                            if(Common.currentUser.getGroupID()==0)
                            {
                                Intent intent = new Intent(SignInActivity.this, First_use_add_or_join_group.class);
                                startActivity(intent);
                                finish();
                            }
                            else    //jezeli juz ma przypisana grupe kontynuuj normalnie
                            {
                                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
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

        VolleySingleton.getInstance(SignInActivity.this).addToRequestQueue(jsonRequest);

    }
}


