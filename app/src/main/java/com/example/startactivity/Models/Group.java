package com.example.startactivity.Models;

public class Group {

    private int grupaID;
    private String name;
    private String password;
    private String city;
    private String road;
    private String house_number;
    private String flat_number;


    public Group(int grupaID, String name, String city, String road, String house_number, String flat_number) {
        this.grupaID = grupaID;
        this.name = name;
        this.city = city;
        this.road = road;
        this.house_number = house_number;
        this.flat_number = flat_number;
    }

    public int getGrupaID() {
        return grupaID;
    }

    public void setGrupaID(int grupaID) {
        this.grupaID = grupaID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getHouse_number() {
        return house_number;
    }

    public void setHouse_number(String house_number) {
        this.house_number = house_number;
    }

    public String getFlat_number() {
        return flat_number;
    }

    public void setFlat_number(String flat_number) {
        this.flat_number = flat_number;
    }
}
