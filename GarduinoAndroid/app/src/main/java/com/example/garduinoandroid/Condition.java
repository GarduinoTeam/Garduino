package com.example.garduinoandroid;

import java.io.Serializable;

public class Condition implements Serializable {
    private int id;
    private String title;


    public Condition(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}