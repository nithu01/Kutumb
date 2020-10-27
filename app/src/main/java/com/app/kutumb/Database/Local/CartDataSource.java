package com.app.kutumb.Database.Local;

import com.app.kutumb.Database.DataSource.ICartDataSource;
import com.app.kutumb.Database.ModelDB.Cart;

import java.util.List;

public class CartDataSource implements ICartDataSource {

    private CartDAO cartDAO;
    private static CartDataSource instance;

    public CartDataSource(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }

    public static CartDataSource getInstance(CartDAO cartDAO) {

        if(instance == null)
            instance = new CartDataSource(cartDAO);

        return instance;
    }

    @Override
    public List<Cart> getCartItems() {
        return cartDAO.getCartItems();
    }

    @Override
    public List<Cart> getCartItemById(String cartItemId) {
        return cartDAO.getCartItemById(cartItemId);
    }

    @Override
    public int countCartItems() {
        return cartDAO.countCartItem();
    }

    @Override
    public String checkCart(String item_id) {
        return cartDAO.checkCart(item_id);
    }

    @Override
    public void emptyCart() {
        cartDAO.emptyCart();
    }

    @Override
    public void insertToCart(Cart... carts) {
        cartDAO.insertToCart(carts);
    }

    @Override
    public void update(String quantity,String itemCost, String item_id) {
        cartDAO.update(quantity,itemCost,item_id);
    }

    @Override
    public void updateCart(Cart... carts) {
        cartDAO.updateCart(carts);
    }
    @Override
    public void deleteByitem_id(String item_id){
        cartDAO.deleteByitem_id(item_id);
    }

    @Override
    public String getCost() {
        return  cartDAO.getCost();
    }
    @Override
    public String getQuantity() {
        return  cartDAO.getQuantity();
    }

    @Override
    public void deleteCartItem(Cart cart) {
        cartDAO.deleteCartItem(cart);
    }

}
