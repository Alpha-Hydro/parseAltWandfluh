package com.vladmeh.parser.wandfluh;

import java.util.List;

/**
 * @autor mvl on 12.12.2017.
 */
public class Section {
    private Integer level;

    private String link;

    private String name;

    private String image;

    private List<String> property;

    private List<Section> groups;

    private List<ProductCategory> productCategories;

    public Section() {
    }

    public Section(String link, String name) {
        this.link = link;
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    public List<String> getProperty() {
        return property;
    }

    public void setProperty(List<String> property) {
        this.property = property;
    }

    public List<Section> getGroups() {
        return groups;
    }

    public void setGroups(List<Section> groups) {
        this.groups = groups;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<ProductCategory> getProductCategories() {
        return productCategories;
    }

    public void setProductCategories(List<ProductCategory> productCategories) {
        this.productCategories = productCategories;
    }

    @Override
    public String toString() {
        return this.name + ": " + this.link;
    }
}
