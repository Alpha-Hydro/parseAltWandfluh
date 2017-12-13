package com.vladmeh.parser.wandfluh;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Solution.
 */
public class Solution {

    private static final String HOST = "http://alt.wandfluh.com";
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws IOException the io exception
     */
    public static void main(String[] args) throws IOException {
        Document web = Jsoup.connect("http://alt.wandfluh.com/ru/karta-saita/").get();
        Element element = web.body().getElementsByAttributeValue("title", "Ассортимент").first();

        List<Section> sections = getSubSections(element, 0);
        writeJsonFile(sections);
    }

    /**
     * @param list List
     * @throws IOException the io exception
     */
    private static void writeJsonFile(List<?> list) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File("data\\sections.json"), list);
    }

    /**
     * @param element Element
     * @return List<Section>
     */
    private static List<Section> getSubSections(Element element, int level) throws IOException {
        List<Section> subSections = new ArrayList<>();

        if (element.nextElementSibling() != null){
            Elements elements = element.nextElementSibling().children();
            for (Element el: elements){
                Element a = el.getElementsByTag("a").first();
                Section group = new Section(a.attr("href"), a.text());

                System.out.println(level);

                if (level > 0)
                    parsePropertyImage(group);

                List<Section> subGroups = getSubSections(a, level+1);
                if (!subGroups.isEmpty()){
                    group.setGroups(subGroups);
                }
                subSections.add(group);
            }
        }

        return subSections;
    }

    private static void parsePropertyImage(Section group) throws IOException {
        Document pageGroup = Jsoup.connect(HOST + group.getLink()).get();

        Element element = pageGroup.selectFirst(".csc-textpic-text");

        if (element != null){

            Elements list = element.child(0).children();
            List<String> properties = new ArrayList<>();
            for (Element li: list)
                properties.add(li.text());

            group.setProperty(properties);
        }
    }
}
