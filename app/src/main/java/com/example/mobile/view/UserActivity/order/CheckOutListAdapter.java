package com.example.mobile.view.UserActivity.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.R;
import com.example.mobile.model.Item.Item;
import com.example.mobile.model.order.OrderItem;

import java.util.ArrayList;
import java.util.Locale;

public class CheckOutListAdapter extends RecyclerView.Adapter<CheckOutListAdapter.CheckOutListViewHolder>{
    private Context context;
    private ArrayList<OrderItem> orderItemList;

    public CheckOutListAdapter(Context context, ArrayList<OrderItem> orderItemList) {
        this.context = context;
        this.orderItemList = orderItemList;
    }

    @NonNull
    @Override
    public CheckOutListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View checkOutItemView = inflater.inflate(R.layout.order_row_item_layout, parent, false);
        CheckOutListViewHolder checkOutListViewHolder = new CheckOutListViewHolder(checkOutItemView);
        return checkOutListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CheckOutListViewHolder holder, int position) {
        OrderItem orderItem = orderItemList.get(position);
        Item item = orderItem.getItem();
        holder.imgItemOrder.setImageResource(context.getResources().getIdentifier(item.getElectronics().getImageLink().trim(), "drawable", context.getPackageName()));
        holder.txtItemOrderName.setText(item.getElectronics().getName());
        holder.txtItemOrderPrice.setText(String.format(Locale.ENGLISH, "Price of 1 item: %.1fđ", item.getUnitPrice()));
        holder.txtItemOrderAmount.setText(String.format(Locale.ENGLISH, "Amount: %d", orderItem.getAmount()));
        holder.txtTotalItemOrder.setText(String.format(Locale.ENGLISH, "%.1fđ", item.getUnitPrice()* orderItem.getAmount()));

    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

    public static class CheckOutListViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgItemOrder;
        public TextView txtItemOrderName;
        public TextView txtItemOrderPrice;
        public TextView txtItemOrderAmount;
        public TextView txtTotalItemOrder;

        public CheckOutListViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItemOrder = itemView.findViewById(R.id.img_item_order);
            txtItemOrderName = itemView.findViewById(R.id.txt_itemorder_name);
            txtItemOrderPrice = itemView.findViewById(R.id.txt_itemorder_price);
            txtItemOrderAmount = itemView.findViewById(R.id.txt_itemorder_amount);
            txtTotalItemOrder = itemView.findViewById(R.id.txt_total_itemorder);
        }
    }
}
