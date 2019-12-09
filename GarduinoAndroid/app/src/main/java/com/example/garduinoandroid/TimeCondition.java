package com.example.garduinoandroid;

import java.io.Serializable;

public class TimeCondition implements Serializable
{
    private int id;
    private String title;
    private String description;

    public TimeCondition(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
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