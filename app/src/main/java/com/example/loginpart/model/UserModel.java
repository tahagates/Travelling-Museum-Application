package com.example.loginpart.model;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class UserModel {

        private String fullName;
        private String email;
        private String job;
        private int age;
        private String password;
        private int point;
        private int reward;



        private ArrayList<String> path;

        public UserModel(String fullName, int point,int reward)
        {
            this.fullName = fullName;
            this.point = point;
            this.reward = reward;
        }


        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public int getPoint() {
            return point;
        }

        public void setPoint(int point) {
            this.point = point;
        }

        public int getReward() {
            return reward;
        }

        public void setReward(int reward) {
            this.reward = reward;
        }


        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public ArrayList<String> getPath() { return path; }

        public void setPath(ArrayList<String> path) { this.path = path; }


}
