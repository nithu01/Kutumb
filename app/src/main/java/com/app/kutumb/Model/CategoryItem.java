package com.app.kutumb.Model;

public class CategoryItem {

    private String name;
    private String url;
    private String description;
    private String price;


    public CategoryItem() {
    }

    public CategoryItem(String name, String url,String description,String price) {
        this.name = name;
        this.url = url;
        this.description=description;
        this.price=price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
