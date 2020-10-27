package com.app.kutumb.Database.DataSource;

import com.app.kutumb.Database.ModelDB.Cart;
import java.util.List;

public interface ICartDataSource {

    List<Cart> getCartItems();
    List<Cart> getCartItemById(String cartItemId);
    int countCartItems();
    String checkCart(String item_id);
    void emptyCart();
    void insertToCart(Cart... carts);
    void updateCart(Cart... carts);
    void deleteCartItem(Cart cart);
    void update(String quantity, String sum, String item_id);
    void deleteByitem_id(String item_id);
    String getCost();
    String getQuantity();
}

