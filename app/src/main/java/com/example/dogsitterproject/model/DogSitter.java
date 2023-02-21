package com.example.dogsitterproject.model;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;


public class DogSitter extends User implements Serializable {

    public DogSitter() {
    }
    ArrayList<String> dogSitterImages = new ArrayList<>();
    private String salary;
    private String description;
    private String profilepictureurl;

    public DogSitter(String fullName, String id, String city, String email, String phone, String password, String salary,String profilepictureurl,String description) {
        super(fullName, id, city, email, phone, password);
        this.salary = salary;
        this.description = description;
    }


    public ArrayList<String> getDogSitterImages() {
        return dogSitterImages;
    }

    public DogSitter setDogSitterImages(ArrayList<String> dogSitterImages) {
        this.dogSitterImages = dogSitterImages;
        return this;
    }

    public String getProfilepictureurl() {
        return profilepictureurl;
    }

    public DogSitter setProfilepictureurl(String profilepictureurl) {
        this.profilepictureurl = profilepictureurl;
        return this;
    }

    public String getSalary() {
        return salary;
    }

    public DogSitter setSalary(String salary) {
        this.salary = salary;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public DogSitter setDescription(String description) {
        this.description = description;
        return this;
    }
}
