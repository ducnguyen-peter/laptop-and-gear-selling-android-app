package com.example.mobile.controller.CartDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.example.mobile.model.Item.Item;
import com.example.mobile.model.cart.Cart;
import com.example.mobile.model.cart.CartItem;
import com.example.mobile.model.user.User;
import com.example.mobile.utils.Constant;
import com.example.mobile.utils.DBHelper;

import java.util.ArrayList;

public class CartDAOImpl implements CartDAO{
    private SQLiteDatabase sqLiteDatabase;
    private DBHelper dbHelper;
    private Context context;

    public CartDAOImpl(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
        try{
            open();
        } catch (SQLiteException e){
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

    public Cart createCart(User user){
        Cart cart = new Cart(user);
        try {
            ContentValues values = new ContentValues();
            values.put(Constant.COLUMN_CART_DISCOUNT, 0);
            values.put(Constant.COLUMN_CART_USER_ID, user.getId());
            long id = sqLiteDatabase.insertOrThrow(Constant.TABLE_CART, null, values);
//            System.out.println(id);
            cart.setId((int) id);
        }catch (SQLiteException e){
            e.printStackTrace();
        }
        System.out.println("New cart Id: " + cart.getId());
        return cart;
    }

    public boolean isCartExisted(User user){
        int cursorCount = 0;
        try{
            String queryCart = "SELECT Cart.Id, Cart.Discount, Cart.UserId FROM Cart, User WHERE Cart.UserId = User.Id AND User.Id = ?;";
            Cursor cursor = sqLiteDatabase.rawQuery(queryCart, new String[]{Integer.toString(user.getId())});
            System.out.println("User Id: " + user.getId());
            cursorCount = cursor.getCount();
            System.out.println("Cursor count: " + cursorCount);
            cursor.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
        return cursorCount > 0;
    }

    public Cart getCartOfUser(User user){
        //get id, discount
        String queryCart = "SELECT Cart.Id, Cart.Discount, Cart.UserId FROM Cart, User WHERE Cart.UserId = User.Id AND User.Id = ?;";
        Cursor cursor = sqLiteDatabase.rawQuery(queryCart, new String[]{Integer.toString(user.getId())});
        int cursorCount = cursor.getCount();
        if(cursorCount<=0) return null;
        cursor.moveToFirst();
        Cart cart = new Cart();
        while(!cursor.isAfterLast()){
            cart = cursorToCart(cursor);
            cart.setUser(user);
            cursor.moveToNext();
        }
        cursor.close();

        //get cart item list
        ArrayList<CartItem> cartItems = new ArrayList<>();
        String queryCartItem = "SELECT CartItem.Amount, CartItem.Discount, CartItem.ItemId \n" +
                "FROM Cart, CartItem, Item \n" +
                "WHERE Cart.Id = CartItem.CartId AND CartItem.ItemId = Item.Id AND Cart.Id = ?;";
        cursor = sqLiteDatabase.rawQuery(queryCartItem, new String[]{Integer.toString(cart.getId())});
        cursor.moveToFirst();
        CartItem cartItem;
        while(!cursor.isAfterLast()){
            cartItem = cursorToCartItem(cursor);
            cartItems.add(cartItem);
            cursor.moveToNext();
        }
        cursor.close();

        cart.setCartItems(cartItems);

        return cart;
    }

    public boolean addCartItem(Item item, int cartId, int amount, int discount){
        if(isDuplicatedCartItem(item)){
            Toast.makeText(context, "This item is already in your cart", Toast.LENGTH_SHORT).show();
            return false;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.COLUMN_CART_ITEM_AMOUNT, amount);
        contentValues.put(Constant.COLUMN_CART_ITEM_DISCOUNT, discount);
        contentValues.put(Constant.COLUMN_CART_ITEM_CART_ID, cartId);
        contentValues.put(Constant.COLUMN_CART_ITEM_ITEM_ID, item.getId());
        long newRowId = sqLiteDatabase.insertOrThrow(Constant.TABLE_CART_ITEM, null, contentValues);
        return newRowId!=-1;
    }

    public boolean deleteCartItem(CartItem cartItem, int cartId){
        String whereClause = Constant.COLUMN_CART_ITEM_ITEM_ID + " = ? AND " + Constant.COLUMN_CART_ITEM_CART_ID + " = ?";
        String[] whereArgs = {
                Integer.toString(cartItem.getItem().getId()), Integer.toString(cartId)
        };
        return sqLiteDatabase.delete(Constant.TABLE_CART_ITEM, whereClause, whereArgs) > 0;
    }

    public boolean isDuplicatedCartItem(Item item){
        int itemId = item.getId();
        String[] columns = {
                Constant.COLUMN_CART_ITEM_ITEM_ID
        };
        String selection = Constant.COLUMN_CART_ITEM_ITEM_ID + " = ?";
        String[] selectionArgs = {Integer.toString(itemId)};
        Cursor cursor = sqLiteDatabase.query(Constant.TABLE_CART_ITEM, columns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        return cursorCount>0;
    }

    private Cart cursorToCart(Cursor cursor){
        Cart cart = new Cart();
        cart.setId(cursor.getInt(cursor.getColumnIndexOrThrow("Id")));
        cart.setDiscount(cursor.getFloat(cursor.getColumnIndexOrThrow("Discount")));
        return cart;
    }

    private CartItem cursorToCartItem(Cursor cursor){
        CartItem cartItem = new CartItem();
        cartItem.setAmount(cursor.getInt(cursor.getColumnIndexOrThrow("Amount")));
        cartItem.setDiscount(cursor.getFloat(cursor.getColumnIndexOrThrow("Discount")));

        Item item = new Item();
        item.setId(cursor.getInt(cursor.getColumnIndexOrThrow("ItemId")));

        cartItem.setItem(item);

        return cartItem;
    }
}
