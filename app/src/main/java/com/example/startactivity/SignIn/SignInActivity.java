package com.example.startactivity.SignIn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.startactivity.DBConnection.DB_Query;
import com.example.startactivity.Models.BCrypt;
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
                String pass = password.getText().toString();
                //if(pass.isPasswordCorrect())

               //pobranie hashPassword konta o podanym mailu
                final DB_Query db_query = new DB_Query(getBaseContext());

                final String hashPassword ;

                hashPassword = db_query.getUserHashPassword(email.getText().toString());




                /*
                Thread getHashThread = new Thread() {
                    @Override
                    public void run() {
                         hashPassword[0] = db_query.getUserHashPassword(email.getText().toString());

                    }
                };
                getHashThread.start();
                try{
                    getHashThread.join();
                }
                catch (Exception e){
                    System.out.print(e);
                }*/







                /*if (BCrypt.checkpw(password.getText().toString(), hashPassword[0]))
                    Toast.makeText(SignInActivity.this,"it matches",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(SignInActivity.this,"it does not match",Toast.LENGTH_LONG).show();*/





               //sprawdzenie wprowadzonego hasla z hashem poprawnego hasla

               //jeżeli poprawne pobrać wszystkie dane użytkownika
                //jeżeli nie wyświetlić komunikat

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
