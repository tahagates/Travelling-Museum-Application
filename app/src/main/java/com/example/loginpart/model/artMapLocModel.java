package com.example.loginpart.model;

public class artMapLocModel {
    private float coordinateX;
    private float coordinateY;
    private int artifactID;

    public artMapLocModel(float coordinateX, float coordinateY, int artifactID) {
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.artifactID = artifactID;
    }

    public float getCoordinateX() {
        return coordinateX;
    }

    public float getCoordinateY() {
        return coordinateY;
    }

    public int getArtifactID() {
        return artifactID;
    }

    public void setArtifactID(int artifactID) {
        this.artifactID = artifactID;
    }



}
