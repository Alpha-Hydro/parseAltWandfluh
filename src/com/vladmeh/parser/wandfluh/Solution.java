package com.vladmeh.parser.wandfluh;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.plugin.javascript.navig.Array;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The type Solution.
 */
public class Solution {

    private static final String HOST = "http://alt.wandfluh.com";
    public static final String UPLOAD_IMG = "data\\img\\";
    public static final String UPLOAD_PDF = "data\\pdf\\";
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

        /*String link = HOST + "/fileadmin/user_upload/files/A_Dok/reg_1_2/1_2_26p_e.pdf";
        String[] path = link.split("/");
        String filePathName = UPLOAD_PDF + path[path.length -1];

        File file = new File(filePathName);


        System.out.println(!file.exists());*/
    }

    /**
     * @param list List
     * @throws IOException the io exception
     */
    private static void writeJsonFile(List<?> list) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.writeValue(new File("data\\sections.json"), list);
    }

    /**
     * @param list List
     * @throws IOException the io exception
     */
    private static void writeJson(List<?> list) throws IOException {
        StringWriter writer = new StringWriter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.writeValue(writer, list);
        System.out.println(writer.toString());
    }

    /**
     * @param link String
     * @param filePathName String
     * @return boolean
     * @throws IOException the io exception
     */
    private static boolean downloadFile(String link, String filePathName) throws IOException {
        URL url = new URL(link);
        File file = new File(filePathName);
        if (!file.exists()){
            try (
                    InputStream is = new BufferedInputStream(url.openStream());
                    OutputStream os = new FileOutputStream(file)
            ) {
                byte[] buffer = new byte[is.available()];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
            }
        }
        return true;
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

                group.setLevel(level);

                switch (level){
                    case 1: parsePropertyImage(group);
                    case 2: parseProductCategory(group);
                }

                List<Section> subGroups = getSubSections(a, level+1);
                if (!subGroups.isEmpty()){
                    group.setGroups(subGroups);
                }
                subSections.add(group);
            }
        }

        return subSections;
    }

    private static void parseProductCategory(Section group) throws IOException {
        Document pageProducts = Jsoup.connect(HOST + group.getLink()).get();

        Elements tableProducts = pageProducts.select("table.wagtable");

        if (tableProducts != null && !tableProducts.isEmpty()){
            List<ProductCategory> productCategories = new ArrayList<>();
            for (Element table: tableProducts){
                Element title = table.previousElementSibling();
                productCategories.add(new ProductCategory(title.children().text()));
            }
            group.setProductCategories(productCategories);
        }
    }

    private static void parsePropertyImage(Section group) throws IOException {
        Document pageGroup = Jsoup.connect(HOST + group.getLink()).get();

        Element container = pageGroup.getElementById("fcecontainer2");
        Element elProperty = container.getElementsByTag("ul").first();

        if (elProperty != null){
            Elements list = elProperty.children();
            List<String> properties = new ArrayList<>();
            for (Element li: list)
                properties.add(li.text());

            group.setProperty(properties);
        }

        Element elImage = pageGroup.selectFirst(".csc-textpic-image");
        if (elImage != null){
            Element img = elImage.getElementsByTag("img").first();
            String linkImg = img.attr("src");

            String link = HOST + linkImg;
            String[] path = link.split("/");
            String filePathName = UPLOAD_IMG + path[path.length -1];

            if (downloadFile(link, filePathName))
                group.setImage(filePathName);
        }
    }
}
