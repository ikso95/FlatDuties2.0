package com.example.startactivity.Activities;

public class ListItem {

    private int dutyId;
    private String duty_name;
    private String duty_description;
    private int groupID;
    private int fromUserID;
    private String forUserID;
    private int monday,tuesday,wednesday,thursday,friday,saturday,sunday;


    public ListItem(int dutyId, String duty_name, String duty_description, int groupID, int fromUserID, String forUserID, int monday, int tuesday, int wednesday, int thursday, int friday, int saturday, int sunday) {
        this.dutyId = dutyId;
        this.duty_name = duty_name;
        this.duty_description = duty_description;
        this.groupID = groupID;
        this.fromUserID = fromUserID;
        this.forUserID = forUserID;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }


    public ListItem(int dutyId, String duty_name, int groupID, int fromUserID, String forUserID, int monday, int tuesday, int wednesday, int thursday, int friday, int saturday, int sunday) {
        this.dutyId = dutyId;
        this.duty_name = duty_name;
        this.groupID = groupID;
        this.fromUserID = fromUserID;
        this.forUserID = forUserID;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }


    public ListItem(int dutyId, String duty_name, int groupID, int fromUserID) {
        this.dutyId = dutyId;
        this.duty_name = duty_name;
        this.groupID = groupID;
        this.fromUserID = fromUserID;
    }




    public int getDutyId() {
        return dutyId;
    }

    public String getDuty_name() {
        return duty_name;
    }

    public String getDuty_description() {
        return duty_description;
    }

    public int getGroupID() {
        return groupID;
    }

    public int getFromUserID() {
        return fromUserID;
    }

    public String getForUserID() {
        return forUserID;
    }

    public int getMonday() {
        return monday;
    }

    public int getTuesday() {
        return tuesday;
    }

    public int getWednesday() {
        return wednesday;
    }

    public int getThursday() {
        return thursday;
    }

    public int getFriday() {
        return friday;
    }

    public int getSaturday() {
        return saturday;
    }

    public int getSunday() {
        return sunday;
    }
}
