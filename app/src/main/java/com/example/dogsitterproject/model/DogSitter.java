package com.example.dogsitterproject.model;

public class DogSitter extends User {

    public DogSitter() {
    }

    private String salary;
    private String available;
    private String profilepictureurl;

    public DogSitter(String fullName, String id, String city, String email, String phone, String password, String salary, String available, String profilepictureurl) {
        super(fullName, id, city, email, phone, password);
        this.salary = salary;
        this.available = available;
        this.profilepictureurl = profilepictureurl;
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

    public String getAvailable() {
        return available;
    }

    public DogSitter setAvailable(String available) {
        this.available = available;
        return this;
    }
}
