package com.example.startactivity.SignUp;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.example.startactivity.DBConnection.DB_Query;
import com.example.startactivity.Models.BCrypt;
import com.example.startactivity.Models.Email;
import com.example.startactivity.Models.Password;
import com.example.startactivity.R;
import com.example.startactivity.SignIn.SignInActivity;

import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText nick;
    private Button sign_up_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = (EditText)findViewById(R.id.email_editText_sign_up);
        password = (EditText)findViewById(R.id.password_editText_sign_up);
        nick = (EditText)findViewById(R.id.nick_editText_sign_up);

        //registration process, check data and if good register
        sign_up_button = (Button)findViewById(R.id.sign_up_button_sign_up);
        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Password pass = new Password(password.getText().toString());
                final Email mail = new Email(email.getText().toString());

                if(mail.isEmailCorrect() && pass.isPasswordCorrect() && nick.getText()!=null)
                {
                    //Toast.makeText(SignUpActivity.this,"Signing up...",Toast.LENGTH_LONG).show();

                    String hashedPassword;
                    //hashing users password using bcrypt, if contains "/" create new hash because this sign is not acceptable in our api path
                    do {
                         hashedPassword = BCrypt.hashpw(pass.getPassword(), BCrypt.gensalt(10));
                    }
                    while(hashedPassword.contains("/"));

                    // register New User
                    registerNewUser(email.getText().toString(),hashedPassword,nick.getText().toString());


                }
                else
                {
                    String message = "Incorrect: ";

                    if(!mail.isEmailCorrect()){message=message + "email ";}
                    if(!pass.isPasswordCorrect()){message = message+ "password ";}
                    if(nick.length()==0){message = message+ "nick";}

                    Toast.makeText(SignUpActivity.this,message,Toast.LENGTH_LONG).show();
                    email.setFocusable(true);
                }

            }
        });
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
