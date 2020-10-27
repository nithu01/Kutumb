package com.app.kutumb.Database.DataSource;

import com.app.kutumb.Database.ModelDB.Cart;

import java.util.List;

public class CartRepository implements ICartDataSource {

    private ICartDataSource iCartDataSource;

    public CartRepository(ICartDataSource iCartDataSource) {
        this.iCartDataSource = iCartDataSource;
    }

    private static CartRepository instance;

    public static CartRepository getInstance(ICartDataSource iCartDataSource) {

        if(instance == null)
            instance = new CartRepository(iCartDataSource);

        return instance;
    }

    @Override
    public List<Cart> getCartItems() {
        return iCartDataSource.getCartItems();
    }

    @Override
    public List<Cart> getCartItemById(String cartItemId) {
        return iCartDataSource.getCartItemById(cartItemId);
    }

    @Override
    public int countCartItems() {
        return iCartDataSource.countCartItems();
    }

    @Override
    public void emptyCart() {
        iCartDataSource.emptyCart();
    }

    @Override
    public void insertToCart(Cart... carts) {
        iCartDataSource.insertToCart(carts);
    }


    @Override
    public void updateCart(Cart... carts) {
        iCartDataSource.updateCart(carts);
    }

    @Override
    public void deleteCartItem(Cart cart) {
        iCartDataSource.deleteCartItem(cart);
    }

    @Override
    public void update(String quantity,String itemCost, String item_id) {
        iCartDataSource.update(quantity,itemCost,item_id);
    }

    @Override
    public String getCost(){
        return iCartDataSource.getCost();
    }

    @Override
    public String getQuantity(){
        return iCartDataSource.getQuantity();
    }

    @Override
    public void deleteByitem_id(String item_id) {
        iCartDataSource.deleteByitem_id(item_id);
    }

    @Override
    public String checkCart(String item_id) {
        return iCartDataSource.checkCart(item_id);
    }
}
