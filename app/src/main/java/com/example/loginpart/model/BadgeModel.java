package com.example.loginpart.model;

import java.util.ArrayList;

public class BadgeModel {

    private String name;

    private String description;
    private ArrayList<String> path;

    public BadgeModel(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public BadgeModel(String name, String description, ArrayList<String> path) {
        this.name = name;
        this.description = description;
        this.path = path;
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

    public ArrayList<String> getPath() {
        return path;
    }

    public int getPathSize() {return path.size();}

    public void setPath(ArrayList<String> path) {
        this.path = path;
    }


}
