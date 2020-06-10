package com.example.loginpart.model;

public class artMapLocModel {
    private long coordinateX;
    private long coordinateY;
    private long artifactID;

    public artMapLocModel(long coordinateX, long coordinateY, long artifactID) {
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.artifactID = artifactID;
    }

    public long getCoordinateX() {
        return coordinateX;
    }

    public long getCoordinateY() {
        return coordinateY;
    }

    public long getArtifactID() {
        return artifactID;
    }


}
