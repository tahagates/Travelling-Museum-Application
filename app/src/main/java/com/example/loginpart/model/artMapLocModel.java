package com.example.loginpart.model;

public class artMapLocModel {
    private float coordinateX;
    private float coordinateY;
    private float artifactID;

    public artMapLocModel(float coordinateX, float coordinateY, float artifactID) {
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

    public float getArtifactID() {
        return artifactID;
    }


}
