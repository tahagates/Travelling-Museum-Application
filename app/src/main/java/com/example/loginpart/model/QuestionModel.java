package com.example.loginpart.model;

public class QuestionModel {

    int artifactID;
    String question;


    public QuestionModel (int artifactID, String question)
    {
        this.artifactID = artifactID;
        this.question = question;
    }

    public int getArtifactID() { return artifactID; }

    public void setArtifactID(int artifactID) { this.artifactID = artifactID; }

    public String getQuestion() { return question; }

    public void setQuestion(String question) { this.question = question; }



}
