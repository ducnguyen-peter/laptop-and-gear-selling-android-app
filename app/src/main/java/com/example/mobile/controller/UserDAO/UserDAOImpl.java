package com.example.mobile.controller.UserDAO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.example.mobile.model.User;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class UserDAOImpl extends SQLiteAssetHelper implements UserDAO {
    public static final String dbName="mobile.db";
    private static final int DATABASE_VERSION = 1;

    public UserDAOImpl(Context context){
        super(context, dbName,null,DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @SuppressLint("Range")
    @Override
    public User checkCredentials(String username, String password) {
        String sql="SELECT Id,\n" +
                "       Username,\n" +
                "       Password,\n" +
                "       Email,\n" +
                "       Address,\n" +
                "       Tel,\n" +
                "       Avatar,\n" +
                "       Role\n" +
                "  FROM User\n" +
                "  WHERE Username= ? AND Password= ?;";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[]{username, password});
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                User u = new User();
                u.setId(cursor.getInt(cursor.getColumnIndex("Id")));
                u.setUsername(cursor.getString(cursor.getColumnIndex("Username")));
                u.setEmail(cursor.getString(cursor.getColumnIndex("Email")));
                u.setAddress(cursor.getString(cursor.getColumnIndex("Address")));
                u.setAddress(cursor.getString(cursor.getColumnIndex("Tel")));
                u.setAddress(cursor.getString(cursor.getColumnIndex("Avatar")));
                u.setAddress(cursor.getString(cursor.getColumnIndex("Role")));
                return u;
            }
        }
        return null;
    }
}
