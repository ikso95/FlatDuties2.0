package com.example.startactivity.SignIn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.startactivity.Models.Email;
import com.example.startactivity.Models.Password;
import com.example.startactivity.R;
import com.example.startactivity.SignUp.SignUpActivity;

public class SignInActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button sign_in_button;
    private Button forgot_password_button;

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
                Password pass = new Password(password.getText().toString());
                //if(pass.isPasswordCorrect())
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
}
