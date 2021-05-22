package com.example.mds;

import java.util.List;

public class User {
    public String fullName,age,email;
    public String imageURL;
    List<String> favourites;
    public User(){

    }

    public List<String> getFavourites() {
        return favourites;
    }

    public void setFavourites(List<String> favourites) {
        this.favourites = favourites;
    }

    public User(String fullName, String age, String email, String imageURL, List<String> favourites) {
        this.fullName = fullName;
        this.imageURL = imageURL;
        this.age = age;
        this.email = email;
        this.favourites=favourites;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
