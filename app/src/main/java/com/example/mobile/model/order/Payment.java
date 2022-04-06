package com.example.mobile.model.order;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

public class Payment implements Serializable {
    private int id;
    private int type;
    private String typeName;
    private float totalExpense;
    private String cardId;
    private Date date;

    public Payment() {
    }

    public Payment(int id, int type, String typeName, float totalExpense, String cardId, Date date) {
        this.id = id;
        this.type = type;
        this.typeName = typeName;
        this.totalExpense = totalExpense;
        this.cardId = cardId;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public float getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(float totalExpense) {
        this.totalExpense = totalExpense;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
