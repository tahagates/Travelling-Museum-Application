package com.example.loginpart.model;

import java.util.ArrayList;

public class BadgeModel {

    private String name;

    private String description;
    private ArrayList<Integer> path;

    public BadgeModel(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public BadgeModel(String name, String description, ArrayList<Integer> path) {
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

    public ArrayList<Integer> getPath() {
        return path;
    }

    public void setPath(ArrayList<Integer> path) {
        this.path = path;
    }


}
