package com.example.mobile.view.UserActivity.order;

import static android.service.controls.ControlsProviderService.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobile.R;
import com.example.mobile.controller.UserDAO.UserDAOImpl;
import com.example.mobile.model.cart.CartItem;
import com.example.mobile.model.order.Order;
import com.example.mobile.model.order.OrderItem;
import com.example.mobile.model.order.Payment;
import com.example.mobile.model.order.Shipment;
import com.example.mobile.model.user.User;
import com.example.mobile.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class CheckOutActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private RecyclerView rclCheckoutItems;
    private Spinner spnShipment;
    private Spinner spnPayment;
    private TextView txtShipFee;
    private TextView txtTotalCost;
    private Button btnOrder;

    private ArrayList<CartItem> selectedCartItems;
    private UserDAOImpl userDAO;

    private User user;
    private Order order;
    private Payment payment;
    private Shipment shipment;
    private ArrayList<OrderItem> listOrderItem;

    public CheckOutActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        rclCheckoutItems = findViewById(R.id.rcl_checkout_item);
        spnShipment = findViewById(R.id.spn_shipment);
        spnPayment = findViewById(R.id.spn_payment);
        txtShipFee = findViewById(R.id.txt_shipping_fee);
        txtTotalCost = findViewById(R.id.txt_total_order);
        btnOrder = findViewById(R.id.btn_order);

        initData();

        for(CartItem cartItem : selectedCartItems){
            listOrderItem.add(new OrderItem(cartItem));
        }

        CheckOutListAdapter checkOutListAdapter = new CheckOutListAdapter(this, listOrderItem);
        checkOutListAdapter.setHasStableIds(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);

        rclCheckoutItems.setAdapter(checkOutListAdapter);
        rclCheckoutItems.setLayoutManager(manager);
        rclCheckoutItems.setHasFixedSize(true);

        ArrayAdapter<CharSequence> shipmentSpnAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_shipment, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> paymentSpnAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_payment, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spnShipment.setAdapter(shipmentSpnAdapter);
        spnPayment.setAdapter(paymentSpnAdapter);

        spnPayment.setOnItemSelectedListener(this);
        spnShipment.setOnItemSelectedListener(this);
    }

    public void initData(){
        Intent intent = this.getIntent();
        selectedCartItems = intent.getParcelableArrayListExtra("SELECTED_CART_ITEMS");
        listOrderItem = new ArrayList<>();

        userDAO = new UserDAOImpl(this);
        user = userDAO.getUser(PreferenceUtils.getUsername(this));
        payment = new Payment();
        shipment = new Shipment();
        listOrderItem = new ArrayList<>();
        order = new Order(Order.ORDER_STATUS_PENDING, user, listOrderItem, payment, shipment);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId() == R.id.spn_payment){
            String[] arrPaymentType = getResources().getStringArray(R.array.spinner_payment);
            order.getPayment().setType(i);
            order.getPayment().setTypeName(arrPaymentType[i]);
        } else if(adapterView.getId() == R.id.spn_shipment){
            String[] arrShipmentType = getResources().getStringArray(R.array.spinner_shipment);
            order.getShipment().setTypeName(arrShipmentType[i]);
            txtShipFee.setText(String.format(Locale.ENGLISH, "Ship fee: %.1fđ", order.getShipment().getShipPrice()));
            Log.d(TAG, "onItemSelected: order shipment price: " + order.getShipment().getShipPrice());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}