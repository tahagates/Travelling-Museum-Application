package com.example.loginpart.model;

import android.media.Image;

public class ArtifactModel {
    private String aCode;
    private String[] questions; // or basic string ?
    private Image aImage;
    private int aLevel; //necessary ?
    private int point; //bir sorgu gerçekleştirirken tüm o bilgilerin bir modele aktarılıp o model üzerinden işlem yapılması
    //gerektiği düşündüğüm için bunları ekliyorum.
    private String category;
    private String name;
    private boolean isTraveled; //Gezildi mi ?

    public ArtifactModel(String aCode,Image aImage,int aLevel) {
        this.aCode = aCode;
        this.aImage = aImage;
        this.aLevel = aLevel;
    }

    public String getaCode() { return aCode; }

    public int getaLevel() { return aLevel; }

    public int getPoint() { return aLevel; }

    public void setPoint(int point) { this.point = point; }

    public void setCategory(String category) { this.category = category; }

    public void setName(String name) { this.name = name; }
}
