package com.example.mobile.model.cart;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.mobile.model.Item.Item;

import java.io.Serializable;

public class CartItem implements Serializable, Parcelable {
    private int amount;
    private float discount;
    private Item item;

    public CartItem() {
    }

    public CartItem(int amount, float discount, Item item) {
        this.amount = amount;
        this.discount = discount;
        this.item = item;
    }

    protected CartItem(Parcel in) {
        amount = in.readInt();
        discount = in.readFloat();
        item = (Item)in.readSerializable();
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

    public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel in) {
            return new CartItem(in);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };

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
