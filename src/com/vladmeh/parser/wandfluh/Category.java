package com.vladmeh.parser.wandfluh;


import java.util.List;

public class Category extends Section{
    private List<String> property;

    private String description;

    private String image;

    public Category() {
    }

    public Category(String link, String name) {
        super(link, name);
    }

    public List<String> getProperty() {
        return property;
    }

    public void setProperty(List<String> property) {
        this.property = property;
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
}
