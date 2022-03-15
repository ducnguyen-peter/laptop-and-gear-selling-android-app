package com.example.mobile.view.UserActivity.main;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cursoradapter.widget.CursorAdapter;

import com.example.mobile.R;

public class ItemCursorAdapter extends CursorAdapter {

    public ItemCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.grid_item_layout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtItemName = view.findViewById(R.id.lbl_item_name);
        TextView txtItemPrice = view.findViewById(R.id.lbl_item_price);
        ImageView imgItem = view.findViewById(R.id.img_item_small);

        txtItemName.setText(cursor.getString(cursor.getColumnIndexOrThrow("Electronics.Name")));
        txtItemPrice.setText(cursor.getString(cursor.getColumnIndexOrThrow("Electronics.Description")));
        imgItem.setImageResource(
                context.getResources().getIdentifier(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("Electronics.ImageLink")), "drawable", context.getPackageName()));
    }
}
