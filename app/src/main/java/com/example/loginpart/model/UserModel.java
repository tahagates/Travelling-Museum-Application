package com.example.loginpart.model;

import com.google.firebase.auth.FirebaseUser;

public class UserModel {

        private String fullName;
        private String email;
        private String job;
        private int age;
        private String password;
        private int point;
        private String reward;
        //path

        public UserModel(String fullName, int point)//rewardda eklenicek daha sonra
        {
            this.fullName = fullName;
            this.point = point;
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

        public String getReward() {
            return reward;
        }

        public void setReward(String reward) {
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


}
