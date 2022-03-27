package com.example.mobile.view.UserActivity.main.fragments;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
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

public class ListItemCartAdapter extends RecyclerView.Adapter<ListItemCartAdapter.CartItemViewHolder> {
    private Context context;
    private ArrayList<CartItem> cartItemList;
    private boolean isSelectAllCB = false;
    private ISendData iSendData;

    public ListItemCartAdapter(Context context, ArrayList<CartItem> cartItemList, ISendData iSendData) {
        this.context = context;
        this.cartItemList = cartItemList;
        this.iSendData = iSendData;
    }


    public void checkAllCB(boolean b) {
        isSelectAllCB = b;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View cartItemView = inflater.inflate(R.layout.cart_row_item_layout, parent, false);
        CartItemViewHolder cartItemViewHolder = new CartItemViewHolder(cartItemView);
        return cartItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);

        Item item = cartItem.getItem();
        holder.cbItemCart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (compoundButton == holder.cbItemCart) {
                    if (isChecked) {
                        iSendData.addSelectedCartItems(cartItem);
                    } else {
                        iSendData.removeSelectedCartItems(cartItem);
                    }
                }
            }
        });
//        itemCheckBoxes.add(holder.cbItemCart);
        holder.cbItemCart.setChecked(isSelectAllCB);
        holder.imgBtnItemCart.setImageResource(context.getResources().getIdentifier(item.getElectronics().getImageLink().trim(), "drawable", context.getPackageName()));
        holder.txtItemCartName.setText(item.getElectronics().getName());
        holder.txtItemCartPrice.setText(String.format(Locale.ENGLISH, "%.1fđ", item.getUnitPrice()));
        holder.edtAmount.setText(String.format(Locale.ENGLISH, "%d", holder.amount));
        //when amount in selecting box changes
        holder.edtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                cartItem.setAmount(holder.amount);
                holder.txtTotalItemCost.setText(String.format(Locale.ENGLISH, "Total: %.1fđ", item.getUnitPrice() * holder.amount));
                iSendData.updateSelectedCartItemsAmount(cartItem, cartItem.getAmount());
//                notifyDataSetChanged();
            }
        });
        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.amount > 1) {
                    holder.amount -= 1;
                    holder.edtAmount.setText(String.format(Locale.ENGLISH, "%d", holder.amount));
                }
            }
        });
        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.amount <= item.getQuantity()) {
                    holder.amount += 1;
                    holder.edtAmount.setText(String.format(Locale.ENGLISH, "%d", holder.amount));
                } else {
                    Toast.makeText(context, "You can't buy more than available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.txtTotalItemCost.setText(String.format(Locale.ENGLISH, "Total: %.1fđ", item.getUnitPrice() * holder.amount));
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iSendData.deleteCartItem(cartItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class CartItemViewHolder extends RecyclerView.ViewHolder {
        public CheckBox cbItemCart;
        public ImageButton imgBtnItemCart;
        public TextView txtItemCartName;
        public TextView txtItemCartPrice;
        public Button btnMinus;
        public Button btnPlus;
        public EditText edtAmount;
        public TextView txtTotalItemCost;
        public ImageButton btnDelete;
        public int amount;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            cbItemCart = itemView.findViewById(R.id.cb_item_cart);
            imgBtnItemCart = itemView.findViewById(R.id.btn_item_cart);
            txtItemCartName = itemView.findViewById(R.id.txt_itemcart_name);
            txtItemCartPrice = itemView.findViewById(R.id.txt_itemcart_price);
            btnMinus = itemView.findViewById(R.id.btn_minus);
            btnPlus = itemView.findViewById(R.id.btn_plus);
            edtAmount = itemView.findViewById(R.id.edt_amount);
            txtTotalItemCost = itemView.findViewById(R.id.txt_total_itemcart);
            btnDelete = itemView.findViewById(R.id.btn_itemcart_delete);
            amount = 1;
        }
    }

    public class ItemMenuViewHolder extends RecyclerView.ViewHolder {

        public ItemMenuViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
