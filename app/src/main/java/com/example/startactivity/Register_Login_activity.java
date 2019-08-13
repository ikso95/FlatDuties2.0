package com.example.startactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.startactivity.Main.MainActivity;
import com.example.startactivity.SignIn.SignInActivity;
import com.example.startactivity.SignUp.SignUpActivity;
import com.example.startactivity.Start.StartActivity;

public class Register_Login_activity extends AppCompatActivity {


    private Button sign_in;
    private Button sign_up;
    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__login_activity);



        //przejście do ekranu logowania
        sign_in = (Button) findViewById(R.id.sign_in_button_start);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register_Login_activity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        //przejscie do ekranu rejestracji
        sign_up = (Button) findViewById(R.id.sign_up_button_start);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register_Login_activity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        next = (Button)findViewById(R.id.next_button_start);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register_Login_activity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}
