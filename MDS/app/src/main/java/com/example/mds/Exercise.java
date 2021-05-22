package com.example.mds;

import java.io.Serializable;

public class Exercise implements Serializable {
    public Exercise(String name, String areas, String description, String image, Boolean isSelected, float rating) {
        this.title = title;
        this.areas = areas;
        this.description = description;
        this.image = image;
        this.isSelected = isSelected;
        this.rating = rating;
    }

    public Exercise() {

    }

    String title, areas, description;
    String image;
    Boolean isSelected=false;
    float rating;

    public String getTitle() {
        return title;
    }

    public void setTile(String title) {
        this.title = title;
    }

    public String getAreas() {
        return areas;
    }

    public void setAreas(String areas) {
        this.areas = areas;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
