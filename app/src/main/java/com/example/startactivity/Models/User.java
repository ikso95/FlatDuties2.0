package com.example.startactivity.Models;

public class User {

    private int uzytkownikID;
    private String email;
    private String password;
    private String nick;
    private int grupaID;


    public User(int uzytkownikID, String email, String password, String nick, int grupaID) {
        this.uzytkownikID = uzytkownikID;
        this.email = email;
        this.password = password;
        this.nick = nick;
        this.grupaID = grupaID;
    }


    public int getUzytkownikID() {
        return uzytkownikID;
    }

    public void setUzytkownikID(int uzytkownikID) {
        this.uzytkownikID = uzytkownikID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getGrupaID() {
        return grupaID;
    }

    public void setGrupaID(int grupaID) {
        this.grupaID = grupaID;
    }

}
