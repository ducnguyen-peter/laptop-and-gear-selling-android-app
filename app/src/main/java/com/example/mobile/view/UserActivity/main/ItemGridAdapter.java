package com.example.mobile.view.UserActivity.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobile.R;
import com.example.mobile.model.Item.Item;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;

public class ItemGridAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Item> itemList;

    public ItemGridAdapter(Context context, ArrayList<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        Item item = itemList.get(i);

        if(view==null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(R.layout.grid_item_layout, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.txtItemName = view.findViewById(R.id.lbl_item_name);
            viewHolder.txtItemPrice = view.findViewById(R.id.lbl_item_price);
            viewHolder.imgItem = view.findViewById(R.id.img_item_small);
            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.txtItemName.setText(item.getElectronics().getName());
        viewHolder.txtItemPrice.setText(new BigDecimal(item.getUnitPrice()).toPlainString());
        viewHolder.imgItem.setImageResource(context.getResources().getIdentifier(item.getElectronics().getImageLink().trim(), "drawable", context.getPackageName()));

        return view;
    }

    private class ViewHolder{
        TextView txtItemName;
        TextView txtItemPrice;
        ImageView imgItem;
    }

    private class GetBitMapUrlTask implements Runnable{
        private volatile String url;

        private volatile Bitmap bitMap;

        public GetBitMapUrlTask(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            try {
                URL imgUrl = new URL(url);

                bitMap = BitmapFactory.decodeStream(imgUrl.openStream());
                if(bitMap == null) Log.e("getBitMapFromUrl", "Null Bitmap");
                else Log.e("getBitMapFromUrl", "Non Null Bitmap");
            }
            catch (IOException e){
                Log.e("getBitMapFromUrl", "Error when getting image", e);
                e.printStackTrace();
            }
        }
        public Bitmap getBitMap() {
            return bitMap;
        }

    }
}
