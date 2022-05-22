package com.example.mobile.view.UserActivity.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.mobile.R;
import com.example.mobile.controller.ItemDAO.ItemDAOImpl;
import com.example.mobile.controller.OrderDAO.OrderDAOImpl;
import com.example.mobile.model.order.OrderItem;
import com.example.mobile.model.order.OrderOfUser;
import com.example.mobile.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.Locale;

public class OrderDetailsActivity extends AppCompatActivity {
    private OrderOfUser order;
    private OrderDAOImpl orderDAO;
    private ItemDAOImpl itemDAO;
    private RecyclerView rclOrderItems;
    private TextView txtShipment;
    private TextView txtPayment;
    private TextView txtUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        rclOrderItems = findViewById(R.id.rcl_order_item);
        txtPayment = findViewById(R.id.txt_payment_order_details);
        txtShipment = findViewById(R.id.txt_shipment_order_details);
        txtUsername = findViewById(R.id.txt_order_id_username);

        orderDAO = new OrderDAOImpl(this);
        itemDAO = new ItemDAOImpl(this);

        Intent intent = getIntent();
        if(intent.hasExtra("ORDER")){
            order = (OrderOfUser) intent.getSerializableExtra("ORDER");
        }
        order.setOrderItems(orderDAO.getOrderItem(order));
        ArrayList<OrderItem> orderItems = order.getOrderItems();
        for(OrderItem orderItem : orderItems){
            orderItem.setItem(itemDAO.getItemById(orderItem.getItem().getId()));
        }

        CheckOutListAdapter adapter = new CheckOutListAdapter(this, orderItems);
        adapter.setHasStableIds(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        rclOrderItems.setAdapter(adapter);
        rclOrderItems.setLayoutManager(manager);
        rclOrderItems.setHasFixedSize(true);

        txtUsername.setText("User: "+ PreferenceUtils.getUsername(this) + ", Order Id: " + order.getId());
        txtShipment.setText("Shipment type: " + order.getShipment().getTypeName());
        txtPayment.setText("Payment type: "+order.getPayment().getTypeName() + ", Amount: " + String.format(Locale.ENGLISH, "%.1fđ",order.getPayment().getTotalExpense()));
    }
}