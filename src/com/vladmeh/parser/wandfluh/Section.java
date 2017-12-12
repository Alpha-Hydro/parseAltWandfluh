package com.vladmeh.parser.wandfluh;

import java.util.List;

/**
 * @autor mvl on 12.12.2017.
 */
public class Section {
    private String link;

    private String name;

    private List<Section> groups;

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

    @Override
    public String toString() {
        return this.name + ": " + this.link;
    }

    public List<Section> getGroups() {
        return groups;
    }

    public void setGroups(List<Section> groups) {
        this.groups = groups;
    }
}
