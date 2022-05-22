package com.example.mobile.view.UserActivity.order;

import static android.service.controls.ControlsProviderService.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobile.R;
import com.example.mobile.controller.CartDAO.CartDAOImpl;
import com.example.mobile.controller.OrderDAO.OrderDAOImpl;
import com.example.mobile.controller.UserDAO.UserDAOImpl;
import com.example.mobile.model.cart.CartItem;
import com.example.mobile.model.order.OrderOfUser;
import com.example.mobile.model.order.OrderItem;
import com.example.mobile.model.order.Payment;
import com.example.mobile.model.order.Shipment;
import com.example.mobile.model.user.User;
import com.example.mobile.utils.PreferenceUtils;
import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.approve.Approval;
import com.paypal.checkout.approve.OnApprove;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.config.PaymentButtonIntent;
import com.paypal.checkout.config.SettingsConfig;
import com.paypal.checkout.createorder.CreateOrder;
import com.paypal.checkout.createorder.CreateOrderActions;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.OrderIntent;
import com.paypal.checkout.createorder.ProcessingInstruction;
import com.paypal.checkout.createorder.UserAction;
import com.paypal.checkout.order.Amount;
import com.paypal.checkout.order.AppContext;
import com.paypal.checkout.order.CaptureOrderResult;
import com.paypal.checkout.order.OnCaptureComplete;
import com.paypal.checkout.order.Order;
import com.paypal.checkout.order.PurchaseUnit;
import com.paypal.checkout.paymentbutton.PayPalButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Locale;

