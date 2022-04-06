package com.example.mobile.controller.OrderDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.mobile.model.cart.Cart;
import com.example.mobile.model.order.Order;
import com.example.mobile.model.order.OrderItem;
import com.example.mobile.model.user.User;
import com.example.mobile.utils.Constant;
import com.example.mobile.utils.DBHelper;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OrderDAOImpl implements OrderDAO{
    private SQLiteDatabase sqLiteDatabase;
    private DBHelper dbHelper;
    private Context context;

    public OrderDAOImpl(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
        try{
            open();
        } catch (SQLiteException e){
            Log.e("OrderDAOImpl", "SQLException on opening database" + e.getMessage());
            e.printStackTrace();
        }
    }
    private void open(){
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }
    private void close(){
        dbHelper.close();
    }

    public Order createOrder(Order order){
        try {
            //create blank order
            ContentValues values = new ContentValues();
            values.put(Constant.COLUMN_ORDER_STATUS, 0);
            values.put(Constant.COLUMN_ORDER_USER_ID, order.getUser().getId());
            long id = sqLiteDatabase.insertOrThrow(Constant.TABLE_ORDER, null, values);
//            System.out.println(id);
            order.setId((int) id);
            values.clear();

            //insert items into order
            for(OrderItem orderItem : order.getOrderItems()){
                values.put(Constant.COLUMN_ORDER_ITEM_AMOUNT, orderItem.getAmount());
                values.put(Constant.COLUMN_ORDER_ITEM_DISCOUNT,orderItem.getDiscount());
                values.put(Constant.COLUMN_ORDER_ITEM_ORDER_ID, order.getId());
                values.put(Constant.COLUMN_ORDER_ITEM_ITEM_ID, orderItem.getItem().getId());
                sqLiteDatabase.insertOrThrow(Constant.TABLE_ORDER_ITEM, null, values);
                values.clear();
            }
            values.clear();

            //add shipment methods
            values.put(Constant.COLUMN_SHIPMENT_TYPENAME, order.getShipment().getTypeName());
            values.put(Constant.COLUMN_SHIPMENT_ORDER_ID, order.getId());
            long shipmentId = sqLiteDatabase.insertOrThrow(Constant.TABLE_SHIPMENT, null, values);
            order.getShipment().setId((int) shipmentId);
            values.clear();

            //add payment method
            values.put(Constant.COLUMN_PAYMENT_TYPE, order.getPayment().getType());
            values.put(Constant.COLUMN_PAYMENT_TYPENAME, order.getPayment().getTypeName());
            values.put(Constant.COLUMN_PAYMENT_TOTAL_EXPENSE, order.getPayment().getTotalExpense());
            values.put(Constant.COLUMN_PAYMENT_CARD_ID, order.getPayment().getCardId());
            values.put(Constant.COLUMN_PAYMENT_DATE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(order.getPayment().getDate()));
            values.put(Constant.COLUMN_PAYMENT_ORDER_ID, order.getId());
            long paymentId = sqLiteDatabase.insertOrThrow(Constant.TABLE_PAYMENT, null, values);
            order.getPayment().setId((int) paymentId);
            values.clear();

        }catch (SQLiteException e){
            e.printStackTrace();
        }
        System.out.println("createOrder(): New cart Id: " + order.getId());


        return order;
    }


}
