package com.example.mobile.view.UserActivity.main.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.R;
import com.example.mobile.model.Item.Item;
import com.example.mobile.model.cart.CartItem;

import java.util.ArrayList;
import java.util.Locale;

public class ListItemCartAdapter extends RecyclerView.Adapter<ListItemCartAdapter.ViewHolder>{
    private Context context;
    private ArrayList<CartItem> cartItemList;

    public ListItemCartAdapter(Context context, ArrayList<CartItem> cartItemList) {
        this.context = context;
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View cartItemView = inflater.inflate(R.layout.cart_row_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(cartItemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        Item item = cartItem.getItem();
//        holder.cbItemCart.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) context);
        holder.imgBtnItemCart.setImageResource(context.getResources().getIdentifier(item.getElectronics().getImageLink().trim(), "drawable", context.getPackageName()));
        holder.txtItemCartName.setText(item.getElectronics().getName());
        holder.txtItemCartPrice.setText(String.format(Locale.ENGLISH, "%.1fđ", item.getUnitPrice()));
        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.amount>1) {
                    holder.amount-=1;
                    holder.edtAmount.setText(String.format(Locale.ENGLISH, "%d", holder.amount));
                }
            }
        });
        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.amount<=item.getQuantity()) {
                    holder.amount+=1;
                    holder.edtAmount.setText(String.format(Locale.ENGLISH, "%d", holder.amount));
                } else{
                    Toast.makeText(context, "You can't buy more than available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.edtAmount.setText(String.format(Locale.ENGLISH, "%d", holder.amount));
        holder.txtTotalItemCost.setText(String.format(Locale.ENGLISH, "%.1fđ", item.getUnitPrice()));
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CheckBox cbItemCart;
        public ImageButton imgBtnItemCart;
        public TextView txtItemCartName;
        public TextView txtItemCartPrice;
        public Button btnMinus;
        public Button btnPlus;
        public EditText edtAmount;
        public TextView txtTotalItemCost;
        public int amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cbItemCart = itemView.findViewById(R.id.cb_item_cart);
            imgBtnItemCart = itemView.findViewById(R.id.btn_item_cart);
            txtItemCartName = itemView.findViewById(R.id.txt_itemcart_name);
            txtItemCartPrice = itemView.findViewById(R.id.txt_itemcart_price);
            btnMinus = itemView.findViewById(R.id.btn_minus);
            btnPlus = itemView.findViewById(R.id.btn_plus);
            edtAmount = itemView.findViewById(R.id.edt_amount);
            txtTotalItemCost = itemView.findViewById(R.id.txt_total_itemcart);
            amount = 1;
        }
    }
}
