package com.example.loginpart.model;

import android.media.Image;

public class ArtifactModel {
    private String aCode;
    private String[] questions; // or basic string ?
    private Image aImage;
    private int aLevel; //necessary ??

    public ArtifactModel(String aCode,Image aImage,int aLevel) {
        this.aCode = aCode;
        this.aImage = aImage;
        this.aLevel = aLevel;
    }

    public String getaCode() {
        return aCode;
    }

    public int getaLevel() {
        return aLevel;
    }

}
