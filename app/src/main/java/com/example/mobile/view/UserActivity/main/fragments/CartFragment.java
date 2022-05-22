package com.example.mobile.view.UserActivity.main.fragments;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.R;
import com.example.mobile.controller.CartDAO.CartDAOImpl;
import com.example.mobile.controller.ItemDAO.ItemDAOImpl;
import com.example.mobile.controller.UserDAO.UserDAOImpl;
import com.example.mobile.model.cart.Cart;
import com.example.mobile.model.cart.CartItem;
import com.example.mobile.utils.PreferenceUtils;
import com.example.mobile.view.UserActivity.main.activities.MainActivity;
import com.example.mobile.view.UserActivity.order.CheckOutActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

public class CartFragment extends Fragment{
    //views
    private RecyclerView rclCartItems;
    private CheckBox cbAllCartItem;
    private TextView txtTotalCartCost;
    private Button btnBuy;

    //data and DAO
    private CartDAOImpl cartDAOImpl;
    private ItemDAOImpl itemDAOImpl;
    private UserDAOImpl userDAOImpl;
    private ListItemCartAdapter listItemCartAdapter;
    private Cart cart;
    private ArrayList<CartItem> cartItemsList;
    private ArrayList<CartItem> selectedCartItems;
    private float cost;
    ActivityResultLauncher<Intent> activityLauncher;

    //parents view and interface
    private MainActivity mainActivity;
    private ISendData iSendData;

    public CartFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        iSendData = (ISendData) this.mainActivity;
        cartDAOImpl = new CartDAOImpl(mainActivity);
        itemDAOImpl = new ItemDAOImpl(mainActivity);
        userDAOImpl = new UserDAOImpl(mainActivity);

        cart = cartDAOImpl.getCartOfUser(userDAOImpl.getUser(PreferenceUtils.getUsername(mainActivity)));
        cartItemsList = cart.getCartItems();
        for(CartItem cartItem : cartItemsList){
            cartItem.setItem(itemDAOImpl.getItemById(cartItem.getItem().getId()));
        }

        listItemCartAdapter = new ListItemCartAdapter(mainActivity, cartItemsList, (ISendData) mainActivity);
        listItemCartAdapter.setHasStableIds(true);

        selectedCartItems = new ArrayList<>();

        cost = getTotalCartCost();

        selectedCartItems = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cart_fragment_layout, container, false);
        rclCartItems = view.findViewById(R.id.rcl_list_cartitem);
        cbAllCartItem = view.findViewById(R.id.cb_all_cart);
        txtTotalCartCost = view.findViewById(R.id.txt_total_cart);
        btnBuy = view.findViewById(R.id.btn_buy_cart);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(mainActivity);
        rclCartItems.setAdapter(listItemCartAdapter);
        rclCartItems.setLayoutManager(manager);
        rclCartItems.setHasFixedSize(true);

        cbAllCartItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b && selectedCartItems.size()==0)
                    listItemCartAdapter.checkAllCB(true);
                if(!b && selectedCartItems.size()==cartItemsList.size()){
                    listItemCartAdapter.checkAllCB(false);
                }
            }
        });

        txtTotalCartCost.setText(String.format(Locale.ENGLISH, "Total: %.1fđ", cost));
        if(selectedCartItems.size()<=0) btnBuy.setEnabled(false);
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, CheckOutActivity.class);
                intent.putExtra("SELECTED_CART_ITEMS",selectedCartItems);
                intent.putExtra("TOTAL_COST", cost);
                activityLauncher.launch(intent);
            }
        });

        activityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode()== Activity.RESULT_OK){
                            updateCartData();
                            iSendData.updateOrderList();
                        }
                        Log.d(TAG, "onActivityResult: Failed to update cart");
                    }
                }
        );

        return view;
    }

    public void updateCartData(){

        String userName = PreferenceUtils.getUsername(mainActivity);
        if(userName!=null) Log.d(TAG, "updateCartData: " + userName);
        cart = cartDAOImpl.getCartOfUser(userDAOImpl.getUser(userName));
        cartItemsList.clear();
        cartItemsList.addAll(cart.getCartItems());
        for(CartItem cartItem : cartItemsList){
            cartItem.setItem(itemDAOImpl.getItemById(cartItem.getItem().getId()));
        }
        listItemCartAdapter.notifyDataSetChanged();
    }

    public void addSelectedCartItems(CartItem cartItem) {
        selectedCartItems.add(cartItem);
        if(selectedCartItems.size()==cartItemsList.size()){
            cbAllCartItem.setChecked(true);
        }
        cost = getTotalCartCost();
        txtTotalCartCost.setText(String.format(Locale.ENGLISH, "Total: %.1fđ", cost));
        btnBuy.setEnabled(true);
        System.out.println("Number of selected items after adding: " + selectedCartItems.size());
    }

    public void removeSelectedCartItems(CartItem cartItem) {
        removeSelectedItemArray(cartItem.getItem().getId());
        cost = getTotalCartCost();
        txtTotalCartCost.setText(String.format(Locale.ENGLISH, "Total: %.1fđ", cost));
        if(selectedCartItems.size()==0) btnBuy.setEnabled(false);
        System.out.println("Number of selected items after removing: " + selectedCartItems.size());
    }

    public void updateSelectedCartItemsAmount(CartItem cartItem, int amount) {
        updateSelectedItemAmount(cartItem.getItem().getId(), amount);
    }

    public void deleteCartItem(CartItem cartItem) {
        cartDAOImpl.deleteCartItem(cartItem, cart.getId());
        removeSelectedItemArray(cartItem.getItem().getId());
        updateCartData();
    }

    private float getTotalCartCost(){
        cost = 0;
        for(CartItem cartItem : selectedCartItems){
            cost += (cartItem.getItem().getUnitPrice()*cartItem.getAmount());
        }
        return cost;
    }

    private void updateSelectedItemAmount(int itemId, int amount){
        for (CartItem cartItem : selectedCartItems) {
            if (cartItem.getItem().getId() == itemId) {
                cartItem.setAmount(amount);
            }
        }
        cost = getTotalCartCost();
        txtTotalCartCost.setText(String.format(Locale.ENGLISH, "Total: %.1fđ", cost));
    }

    private void removeSelectedItemArray(int itemId){
        Iterator<CartItem> cartItemIterator = selectedCartItems.iterator();
        while(cartItemIterator.hasNext()){
            if(cartItemIterator.next().getItem().getId()==itemId){
                cartItemIterator.remove();
                cbAllCartItem.setChecked(false);
            }
        }
        cost = getTotalCartCost();
        txtTotalCartCost.setText(String.format(Locale.ENGLISH, "Total: %.1fđ", cost));
    }

}
