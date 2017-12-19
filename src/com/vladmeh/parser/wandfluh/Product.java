package com.vladmeh.parser.wandfluh;

/**
 * @autor mvl on 14.12.2017.
 */
public class Product {
    private int id;

    private String dataSheetNo;
    private String construction;
    private String size;
    private String type;
    private String pdfFile;

    private int sizeId;
    private int constructionId;
    private int typeId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSizeId() {
        return sizeId;
    }

    public void setSizeId(int sizeId) {
        this.sizeId = sizeId;
    }

    public int getConstructionId() {
        return constructionId;
    }

    public void setConstructionId(int constructionId) {
        this.constructionId = constructionId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getDataSheetNo() {
        return dataSheetNo;
    }

    public void setDataSheetNo(String dataSheetNo) {
        this.dataSheetNo = dataSheetNo;
    }

    public String getConstruction() {
        return construction;
    }

    public void setConstruction(String construction) {
        this.construction = construction;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(String pdfFile) {
        this.pdfFile = pdfFile;
    }
}
