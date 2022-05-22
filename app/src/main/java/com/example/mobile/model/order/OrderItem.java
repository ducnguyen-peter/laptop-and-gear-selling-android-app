package com.example.mobile.model.order;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.mobile.model.Item.Item;
import com.example.mobile.model.cart.CartItem;

import java.io.Serializable;

public class OrderItem implements Serializable, Parcelable {
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

    protected OrderItem(Parcel in) {
        amount = in.readInt();
        discount = in.readFloat();
        item = (Item)in.readSerializable();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(amount);
        parcel.writeFloat(discount);
        parcel.writeSerializable(item);
    }

    public static final Creator<OrderItem> CREATOR = new Creator<OrderItem>() {
        @Override
        public OrderItem createFromParcel(Parcel in) {
            return new OrderItem(in);
        }

        @Override
        public OrderItem[] newArray(int size) {
            return new OrderItem[size];
        }
    };
}
