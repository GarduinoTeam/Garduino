package com.example.garduinoandroid;

import java.io.Serializable;

public class EditTextCondition implements Serializable {
    private int id;
    private String title;
    private String edit;
    private String measure;

    public EditTextCondition(int id, String title, String edit, String measure) {
        this.id = id;
        this.title = title;
        this.edit = edit;
        this.measure = measure;
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

    public void setEdit(String edit) {
        this.edit = edit;
    }

    public String getEdit() {
        return edit;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getMeasure() {
        return measure;
    }
}
