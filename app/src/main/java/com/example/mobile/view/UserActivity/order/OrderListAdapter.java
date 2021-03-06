package com.example.mobile.view.UserActivity.order;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.R;
import com.example.mobile.model.order.OrderOfUser;

import java.util.ArrayList;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderListViewHolder>{
    private Context context;
    private ArrayList<OrderOfUser> orderList;

    public OrderListAdapter(Context context, ArrayList<OrderOfUser> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View orderRowView = inflater.inflate(R.layout.order_item_row_layout, parent, false);
        OrderListViewHolder orderListViewHolder = new OrderListViewHolder(orderRowView);
        return orderListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListViewHolder holder, int position) {
        OrderOfUser order = orderList.get(position);
        holder.txtOrderId.setText("Order Id: " + order.getId());
        holder.orderRowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OrderDetailsActivity.class);
                intent.putExtra("ORDER", order);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderListViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout orderRowLayout;
        public TextView txtOrderId;

        public OrderListViewHolder(@NonNull View itemView) {
            super(itemView);
            orderRowLayout = itemView.findViewById(R.id.order_row);
            txtOrderId = itemView.findViewById(R.id.txt_order_id);
        }

    }
}
