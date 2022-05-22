package com.example.mobile.view.UserActivity.main.fragments;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.R;
import com.example.mobile.controller.OrderDAO.OrderDAOImpl;
import com.example.mobile.controller.UserDAO.UserDAOImpl;
import com.example.mobile.model.order.OrderOfUser;
import com.example.mobile.utils.PreferenceUtils;
import com.example.mobile.view.UserActivity.order.OrderListAdapter;

import java.util.ArrayList;

public class OrderFragment extends Fragment {
    //views
    private RecyclerView rclOrderList;
    private OrderDAOImpl orderDAO;
    private UserDAOImpl userDAO;

    private Context context;
    private ArrayList<OrderOfUser> orderList;

    OrderListAdapter orderListAdapter;

    private ISendData iSendData;

    public OrderFragment(Context context) {
        this.context = context;
        iSendData = (ISendData) context;
        orderDAO = new OrderDAOImpl(context);
        userDAO = new UserDAOImpl(context);
        orderList = orderDAO.getAllOrders(userDAO.getUser(PreferenceUtils.getUsername(context)));

        orderListAdapter = new OrderListAdapter(context, orderList);
        orderListAdapter.setHasStableIds(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_fragment_layout, container, false);

        rclOrderList = view.findViewById(R.id.rcl_order_list);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(context);
        rclOrderList.setAdapter(orderListAdapter);
        rclOrderList.setLayoutManager(manager);
        rclOrderList.setHasFixedSize(true);

        return view;
    }

    public void updateOrderList(){
        String userName = PreferenceUtils.getUsername(context);
        if(userName!=null) Log.d(TAG, "updateCartData: " + userName);
        orderList.clear();
        orderList.addAll(orderDAO.getAllOrders(userDAO.getUser(userName)));
        orderListAdapter.notifyDataSetChanged();
    }
}
