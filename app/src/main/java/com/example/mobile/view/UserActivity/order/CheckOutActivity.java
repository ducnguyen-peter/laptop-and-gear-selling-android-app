package com.example.mobile.view.UserActivity.order;

import static android.service.controls.ControlsProviderService.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobile.R;
import com.example.mobile.controller.UserDAO.UserDAOImpl;
import com.example.mobile.model.cart.CartItem;
import com.example.mobile.model.order.OrderOfUser;
import com.example.mobile.model.order.OrderItem;
import com.example.mobile.model.order.Payment;
import com.example.mobile.model.order.Shipment;
import com.example.mobile.model.user.User;
import com.example.mobile.utils.PaymentsUtil;
import com.example.mobile.utils.PreferenceUtils;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class CheckOutActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private RecyclerView rclCheckoutItems;
    private Spinner spnShipment;
    private Spinner spnPayment;
    private TextView txtShipFee;
    private TextView txtTotalCost;
    private Button btnOrder;
    //google order buttor
    private RelativeLayout btnOrderGoogle;

    private ArrayList<CartItem> selectedCartItems;
    private UserDAOImpl userDAO;

    private User user;
    private OrderOfUser orderOfUser;
    private Payment payment;
    private Shipment shipment;
    private ArrayList<OrderItem> listOrderItem;
    private float totalCost;

    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;

    private static final long SHIPPING_COST_CENTS = 90 * PaymentsUtil.CENTS_IN_A_UNIT.longValue();

    // A client for interacting with the Google Pay API.
    private PaymentsClient paymentsClient;


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

        ArrayAdapter<CharSequence> shipmentSpnAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_shipment, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> paymentSpnAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_payment, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spnShipment.setAdapter(shipmentSpnAdapter);
        spnPayment.setAdapter(paymentSpnAdapter);

        spnPayment.setOnItemSelectedListener(this);
        spnShipment.setOnItemSelectedListener(this);

        btnOrder.setOnClickListener(this);
        paymentsClient = PaymentsUtil.createPaymentsClient(this);
        possiblyShowGooglePayButton();
    }

    public void initView(){
        rclCheckoutItems = findViewById(R.id.rcl_checkout_item);
        spnShipment = findViewById(R.id.spn_shipment);
        spnPayment = findViewById(R.id.spn_payment);
        txtShipFee = findViewById(R.id.txt_shipping_fee);
        txtTotalCost = findViewById(R.id.txt_total_order);
        btnOrder = findViewById(R.id.btn_order);
        btnOrderGoogle = findViewById(R.id.btn_order_with_ggpay);
        btnOrderGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPayment(view);
            }
        });
    }

    public void initData(){
        Intent intent = this.getIntent();
        selectedCartItems = intent.getParcelableArrayListExtra("SELECTED_CART_ITEMS");
        listOrderItem = new ArrayList<>();

        totalCost = intent.getFloatExtra("TOTAL_COST", -1);

        userDAO = new UserDAOImpl(this);
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
            if(i==1){

            }
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

    }

    private void possiblyShowGooglePayButton() {

        final Optional<JSONObject> isReadyToPayJson = PaymentsUtil.getIsReadyToPayRequest();
        if (!isReadyToPayJson.isPresent()) {
            return;
        }

        // The call to isReadyToPay is asynchronous and returns a Task. We need to provide an
        // OnCompleteListener to be triggered when the result of the call is known.
        IsReadyToPayRequest request = IsReadyToPayRequest.fromJson(isReadyToPayJson.get().toString());
        Task<Boolean> task = paymentsClient.isReadyToPay(request);
        task.addOnCompleteListener(this,
                new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            setGooglePayAvailable(task.getResult());
                        } else {
                            Log.w("isReadyToPay failed", task.getException());
                        }
                    }
                });
    }

    private void setGooglePayAvailable(boolean available) {
        if (available) {
            btnOrderGoogle.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, R.string.googlepay_status_unavailable, Toast.LENGTH_LONG).show();
        }
    }

    public void requestPayment(View view) {

        // Disables the button to prevent multiple clicks.
        btnOrderGoogle.setClickable(false);

        // The price provided to the API should include taxes and shipping.
        // This price is not displayed to the user.
        try {
            long priceCents = Math.round(totalCost);

            Optional<JSONObject> paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(priceCents);
            if (!paymentDataRequestJson.isPresent()) {
                return;
            }

            PaymentDataRequest request =
                    PaymentDataRequest.fromJson(paymentDataRequestJson.get().toString());

            // Since loadPaymentData may show the UI asking the user to select a payment method, we use
            // AutoResolveHelper to wait for the user interacting with it. Once completed,
            // onActivityResult will be called with the result.
            if (request != null) {
                AutoResolveHelper.resolveTask(
                        paymentsClient.loadPaymentData(request),
                        this, LOAD_PAYMENT_DATA_REQUEST_CODE);
            }

        } catch (Exception e) {
            throw new RuntimeException("The price cannot be deserialized from the JSON object.");
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // value passed in AutoResolveHelper
            case LOAD_PAYMENT_DATA_REQUEST_CODE:
                switch (resultCode) {

                    case Activity.RESULT_OK:
                        PaymentData paymentData = PaymentData.getFromIntent(data);
                        handlePaymentSuccess(paymentData);
                        break;

                    case Activity.RESULT_CANCELED:
                        // The user cancelled the payment attempt
                        break;

                    case AutoResolveHelper.RESULT_ERROR:
                        Status status = AutoResolveHelper.getStatusFromIntent(data);
                        handleError(status.getStatusCode());
                        break;
                }

                // Re-enables the Google Pay payment button.
                btnOrderGoogle.setClickable(true);
        }
    }

    private void handleError(int statusCode) {
        Log.e("loadPaymentData failed", String.format("Error code: %d", statusCode));
    }

    private void handlePaymentSuccess(PaymentData paymentData){
        final String paymentInfo = paymentData.toJson();
        if (paymentInfo == null) {
            return;
        }
        try {
            JSONObject paymentMethodData = new JSONObject(paymentInfo).getJSONObject("paymentMethodData");
            // If the gateway is set to "example", no payment information is returned - instead, the
            // token will only consist of "examplePaymentMethodToken".

            final JSONObject tokenizationData = paymentMethodData.getJSONObject("tokenizationData");
            final String token = tokenizationData.getString("token");
            final JSONObject info = paymentMethodData.getJSONObject("info");
            final String billingName = info.getJSONObject("billingAddress").getString("name");
            Toast.makeText(
                    this, getString(R.string.payments_show_name, billingName),
                    Toast.LENGTH_LONG).show();

            // Logging token string.
            Log.d("Google Pay token: ", token);

        } catch (JSONException e) {
            throw new RuntimeException("The selected garment cannot be parsed from the list of elements");
        }
    }

}