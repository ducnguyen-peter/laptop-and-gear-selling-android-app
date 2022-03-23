package com.example.mobile.controller.CartDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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

    public Cart createCart(Cart cart){
        ContentValues values = new ContentValues();
        values.put(Constant.COLUMN_CART_DISCOUNT, cart.getDiscount());
        values.put(Constant.COLUMN_CART_USER_ID, cart.getUser().getId());
        long id = sqLiteDatabase.insertOrThrow(Constant.TABLE_CART,null, values);
        cart.setId((int)id);
        return cart;
    }

    public Cart checkCartExisted(User user){

    }

    public Cart getCartOfUser(User user){
        //get id, discount
        String queryCart = "SELECT Cart.Id, Cart.Discount, Cart.UserId FROM Cart, User WHERE Cart.UserId = User.Id;";
        Cursor cursor = sqLiteDatabase.rawQuery(queryCart, new String[]{});
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



    private Cart cursorToCart(Cursor cursor){
        Cart cart = new Cart();
        cart.setId(cursor.getInt(cursor.getColumnIndexOrThrow("Cart.Id")));
        cart.setDiscount(cursor.getFloat(cursor.getColumnIndexOrThrow("Cart.Discount")));
        return cart;
    }

    private CartItem cursorToCartItem(Cursor cursor){
        CartItem cartItem = new CartItem();
        cartItem.setAmount(cursor.getInt(cursor.getColumnIndexOrThrow("CartItem.Amount")));
        cartItem.setDiscount(cursor.getFloat(cursor.getColumnIndexOrThrow("CartItem.Discount")));

        Item item = new Item();
        item.setId(cursor.getInt(cursor.getColumnIndexOrThrow("CartItem.ItemId")));

        cartItem.setItem(item);

        return cartItem;
    }
}
