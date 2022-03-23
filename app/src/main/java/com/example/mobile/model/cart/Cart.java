package com.example.mobile.model.cart;

import com.example.mobile.model.user.User;

import java.io.Serializable;
import java.util.ArrayList;

public class Cart implements Serializable {
    private int id;
    private float discount;
    private User user;
    private ArrayList<CartItem> cartItems;

    public Cart() {
    }

    public Cart(int id, User user) {
        this.id = id;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(ArrayList<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
}
