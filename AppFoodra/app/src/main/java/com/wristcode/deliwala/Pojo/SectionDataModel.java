package com.wristcode.deliwala.Pojo;

import java.util.ArrayList;

public class SectionDataModel
{
    private String headerTitle, headerType;
    private ArrayList<SingleItemModel> allItemsInSection;

    public SectionDataModel() {}

    public SectionDataModel(String headerTitle, String headerType, ArrayList<SingleItemModel> allItemsInSection)
    {
        this.headerTitle = headerTitle;
        this.headerType = headerType;
        this.allItemsInSection = allItemsInSection;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public String getHeaderType() {
        return headerType;
    }

    public void setHeaderType(String headerType) {
        this.headerType = headerType;
    }

    public ArrayList<SingleItemModel> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(ArrayList<SingleItemModel> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }


}
