package com.example.mobile.view.UserActivity.main.fragments;

import com.example.mobile.model.Item.Item;
import com.example.mobile.model.cart.CartItem;

import java.util.ArrayList;

public interface ISendData {
    void updateCartData();
    void addSelectedCartItems(CartItem cartItem);
    void removeSelectedCartItems(CartItem cartItem);
}
