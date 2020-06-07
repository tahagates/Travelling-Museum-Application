package com.example.loginpart.model;

import android.media.Image;

public class ArtifactModel {
    int artID;
    String category;
    String name;
    int point;
    int[] mapLocation; // index 0 for X, 1 for Y

    public ArtifactModel(int artID, String category, String name, int point, int[] mapLocation) {
        this.artID = artID;
        this.category = category;
        this.name = name;
        this.point = point;
        this.mapLocation = mapLocation;
    }
}
