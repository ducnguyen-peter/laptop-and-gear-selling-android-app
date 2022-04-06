package com.example.mobile.model.order;

import com.example.mobile.model.Item.Item;
import com.example.mobile.model.cart.CartItem;

import java.io.Serializable;

public class OrderItem implements Serializable {
    private int amount;
    private float discount;
    private Item item;

    public OrderItem() {
    }

    public OrderItem(int amount, float discount, Item item) {
        this.amount = amount;
        this.discount = discount;
        this.item = item;
    }

    public OrderItem(CartItem cartItem){
        this.amount = cartItem.getAmount();
        this.discount = cartItem.getDiscount();
        this.item = cartItem.getItem();
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
