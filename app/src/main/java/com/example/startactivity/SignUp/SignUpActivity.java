package com.example.startactivity.SignUp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.startactivity.R;

public class SignUpActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText nick;
    private Button sign_up_button;
    private final int min_password_length = 5;

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

                if(isEmailCorrect(email.getText().toString()) && isPasswordCorrect(password.getText().toString()))
                {
                    //registerNewUser();
                }

            }
        });
    }

    private boolean isEmailCorrect(String email_adress) {
        //checking if email_adress contains "@", if no set focusable to email editText
        if(!email_adress.contains("@")){
            Toast.makeText(this,"Invalid email",Toast.LENGTH_LONG).show();
            email.setFocusable(true);
            return false;
        }
        else{
            return true;
        }
    }

    private boolean isPasswordCorrect(String pass) {
        //checking if password have min length and at least one digit
        if(pass.length()>min_password_length && pass.matches(".*\\d+.*")){
            return true;
        }
        else
        {
            Toast.makeText(this,"Invalid password",Toast.LENGTH_LONG).show();
            password.setFocusable(true);
            return false;
        }

    }


}
