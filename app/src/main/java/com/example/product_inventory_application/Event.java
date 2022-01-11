package com.example.product_inventory_application;

import java.sql.Time;

public class Event {
    String name;
    String action;
    String time;
    String user;

    public Event (String name, String action, String time, String user) {
        this.name = name;
        this.action = action;
        this.time = time;
        this.user = user;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getAction () {
        return action;
    }

    public void setAction (String action) {
        this.action = action;
    }

    public String getTime () {
        return time;
    }

    public void setTime (String time) {
        this.time = time;
    }

    public void setUser (String user) { this.user = user; }

    public String getUser () { return user; }
}
