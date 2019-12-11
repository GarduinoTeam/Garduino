package com.example.garduinoandroid;

import java.io.Serializable;

public class Data implements Serializable
{
    private int id;
    private String title;
    private String description;
    private String imagePath;
    private  String temperature;
    private String moisture;
    private String soil;

    public Data(int id, String title, String description, String imagePath, String temperature, String moisture, String soil) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.temperature = temperature;
        this.moisture = moisture;
        this.soil = soil;
        this.imagePath = imagePath;
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

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getTemperature(){return  temperature;}
    public String getMoisture(){return  moisture;}

    public void setMoisture(String moisture) {
        this.moisture = moisture;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setSoil(String soil) {
        soil = soil;
    }

    public String getSoil() {
        return soil;
    }
}
