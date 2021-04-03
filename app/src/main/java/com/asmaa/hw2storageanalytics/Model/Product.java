package com.asmaa.hw2storageanalytics.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Product implements Serializable {
  private String categoryName;
    private String name;
    private String image;
    private String details;

    public Product() {
    }

    public Product(String categoryName, String name, String image, String details) {
        this.categoryName = categoryName;
        this.name = name;
        this.image = image;
        this.details = details;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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
