package com.example.startactivity.SignUp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.startactivity.Models.Email;
import com.example.startactivity.Models.Password;
import com.example.startactivity.R;

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
                Email mail = new Email(email.getText().toString());

                if(mail.isEmailCorrect() && pass.isPasswordCorrect() && nick.getText()!=null)
                {
                    Toast.makeText(SignUpActivity.this,"Signing up...",Toast.LENGTH_LONG).show();
                    //registerNewUser();
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




}
