package com.vladmeh.parser.wandfluh;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * @autor mvl on 08.12.2017.
 */
public class Solution {
    public static void main(String[] args) throws IOException {
        Document web = Jsoup.connect("http://alt.wandfluh.com/ru/karta-saita/").get();
        
        Element section = web.body().getElementsByAttributeValue("title", "Ассортимент").first();
        Elements list = section.nextElementSibling().children();

        for (Element li: list){
            Element link = li.getElementsByTag("a").first();
            System.out.println(link.text() + ": " +  link.attr("href"));
        }

    }
}
