package com.app.kutumb.Model;

import java.util.ArrayList;

public class SectionCategoryDataModel {

    private String headerTitle;
    private ArrayList<CategoryItem> allItemsInSection;


    public SectionCategoryDataModel() {

    }
    public SectionCategoryDataModel(String headerTitle, ArrayList<CategoryItem> allItemsInSection) {
        this.headerTitle = headerTitle;
        this.allItemsInSection = allItemsInSection;
    }



    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public ArrayList<CategoryItem> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(ArrayList<CategoryItem> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }

}
