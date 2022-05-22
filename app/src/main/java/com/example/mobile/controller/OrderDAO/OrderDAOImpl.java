package com.example.mobile.controller.OrderDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.mobile.model.Item.Item;
import com.example.mobile.model.order.OrderOfUser;
import com.example.mobile.model.order.OrderItem;
import com.example.mobile.model.order.Payment;
import com.example.mobile.model.order.Shipment;
import com.example.mobile.model.user.User;
import com.example.mobile.utils.Constant;
import com.example.mobile.utils.DBHelper;

import java.util.ArrayList;

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

    public OrderOfUser createOrder(OrderOfUser orderOfUser){
        try {
            //create blank orderOfUser
            ContentValues values = new ContentValues();
            values.put(Constant.COLUMN_ORDER_STATUS, OrderOfUser.ORDER_STATUS_PENDING);
            values.put(Constant.COLUMN_ORDER_USER_ID, orderOfUser.getUser().getId());

            long id = sqLiteDatabase.insertOrThrow(Constant.TABLE_ORDER, null, values);
//            System.out.println(id);
            orderOfUser.setId((int) id);
            values.clear();

            //insert items into orderOfUser
            for(OrderItem orderItem : orderOfUser.getOrderItems()){
                values.put(Constant.COLUMN_ORDER_ITEM_QUANTITY, orderItem.getAmount());
                values.put(Constant.COLUMN_ORDER_ITEM_DISCOUNT,orderItem.getDiscount());
                values.put(Constant.COLUMN_ORDER_ITEM_ORDER_ID, orderOfUser.getId());
                values.put(Constant.COLUMN_ORDER_ITEM_ITEM_ID, orderItem.getItem().getId());
                sqLiteDatabase.insertOrThrow(Constant.TABLE_ORDER_ITEM, null, values);
                values.clear();
            }
            values.clear();

            //add shipment methods
            values.put(Constant.COLUMN_SHIPMENT_TYPENAME, orderOfUser.getShipment().getTypeName());
            values.put(Constant.COLUMN_SHIPMENT_ORDER_ID, orderOfUser.getId());
            long shipmentId = sqLiteDatabase.insertOrThrow(Constant.TABLE_SHIPMENT, null, values);
            orderOfUser.getShipment().setId((int) shipmentId);
            values.clear();

            //add payment method
            values.put(Constant.COLUMN_PAYMENT_TYPE, orderOfUser.getPayment().getType());
            values.put(Constant.COLUMN_PAYMENT_TYPENAME, orderOfUser.getPayment().getTypeName());
            values.put(Constant.COLUMN_PAYMENT_AMOUNT, orderOfUser.getPayment().getTotalExpense());
            values.put(Constant.COLUMN_PAYMENT_CARD_ID, orderOfUser.getPayment().getCardId());
            values.put(Constant.COLUMN_PAYMENT_ORDER_ID, orderOfUser.getId());
            long paymentId = sqLiteDatabase.insertOrThrow(Constant.TABLE_PAYMENT, null, values);
            orderOfUser.getPayment().setId((int) paymentId);
            values.clear();

        }catch (SQLiteException e){
            e.printStackTrace();
        }
        System.out.println("createOrder(): New cart Id: " + orderOfUser.getId());


        return orderOfUser;
    }

    public ArrayList<OrderOfUser> getAllOrders(User user){
        ArrayList<OrderOfUser> orderList = new ArrayList<>();
        String queryOrder = "SELECT [Order].Id, [Order].Status, " +
                "Shipment.Id, Shipment.TypeName," +
                "Payment.Id, Payment.Type, Payment.TypeName, Payment.Amount, Payment.CardID, Payment.Date FROM [Order], Payment, Shipment" +
                " WHERE UserId = ? AND Payment.OrderId = [Order].Id AND Shipment.OrderId = [Order].Id;";
        Cursor cursor = sqLiteDatabase.rawQuery(queryOrder, new String[]{Integer.toString(user.getId())});
        int cursorCount = cursor.getCount();
        if (cursorCount<=0) return orderList;
        cursor.moveToFirst();
        OrderOfUser order = new OrderOfUser();
        while(!cursor.isAfterLast()){
            order = cursorToOrder(cursor);
            orderList.add(order);
            cursor.moveToNext();
        }
        return orderList;
    }

    public ArrayList<OrderItem> getOrderItem(OrderOfUser order){
        ArrayList<OrderItem> orderItems = new ArrayList<>();
        String queryOrderItem = "SELECT OrderItem.Quantity, OrderItem.Discount, OrderItem.ItemId \n" +
                "FROM [Order], OrderItem, Item \n" +
                "WHERE [Order].Id = OrderItem.OrderId AND OrderItem.ItemId = Item.Id AND [Order].Id = ?;";
        Cursor cursor = sqLiteDatabase.rawQuery(queryOrderItem, new String[]{Integer.toString(order.getId())});
        cursor.moveToFirst();
        OrderItem orderItem;
        while(!cursor.isAfterLast()){
            orderItem = cursorToOrderItem(cursor);
            orderItems.add(orderItem);
            cursor.moveToNext();
        }
        return orderItems;
    }


    private OrderOfUser cursorToOrder(Cursor cursor){
        OrderOfUser order = new OrderOfUser();
        Payment payment = new Payment();
        Shipment shipment = new Shipment();
        order.setId(cursor.getInt(0));
        order.setStatus(cursor.getString(1));

        shipment.setId(cursor.getInt(2));
        shipment.setTypeName(cursor.getString(3));
        payment.setId(cursor.getInt(4));
        payment.setType(cursor.getInt(5));
        payment.setTypeName(cursor.getString(6));
        payment.setTotalExpense(cursor.getFloat(7));
        payment.setCardId(cursor.getString(8));

        order.setShipment(shipment);
        order.setPayment(payment);
        return order;
    }

    private OrderItem cursorToOrderItem(Cursor cursor){
        OrderItem orderItem = new OrderItem();
        orderItem.setAmount(cursor.getInt(0));
        orderItem.setDiscount(cursor.getFloat(1));
        Item item = new Item();
        item.setId(cursor.getInt(cursor.getColumnIndexOrThrow("ItemId")));

        orderItem.setItem(item);

        return orderItem;
    }

}
