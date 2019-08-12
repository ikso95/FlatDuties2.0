package com.example.startactivity.Start;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.startactivity.Common.Common;
import com.example.startactivity.Main.MainActivity;
import com.example.startactivity.Models.User;
import com.example.startactivity.R;
import com.example.startactivity.SignIn.SignInActivity;
import com.example.startactivity.SignUp.SignUpActivity;

public class StartActivity extends AppCompatActivity {

    private Button sign_in;
    private Button sign_up;
    private Button next;
    private ViewPager viewPager;

    private SharedPreferences preferences;
    private int uzytkownikID,grupaID;
    private String mail, nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        isUserLoggedIn();

        //przejście do ekranu logowania
        sign_in = (Button) findViewById(R.id.sign_in_button_start);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        //przejscie do ekranu rejestracji
        sign_up = (Button) findViewById(R.id.sign_up_button_start);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        next = (Button)findViewById(R.id.next_button_start);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        viewPager = (ViewPager)findViewById(R.id.instructions_gallery_viewPager_start);
        ImageAdapter adapter = new ImageAdapter(this);
        viewPager.setAdapter(adapter);

    }

    private void isUserLoggedIn() {
        preferences = getSharedPreferences("Login", MODE_PRIVATE);
        uzytkownikID = preferences.getInt("UserID",0);
        mail=preferences.getString("Email","");
        nick=preferences.getString("Nick","");
        grupaID=preferences.getInt("GroupID",0);

        User u = new User(uzytkownikID,mail, nick, grupaID);
        Common.currentUser=u;

        if(uzytkownikID!=0) //jeżeli zostały odczytane poprawne dane, wartosc 0 jest domyślnie ustawiana, w bazie nie ma takiego rekordu
        {
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(StartActivity.this, "Signed in", Toast.LENGTH_SHORT).show();
        }

    }
}
