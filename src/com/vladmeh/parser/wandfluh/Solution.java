package com.vladmeh.parser.wandfluh;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Solution {
    public static void main(String[] args) throws IOException {
        Document web = Jsoup.connect("http://alt.wandfluh.com/ru/karta-saita/").get();
        
        Element section = web.body().getElementsByAttributeValue("title", "Ассортимент").first();
        Elements list = section.nextElementSibling().children();

        for (Element li: list){
            Element link = li.getElementsByTag("a").first();
            System.out.println(link.text() + ": " +  link.attr("href"));
        }
        System.out.println();
        writeJson();
    }

    /**
     * Example to writeJson using TreeModel
     */
    private static void writeJson() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode rootNode = mapper.createObjectNode();
        //rootNode.put("message", "A");

        ObjectNode childNode = rootNode.putObject("place");
        childNode.put("name", "B");

        mapper.writeValue(outputStream, rootNode);

        System.out.println(outputStream.toString());
        // print "{"message":"Hi","place":{"name":"World!"}}"
    }
}
