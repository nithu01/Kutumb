package com.app.kutumb.Database.Local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.app.kutumb.Database.ModelDB.Cart;

import java.util.List;

@Dao
public interface CartDAO {

    @Query("SELECT * FROM Cart")
    List<Cart> getCartItems();

    @Query("SELECT * FROM Cart WHERE item_id=:item_id")
    List<Cart> getCartItemById(String item_id);

    @Query("SELECT item_id FROM Cart WHERE item_id=:item_id")
    String checkCart(String item_id);

    @Query("SELECT COUNT(*) from Cart")
    int countCartItem();

    @Query("DELETE FROM Cart")
    void emptyCart();

    @Query("UPDATE Cart SET quantity=:quantity , item_cost=:itemCost WHERE item_id = :item_id")
    void update(String quantity, String itemCost, String item_id);

    @Query("DELETE FROM Cart WHERE item_id = :item_id")
    void deleteByitem_id(String item_id);

    @Query("SELECT SUM(item_cost) as item_cost FROM Cart")
    String getCost();

    @Query("SELECT SUM(quantity) as quantity FROM Cart")
    String getQuantity();

    @Insert
    void insertToCart(Cart... carts);

    @Update
    void updateCart(Cart... carts);

    @Delete
    void deleteCartItem(Cart cart);
}
