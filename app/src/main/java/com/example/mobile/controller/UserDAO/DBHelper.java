package com.example.mobile.controller.UserDAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DBHelper extends SQLiteAssetHelper {
    public static final String DATABASE_NAME="mobile.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
