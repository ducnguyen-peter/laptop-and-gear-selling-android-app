package com.example.mobile.model.order;

import com.example.mobile.model.user.User;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {
    private int id;
    private String status;
    private User user;
    private ArrayList<OrderItem> orderItems;
    private Payment payment;
    private Shipment shipment;

    public static final String ORDER_STATUS_PENDING = "Pending";
    public static final String ORDER_STATUS_DELIVERING = "Delivering";
    public static final String ORDER_STATUS_DELIVERED = "Delivered";

    public Order() {
    }

    public Order(User user) {
        this.user = user;
    }

    public Order(int id, String status, User user, ArrayList<OrderItem> orderItems) {
        this.id = id;
        this.status = status;
        this.user = user;
        this.orderItems = orderItems;
    }

    public Order(String status, User user, ArrayList<OrderItem> orderItems, Payment payment, Shipment shipment) {
        this.status = status;
        this.user = user;
        this.orderItems = orderItems;
        this.payment = payment;
        this.shipment = shipment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(ArrayList<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", user=" + user.getUsername() +
                ", payment=" + payment.getTypeName() +
                ", shipment=" + shipment.getTypeName() +
                '}';
    }
}
