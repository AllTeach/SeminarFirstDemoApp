package com.example.seminarfirstdemoapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FruitItem {
    @PrimaryKey(autoGenerate = true)
    private int id; // Added as primary key

    private int imageResource;
    private String name;
    private String description;

    // Updated constructor to optionally accept id if needed (Room uses setters)
    public FruitItem(int imageResource, String name, String description) {
        this.imageResource = imageResource;
        this.name = name;
        this.description = description;
    }

    // Room needs a public getter/setter for the primary key
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}