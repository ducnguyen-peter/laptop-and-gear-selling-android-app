package com.example.mobile.model.cart;

import com.example.mobile.model.Item.Item;

import java.io.Serializable;

public class CartItem implements Serializable {
    private int id;
    private int amount;
    private float discount;
    private Item item;

    public CartItem() {
    }

    public CartItem(int id, int amount, float discount, Item item) {
        this.id = id;
        this.amount = amount;
        this.discount = discount;
        this.item = item;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
