package com.example.startactivity.Models;

public class User {

    private int uzytkownikID;
    private String email;
    private String password;
    //private Password password;
    private String nick;
    private int groupID;
    private String groupName;


    public User(int uzytkownikID, String email, String nick, int groupID, String groupName) {
        this.uzytkownikID = uzytkownikID;
        this.email = email;
        this.nick = nick;
        this.groupID = groupID;
        this.groupName = groupName;
    }

    public User(int uzytkownikID, String email, String nick, int groupID) {
        this.uzytkownikID = uzytkownikID;
        this.email = email;
        this.nick = nick;
        this.groupID = groupID;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
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

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int grupaID) {
        this.groupID = grupaID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

}
