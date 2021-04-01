package com.asmaa.hw2storageanalytics.Model;

import java.io.Serializable;

public class Item implements Serializable {
    private String name;
    private String image;
    private String details;

    public Item(String name, String image, String details) {
        this.name = name;
        this.image = image;
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}