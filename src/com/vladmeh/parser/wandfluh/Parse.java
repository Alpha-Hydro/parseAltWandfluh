package com.vladmeh.parser.wandfluh;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Parse.
 */
public class Parse {

    private static final String HOST = "http://alt.wandfluh.com";
    private static final String UPLOAD_IMG = "data\\img\\";
    private static final String UPLOAD_PDF = "data\\pdf\\";

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws IOException the io exception
     */
    public static void main(String[] args) throws IOException {
        Connection connect = Jsoup.connect("http://alt.wandfluh.com/ru/karta-saita/");
        Document web = connect.get();
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
     * @param link         String
     * @param filePathName String
     * @return boolean
     * @throws IOException the io exception
     */
    private static boolean downloadFile(String link, String filePathName) throws IOException {
        URL url = new URL(link);
        File file = new File(filePathName);
        if (!file.exists()) {
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

        if (element.nextElementSibling() != null) {
            Elements elements = element.nextElementSibling().children();
            for (Element el : elements) {
                Element a = el.getElementsByTag("a").first();
                Section group = new Section(a.attr("href"), a.text());

                group.setLevel(level);

                System.out.print(level);

                switch (level) {
                    case 1:
                        parsePropertyImage(group);
                    case 2:
                        parseProductCategory(group);
                }

                List<Section> subGroups = getSubSections(a, level + 1);
                if (!subGroups.isEmpty()) {
                    group.setGroups(subGroups);
                }
                subSections.add(group);
            }
        }

        return subSections;
    }

    /**
     * @param group Section
     * @throws IOException the io exception
     */
    private static void parseProductCategory(Section group) throws IOException {
        Document pageProducts = Jsoup.connect(HOST + group.getLink()).get();
        Elements tableProducts = pageProducts.select("table.wagtable");

        if (tableProducts != null && !tableProducts.isEmpty()) {
            List<ProductCategory> productCategories = new ArrayList<>();
            for (Element table : tableProducts) {
                Element title = table.previousElementSibling();
                ProductCategory productCategory = new ProductCategory(title.children().text());
                productCategory.setProducts(parseProducts(table));
                productCategories.add(productCategory);
            }
            group.setProductCategories(productCategories);
        }
    }

    /**
     * @param table Element
     * @return List<Product>
     * @throws IOException the io exception
     */
    private static List<Product> parseProducts(Element table) throws IOException {
        List<Product> products = new ArrayList<>();

        //заголовок таблицы (названия полей, свойств продукта)
        Elements fields = table.selectFirst("thead").select("td");
        //строки таблицы (товары)
        Elements rows = table.selectFirst("tbody").select("tr");

        //читаем строки (товары)
        for (Element row : rows) {
            //получаем ячейки (свойства)
            Elements cols = row.select("td");
            Product product = new Product();
            //читаем каждую ячейку (свойство)
            for (int i = 0; i < cols.size(); i++) {
                if (i <= 1) {
                    product.setDataSheetNo(cols.get(0).text());
                    product.setConstruction(cols.get(1).text());
                }
                //сравниваем индекс ячейки заголовка
                if (fields.get(i).text().equals("Тип") || fields.get(i).text().equals("Type"))
                    product.setType(cols.get(i).text());

                if (fields.get(i).text().equals("Размеры") || fields.get(i).text().equals("Size"))
                    product.setSize(cols.get(i).text());

                String linkPdf = cols.get(0).getElementsByTag("a").attr("href");
                String link = HOST + linkPdf;
                String[] path = link.split("/");
                String fileName = path[path.length - 1];
                String filePathName = UPLOAD_PDF + fileName;
                if (downloadFile(link, filePathName))
                    product.setPdfFile(fileName);
            }

            products.add(product);
        }

        return products;
    }


    /**
     * @param group Section
     * @throws IOException the io exception
     */
    private static void parsePropertyImage(Section group) throws IOException {
        Document pageGroup = Jsoup.connect(HOST + group.getLink()).get();

        Element container = pageGroup.getElementById("fcecontainer2");
        Element elProperty = container.getElementsByTag("ul").first();

        if (elProperty != null) {
            Elements list = elProperty.children();
            List<String> properties = new ArrayList<>();
            for (Element li : list)
                properties.add(li.text());

            group.setProperty(properties);
        }

        Element elImage = pageGroup.selectFirst(".csc-textpic-image");
        if (elImage != null) {
            Element img = elImage.getElementsByTag("img").first();
            String linkImg = img.attr("src");

            String link = HOST + linkImg;
            String[] path = link.split("/");
            String fileName = path[path.length - 1];
            String filePathName = UPLOAD_IMG + fileName;

            if (downloadFile(link, filePathName))
                group.setImage(fileName);
        }
    }
}
