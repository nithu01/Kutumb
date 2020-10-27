package com.app.kutumb.Model;

public class MenuItems {

    private String item_id,menu_id,item_name,item_cost,item_type_id,item_image_name,
            item_description,status,is_most_selling_item,product_id,outlet_id;

    public String getItem_cost() {
        return item_cost;
    }

    public String getItem_id() {
        return item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getItem_type_id() {
        return item_type_id;
    }

    public String getItem_image_name() {
        return item_image_name;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public String getStatus() {
        return status;
    }

    public String getIs_most_selling_item() {
        return is_most_selling_item;
    }

    public String getItem_description() {
        return item_description;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getOutlet_id() {
        return outlet_id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setItem_cost(String item_cost) {
        this.item_cost = item_cost;
    }

    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public void setItem_image_name(String item_image_name) {
        this.item_image_name = item_image_name;
    }

    public void setIs_most_selling_item(String is_most_selling_item) {
        this.is_most_selling_item = is_most_selling_item;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public void setItem_type_id(String item_type_id) {
        this.item_type_id = item_type_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setOutlet_id(String outlet_id) {
        this.outlet_id = outlet_id;
    }
}