public class CheckOutActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private RecyclerView rclCheckoutItems;
    private EditText edtAddress;
    private Spinner spnShipment;
    private Spinner spnPayment;
    private TextView txtShipFee;
    private TextView txtTotalCost;
    private Button btnOrder;

    private ArrayList<CartItem> selectedCartItems;
    private UserDAOImpl userDAO;
    private OrderDAOImpl orderDAO;
    private CartDAOImpl cartDAO;

    private User user;
    private OrderOfUser orderOfUser;
    private Payment payment;
    private Shipment shipment;
    private ArrayList<OrderItem> listOrderItem;
    private float totalCost;

    //paypal api
    private static final String YOUR_CLIENT_ID = "AZdI_IPRzDx_UJQt0ngkxmuDR500O-wvQwTgIRiXl5qCFQ_0cgLCekrcX-PIIx-5TQAz4vQzAi5enduu";
    private PayPalButton payPalButton;


    public CheckOutActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        initView();
        initData();

        for(CartItem cartItem : selectedCartItems){
            listOrderItem.add(new OrderItem(cartItem));
        }
        orderOfUser.setOrderItems(listOrderItem);

        CheckOutListAdapter checkOutListAdapter = new CheckOutListAdapter(this, listOrderItem);
        checkOutListAdapter.setHasStableIds(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);

        rclCheckoutItems.setAdapter(checkOutListAdapter);
        rclCheckoutItems.setLayoutManager(manager);
        rclCheckoutItems.setHasFixedSize(true);

        if(user.getAddress()!=null) edtAddress.setText(user.getAddress());

        ArrayAdapter<CharSequence> shipmentSpnAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_shipment, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> paymentSpnAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_payment, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spnShipment.setAdapter(shipmentSpnAdapter);
        spnPayment.setAdapter(paymentSpnAdapter);

        spnPayment.setOnItemSelectedListener(this);
        spnShipment.setOnItemSelectedListener(this);

        btnOrder.setOnClickListener(this);
        CheckoutConfig config = new CheckoutConfig(
                getApplication(),
                YOUR_CLIENT_ID,
                Environment.SANDBOX,
                "com.example.mobile://paypalpay",
                CurrencyCode.USD,
                UserAction.PAY_NOW,
                PaymentButtonIntent.AUTHORIZE,
                new SettingsConfig(true, false)
        );
        PayPalCheckout.setConfig(config);
        payPalButton.setup(
                new CreateOrder() {
                    @Override
                    public void create(@NotNull CreateOrderActions createOrderActions) {
                        ArrayList<PurchaseUnit> purchaseUnits = new ArrayList<>();
                        purchaseUnits.add(
                                new PurchaseUnit.Builder()
                                        .amount(
                                                new Amount.Builder()
                                                        .currencyCode(CurrencyCode.USD)
                                                        .value(String.format(Locale.ENGLISH, "%.2f", (totalCost+orderOfUser.getShipment().getShipPrice())/23000))
                                                        .build()
                                        )
                                        .build()
                        );
                        Order order = new Order(
                                OrderIntent.CAPTURE,
                                new AppContext.Builder()
                                        .userAction(UserAction.PAY_NOW)
                                        .build(),
                                purchaseUnits,
                                ProcessingInstruction.ORDER_COMPLETE_ON_PAYMENT_APPROVAL
                        );
                        createOrderActions.create(order, (CreateOrderActions.OnOrderCreated) null);
                    }
                },
                new OnApprove() {
                    @Override
                    public void onApprove(@NotNull Approval approval) {
                        approval.getOrderActions().capture(new OnCaptureComplete() {
                            @Override
                            public void onCaptureComplete(@NotNull CaptureOrderResult result) {
                                Log.i("CaptureOrder", String.format("CaptureOrderResult: %s", result));
                                orderOfUser.getPayment().setTotalExpense(totalCost+orderOfUser.getShipment().getShipPrice());
                                orderDAO.createOrder(orderOfUser);
                                for(CartItem cartItem : selectedCartItems){
                                    cartDAO.deleteCartItem(cartItem, cartDAO.getCartOfUser(user).getId());
                                }
                                CheckOutActivity.this.finish();
                            }
                        });
                    }
                }
        );
    }

    public void initView(){
        rclCheckoutItems = findViewById(R.id.rcl_checkout_item);
        edtAddress = findViewById(R.id.edt_order_address);
        spnShipment = findViewById(R.id.spn_shipment);
        spnPayment = findViewById(R.id.spn_payment);
        txtShipFee = findViewById(R.id.txt_shipping_fee);
        txtTotalCost = findViewById(R.id.txt_total_order);
        btnOrder = findViewById(R.id.btn_order);
        payPalButton = findViewById(R.id.payPalButton);
    }

    public void initData(){
        Intent intent = this.getIntent();
        selectedCartItems = intent.getParcelableArrayListExtra("SELECTED_CART_ITEMS");

        totalCost = intent.getFloatExtra("TOTAL_COST", -1);

        userDAO = new UserDAOImpl(this);
        orderDAO = new OrderDAOImpl(this);
        cartDAO = new CartDAOImpl(this);

        user = userDAO.getUser(PreferenceUtils.getUsername(this));
        payment = new Payment();
        shipment = new Shipment();
        listOrderItem = new ArrayList<>();
        orderOfUser = new OrderOfUser(OrderOfUser.ORDER_STATUS_PENDING, user, listOrderItem, payment, shipment);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId() == R.id.spn_payment){
            String[] arrPaymentType = getResources().getStringArray(R.array.spinner_payment);
            orderOfUser.getPayment().setType(i);
            orderOfUser.getPayment().setTypeName(arrPaymentType[i]);
        } else if(adapterView.getId() == R.id.spn_shipment){
            String[] arrShipmentType = getResources().getStringArray(R.array.spinner_shipment);
            orderOfUser.getShipment().setTypeName(arrShipmentType[i]);
            txtShipFee.setText(String.format(Locale.ENGLISH, "Ship fee: %.1fđ", orderOfUser.getShipment().getShipPrice()));
            Log.d(TAG, "onItemSelected: orderOfUser shipment price: " + orderOfUser.getShipment().getShipPrice());
            txtTotalCost.setText(String.format(Locale.ENGLISH, "Total: %.1fđ", totalCost + orderOfUser.getShipment().getShipPrice()));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        if(view==btnOrder){
            if(orderOfUser.getPayment().getType()==0){
                orderDAO.createOrder(orderOfUser);
                for(CartItem cartItem : selectedCartItems){
                    cartDAO.deleteCartItem(cartItem, cartDAO.getCartOfUser(user).getId());
                }
                setResult(Activity.RESULT_OK);
                this.finish();
            }
        }
    }



    private void handleError(int statusCode) {
        Log.e("loadPaymentData failed", String.format("Error code: %d", statusCode));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}