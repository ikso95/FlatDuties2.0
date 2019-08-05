package com.example.startactivity.Models;

import java.util.Date;

public class Duty {

    private int dutyID;
    private String name;
    private String description;
    private Date date_to;
    private int groupID;
    private int uzytkownikID;



    public Duty(int dutyID, String name, String description, Date date_to, int groupID) {
        this.dutyID = dutyID;
        this.name = name;
        this.description = description;
        this.date_to = date_to;
        this.groupID = groupID;
    }

    public Duty(String name, String description, Date date_to, int groupID) {
        this.name = name;
        this.description = description;
        this.date_to = date_to;
        this.groupID = groupID;
    }

    public int getDutyID() {
        return dutyID;
    }

    public void setDutyID(int dutyID) {
        this.dutyID = dutyID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUzytkownikID() {
        return uzytkownikID;
    }

    public void setUzytkownikID(int uzytkownikID) {
        this.uzytkownikID = uzytkownikID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate_to() {
        return date_to;
    }

    public void setDate_to(Date date_to) {
        this.date_to = date_to;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }
}
