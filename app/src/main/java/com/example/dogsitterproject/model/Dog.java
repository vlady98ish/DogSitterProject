package com.example.dogsitterproject.model;

public class Dog {

    private String name;
    private String gender;
    private String age;
    private String breed;
    private String weight;
    private String dogpictureurl;
    private String phone;
    private String idUser;


    public Dog() {
    }

    public Dog(String name, String idUser, String gender, String age, String breed, String weight, String dogpictureurl, String phone) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.breed = breed;
        this.weight = weight;
        this.dogpictureurl = dogpictureurl;
        this.phone = phone;
        this.idUser = idUser;
    }


    public String getIdUser() {
        return idUser;
    }

    public Dog setIdUser(String idUser) {
        this.idUser = idUser;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public Dog setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getName() {
        return name;
    }

    public Dog setName(String name) {
        this.name = name;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public Dog setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getAge() {
        return age;
    }

    public Dog setAge(String age) {
        this.age = age;
        return this;
    }

    public String getBreed() {
        return breed;
    }

    public Dog setBreed(String breed) {
        this.breed = breed;
        return this;
    }

    public String getWeight() {
        return weight;
    }

    public Dog setWeight(String weight) {
        this.weight = weight;
        return this;
    }

    public String getDogpictureurl() {
        return dogpictureurl;
    }

    public Dog setDogpictureurl(String dogpictureurl) {
        this.dogpictureurl = dogpictureurl;
        return this;
    }
}
