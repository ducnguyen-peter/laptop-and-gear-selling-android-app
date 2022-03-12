package com.example.mobile.controller.ItemDAO;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mobile.model.Item.Electronics;
import com.example.mobile.model.Item.Item;
import com.example.mobile.utils.Constant;
import com.example.mobile.utils.DBHelper;

import java.util.ArrayList;

public class ItemDAOImpl implements ItemDAO{
    private SQLiteDatabase sqLiteDatabase;
    private DBHelper dbHelper;
    private Context context;

    public ItemDAOImpl(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
        try{
            open();
        } catch (SQLException e){
            Log.e("ItemDAOImpl", "SQLException on opening database" + e.getMessage());
            e.printStackTrace();
        }
    }
    private void open(){
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    private void close(){
        dbHelper.close();
    }

    @Override
    public ArrayList<Item> searchItem(String input) {
        ArrayList<Item> searchResultItem = new ArrayList<>();
        String query = "SELECT Item.Id, Item.UnitPrice, Item.Quantity, Item.TotalBuy, " +
                "Electronics.Name, Electronics.Description, Electronics.ImageLink " +
                "FROM Item, Electronics " +
                "WHERE Item.ElectronicsId = Electronics.id AND Item.ElectronicsId = ?";

        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{input});
        int cursorCount = cursor.getCount();
        if(cursorCount<=0) return null;
        cursor.moveToFirst();
        Item item;
        while(!cursor.isAfterLast()){
            item = cursorToItem(cursor);
            searchResultItem.add(item);
            cursor.moveToNext();
        }
        cursor.close();

        return searchResultItem;
    }

    @Override
    public ArrayList<Item> getAllItems() {
        return null;
    }

    private Item cursorToItem(Cursor cursor){
        Item item = new Item();
        Electronics electronics = new Electronics();
        item.setId(cursor.getInt(1));
        item.setUnitPrice(cursor.getFloat(2));
        item.setQuantity(cursor.getInt(3));
        item.setTotalBuy(cursor.getInt(4));

        electronics.setName(cursor.getString(5));
        electronics.setDescription(cursor.getString(6));
        electronics.setImageLink(cursor.getString(7));

        item.setElectronics(electronics);
        return item;
    }
}
