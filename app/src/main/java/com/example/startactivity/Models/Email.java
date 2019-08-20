package com.example.startactivity.Models;

import android.widget.Toast;

public class Email {

    private String email;

    public Email(String email){
        this.email=email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailCorrect() {
        //checking if email_adress contains "@", if no set focusable to email editText
        if(!email.contains("@")){
            return false;
        }
        else{
            return true;
        }
    }
}
