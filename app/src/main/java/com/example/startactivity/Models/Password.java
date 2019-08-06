package com.example.startactivity.Models;

import android.widget.Toast;

public class Password {



    private String password;
    private final int min_password_length = 5;

    public Password(String pass)
    {
        this.password=pass;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isPasswordCorrect() {
        //checking if password have min length and at least one digit
        if(password.length()>min_password_length && password.matches(".*\\d+.*")){
            return true;
        }
        else
        {
            //Toast.makeText(this,"Invalid password",Toast.LENGTH_LONG).show();
            //password.setFocusable(true);
            return false;
        }

    }


}
