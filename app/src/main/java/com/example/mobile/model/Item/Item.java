package com.example.mobile.model.Item;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Item implements Serializable{
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

    protected Item(Parcel in) {
        id = in.readInt();
        unitPrice = in.readFloat();
        quantity = in.readInt();
        totalBuy = in.readInt();
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
