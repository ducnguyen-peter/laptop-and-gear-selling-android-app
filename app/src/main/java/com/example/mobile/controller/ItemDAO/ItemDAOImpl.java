package com.example.mobile.controller.ItemDAO;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mobile.model.Item.Category;
import com.example.mobile.model.Item.Electronics;
import com.example.mobile.model.Item.Item;
import com.example.mobile.utils.Constant;
import com.example.mobile.utils.DBHelper;

import java.lang.reflect.Array;
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
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Electronics_fts");
        sqLiteDatabase.execSQL("CREATE VIRTUAL TABLE Electronics_fts USING fts4(Name)");
        sqLiteDatabase.execSQL("INSERT INTO Electronics_fts (Name) SELECT Name FROM Electronics");
    }

    private void close(){
        dbHelper.close();
    }

    @Override
    public ArrayList<Item> searchItem(String input) {
        ArrayList<Item> searchResultItem = new ArrayList<>();
        String query = "SELECT Item.Id, Item.UnitPrice, Item.Quantity, Item.TotalBuy, Electronics.Id, Electronics.Name, Electronics.Description, Electronics.ImageLink\n" +
                "FROM Item, Electronics\n" +
                "WHERE ElectronicsId = Electronics.Id AND Electronics.Id IN (SELECT rowid FROM Electronics_fts WHERE Name MATCH ?)";

        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{input + "*"});
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
        ArrayList<Item> allItems = new ArrayList<>();
        try{
            String query = "SELECT Item.Id, Item.UnitPrice, Item.Quantity, Item.TotalBuy, Electronics.Id, Electronics.Name, Electronics.Description, Electronics.ImageLink\n" +
                    "                FROM Item, Electronics\n" +
                    "                WHERE Item.ElectronicsId = Electronics.Id;";
            Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{});
            int cursorCount = cursor.getCount();
            if(cursorCount<=0) return null;
            cursor.moveToFirst();
            Item item;
            while(!cursor.isAfterLast()){
                item = cursorToItem(cursor);
                allItems.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return allItems;
    }

    public Item getItemById(int id){
        Item item = new Item();
        try{
            String query = "SELECT Item.Id, Item.UnitPrice, Item.Quantity, Item.TotalBuy, Electronics.Id, Electronics.Name, Electronics.Description, Electronics.ImageLink\n" +
                    "                FROM Item, Electronics\n" +
                    "                WHERE Item.ElectronicsId = Electronics.Id AND Item.Id = ?;";
            Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{Integer.toString(id)});
            int cursorCount = cursor.getCount();
            if(cursorCount<=0) return null;
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                item = cursorToItem(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return item;
    }

    public ArrayList<Category> getItemCategory(Item item){
        ArrayList<Category> itemCategories = new ArrayList<>();
        String query = "SELECT DISTINCT Category.Id, Category.Name\n" +
                "FROM Category, CategoryElectronics, Electronics\n" +
                "WHERE Category.Id = CategoryElectronics.CategoryId \n" +
                "AND CategoryElectronics.ElectronicsId = Electronics.Id \n" +
                "AND Electronics.Id = 1;";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        int cursorCount = cursor.getCount();
        if(cursorCount<=0) return null;
        cursor.moveToFirst();
        Category category;
        while (!cursor.isAfterLast()){
            category = cursorToCategory(cursor);
            itemCategories.add(category);
            cursor.moveToNext();
        }
        cursor.close();
        return itemCategories;
    }

    private Item cursorToItem(Cursor cursor){
        Item item = new Item();
        Electronics electronics = new Electronics();

        item.setId(cursor.getInt(0));
        item.setUnitPrice(cursor.getFloat(1));
        item.setQuantity(cursor.getInt(2));
        item.setTotalBuy(cursor.getInt(3));

        electronics.setId(cursor.getInt(4));
        electronics.setName(cursor.getString(5));
        electronics.setDescription(cursor.getString(6));
        electronics.setImageLink(cursor.getString(7));

        item.setElectronics(electronics);
        return item;
    }

    private Category cursorToCategory(Cursor cursor){
        Category category = new Category();
        category.setId(cursor.getInt(0));
        category.setName(cursor.getString(1));
        return category;
    }
}
