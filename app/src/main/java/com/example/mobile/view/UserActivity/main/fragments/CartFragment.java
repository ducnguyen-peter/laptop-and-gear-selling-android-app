package com.example.mobile.view.UserActivity.main.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.R;
import com.example.mobile.controller.CartDAO.CartDAOImpl;
import com.example.mobile.controller.ItemDAO.ItemDAOImpl;
import com.example.mobile.controller.UserDAO.UserDAOImpl;
import com.example.mobile.model.Item.Item;
import com.example.mobile.model.cart.Cart;
import com.example.mobile.model.cart.CartItem;
import com.example.mobile.utils.PreferenceUtils;
import com.example.mobile.view.UserActivity.main.MainActivity;

import java.util.ArrayList;

public class CartFragment extends Fragment {
    private RecyclerView rclCartItems;
    private CheckBox cbAllCartItem;
    private TextView txtTotalCartCost;
    private Button btnBuy;

    private CartDAOImpl cartDAOImpl;
    private ItemDAOImpl itemDAOImpl;
    private UserDAOImpl userDAOImpl;
    private ListItemCartAdapter listItemCartAdapter;
    private Cart cart;

    private MainActivity mainActivity;

    public CartFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cart_fragment_layout, container, false);
        rclCartItems = view.findViewById(R.id.rcl_list_cartitem);
        cbAllCartItem = view.findViewById(R.id.cb_all_cart);
        txtTotalCartCost = view.findViewById(R.id.txt_total_cart);
        btnBuy = view.findViewById(R.id.btn_buy_cart);

        cartDAOImpl = new CartDAOImpl(mainActivity);
        itemDAOImpl = new ItemDAOImpl(mainActivity);
        userDAOImpl = new UserDAOImpl(mainActivity);

        cart = cartDAOImpl.getCartOfUser(userDAOImpl.getUser(PreferenceUtils.getUsername(mainActivity)));
        for(CartItem cartItem : cart.getCartItems()){
            cartItem.setItem(itemDAOImpl.getItemById(cartItem.getItem().getId()));
        }

        listItemCartAdapter = new ListItemCartAdapter(mainActivity, cart.getCartItems());
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mainActivity);
        rclCartItems.setAdapter(listItemCartAdapter);
        rclCartItems.setLayoutManager(manager);

        return view;
    }
}
