package com.example.startactivity.Models;

import android.widget.Toast;

import java.util.Random;

public class Password {

    private String password;
    private String hashedPassword;

    private final int min_password_length = 6;

    public Password(String pass)
    {
        this.password=pass;
    }

    public Password()
    {
        Random random = new Random();
        String tempPassword = String.valueOf(random.nextInt(899999)+100000);
        this.password=tempPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public boolean isPasswordCorrect() {
        //checking if password have min length and at least one digit
        if(password.length()>=min_password_length && password.matches(".*\\d+.*")){
            return true;
        }
        else
        {
            return false;
        }
    }


    public void hashPassword(){

        //hashing users password using bcrypt, if contains "/" create new hash because this sign is not acceptable in our api path
        do {
            hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
        }
        while(hashedPassword.contains("/"));

    }


    public String hashPassword(String password){

        //hashing users password using bcrypt, if contains "/" create new hash because this sign is not acceptable in our api path
        do {
            hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
        }
        while(hashedPassword.contains("/"));

        return hashedPassword;
    }

    public boolean doPasswordMatchHashPassword(String password, String hashedPassword)
    {
        if (BCrypt.checkpw(password, hashedPassword))
            return true;
        else
            return false;
    }



}
