package com.app.kutumb.Model;

public class OfferItemDetail {

    String offer_product_id,offer_id,menu_id,menu_name,item_id,item_name,quantity;

    public String getOffer_id() {
        return offer_id;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public String getItem_id() {
        return item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getOffer_product_id() {
        return offer_product_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setOffer_id(String offer_id) {
        this.offer_id = offer_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public void setOffer_product_id(String offer_product_id) {
        this.offer_product_id = offer_product_id;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
