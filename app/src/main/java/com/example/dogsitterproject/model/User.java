package com.example.dogsitterproject.model;


import java.io.Serializable;

public class User implements Serializable {
    private String fullName, id, email, phone, password, city,type;


    public User() {
    }

    public User(String fullName, String id, String city, String email, String phone, String password) {
        this.fullName = fullName;

        this.email = email;
        this.phone = phone;
        this.password = password;
        this.city = city;
        this.id = id;


    }

    public String getId() {
        return id;
    }

    public User setId(String id) {
        this.id = id;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public User setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getType() {
        return type;
    }

    public User setType(String type) {
        this.type = type;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public User setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getCity() {
        return city;
    }

    public User setCity(String city) {
        this.city = city;
        return this;
    }
}
