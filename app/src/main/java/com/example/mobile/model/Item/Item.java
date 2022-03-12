package com.example.mobile.model.Item;

public class Item {
    private int id;
    private float unitPrice;
    private int quantity;
    private int totalBuy;
    private Electronics electronics;

    public Item() {
    }

    public Item(int id, float unitPrice, int quantity, int totalBuy, Electronics electronics) {
        this.id = id;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.totalBuy = totalBuy;
        this.electronics = electronics;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalBuy() {
        return totalBuy;
    }

    public void setTotalBuy(int totalBuy) {
        this.totalBuy = totalBuy;
    }

    public Electronics getElectronics() {
        return electronics;
    }

    public void setElectronics(Electronics electronics) {
        this.electronics = electronics;
    }
}
