package com.vladmeh.parser.wandfluh;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Solution {

    private static List<Section> sections = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Document web = Jsoup.connect("http://alt.wandfluh.com/ru/karta-saita/").get();
        
        Element element = web.body().getElementsByAttributeValue("title", "Ассортимент").first();

        List<Section> sections = getListSections(element.nextElementSibling().children());

        writeJsonFile(sections);

    }

    private static void writeJsonFile(List<?> list) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File("data\\sections.json"), list);
    }

    private static List<Section> getListSections(Elements elements){

        for (Element liSection: elements){
            Element aSection = liSection.getElementsByTag("a").first();
            Section section = new Section(aSection.attr("href"), aSection.text());

            Elements liGroups = aSection.nextElementSibling().children();
            if (!liGroups.isEmpty()){
                List<Section> groups = new ArrayList<>();
                for (Element liGroup: liGroups){
                    Element aGroup = liGroup.getElementsByTag("a").first();
                    Section group = new Section(aGroup.attr("href"), aGroup.text());

                    List<Section> subgroups = new ArrayList<>();
                    Elements liSubgroups = aGroup.nextElementSibling().children();

                    if (!liSubgroups.isEmpty()){
                        for (Element liSubgroup: liSubgroups){
                            Element aSubgroup = liSubgroup.getElementsByTag("a").first();
                            Section subgroup = new Section(aSubgroup.attr("href"), aSubgroup.text());

                            subgroups.add(subgroup);
                        }
                        group.setGroups(subgroups);
                    }
                    groups.add(group);
                }
                section.setGroups(groups);
            }

            sections.add(section);
        }

        return sections;
    }
}
