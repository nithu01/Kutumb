package com.app.kutumb.Database.ModelDB;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Cart")
public class Cart {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "item_id")
    public String item_id;

    @ColumnInfo(name = "menu_id")
    public String menu_id;

    @ColumnInfo(name = "item_name")
    public String item_name;

    @ColumnInfo(name = "item_cost")
    public String item_cost;

    @ColumnInfo(name = "item_type_id")
    public String item_type_id;

    @ColumnInfo(name = "item_image_name")
    public String item_image_name;

    @ColumnInfo(name = "item_description")
    public String item_description;

    @ColumnInfo(name = "status")
    public String status;

    @ColumnInfo(name = "is_most_selling_item")
    public String is_most_selling_item;

    @ColumnInfo(name = "product_id")
    public String product_id;

    @ColumnInfo(name = "quantity")
    public String quantity;

    public String getItem_name() {
        return item_name;
    }

    public String getItem_id() {
        return item_id;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public String getItem_description() {
        return item_description;
    }

    public String getStatus() {
        return status;
    }

    public String getIs_most_selling_item() {
        return is_most_selling_item;
    }

    public String getItem_image_name() {
        return item_image_name;
    }

    public String getItem_type_id() {
        return item_type_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getItem_cost() {
        return item_cost;
    }

    public int getId() {
        return id;
    }

    public String getQuantity() {
        return quantity;
    }

}
