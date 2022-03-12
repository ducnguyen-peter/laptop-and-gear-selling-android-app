package com.example.mobile.controller.UserDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;

import com.example.mobile.model.User;
import com.example.mobile.utils.Constant;

public class UserDAOImpl implements UserDAO{

    private SQLiteDatabase sqLiteDatabase;
    private DBHelper dbHelper;
    private Context context;

    public UserDAOImpl(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
        try{
            open();
        } catch (SQLException e){
            Log.e("UserDAOImpl", "SQLException on opening database" + e.getMessage());
            e.printStackTrace();
        }
    }
    private void open(){
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    private void close(){
        dbHelper.close();
    }

    public void addUser(User user){
        ContentValues values = new ContentValues();
        values.put(Constant.COLUMN_USER_NAME, user.getUsername());
        values.put(Constant.COLUMN_USER_PASSWORD, user.getPassword());
        values.put(Constant.COLUMN_USER_ADDRESS, user.getAddress());
        values.put(Constant.COLUMN_USER_EMAIL, user.getEmail());
        values.put(Constant.COLUMN_USER_ADDRESS, user.getAddress());
        values.put(Constant.COLUMN_USER_TEL, user.getTel());
        values.put(Constant.COLUMN_USER_AVATAR, user.getAvatar());
        values.put(Constant.COLUMN_USER_ROLE, user.getRole());
        sqLiteDatabase.insert(Constant.TABLE_USER,null, values);

    }

    public void updateUser(String identity, String[] columns, String[] values){

        String selection;
        if(Patterns.EMAIL_ADDRESS.matcher(identity).matches() && identity.contains("@")){
            selection = Constant.COLUMN_USER_EMAIL + " = ?";
        } else if(PhoneNumberUtils.isGlobalPhoneNumber(identity)){
            selection = Constant.COLUMN_USER_TEL + " = ?";
        } else{
            selection = Constant.COLUMN_USER_NAME + " = ?";
        }

        ContentValues contentValues = new ContentValues();
        int valuePos = 0;
        int valuesLength = values.length;
        try{
            for(String column : columns){
                if(valuePos<valuesLength){
                    contentValues.put(column, values[valuePos++]);
                }
                else return;
            }
            sqLiteDatabase.update(Constant.TABLE_USER, contentValues, selection, new String[]{identity});

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean checkLogin(EditText txtIdentity, EditText txtPassword){
        String identity = txtIdentity.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();
        String[] columns = {
                Constant.COLUMN_USER_ID
        };
        String selection;
        if(Patterns.EMAIL_ADDRESS.matcher(identity).matches()){
            selection = Constant.COLUMN_USER_EMAIL + " = ?" + " AND " + Constant.COLUMN_USER_PASSWORD + " = ?";
        }
        else if(PhoneNumberUtils.isGlobalPhoneNumber(identity)){
            selection = Constant.COLUMN_USER_TEL + " = ?" + " AND " + Constant.COLUMN_USER_PASSWORD + " = ?";
        }
        else{
            selection = Constant.COLUMN_USER_NAME + " = ?" + " AND " + Constant.COLUMN_USER_PASSWORD + " = ?";
        }
        String[] selectionArgs = {
                identity, password
        };
        Cursor cursor = sqLiteDatabase.query(Constant.TABLE_USER, columns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();

        if(cursorCount>0) return true;
        return false;
    }

    public User getUser(EditText txtInput){
        String input = txtInput.getText().toString().trim();
        String selection;
        if(Patterns.EMAIL_ADDRESS.matcher(input).matches() && input.contains("@")){
            selection = Constant.COLUMN_USER_EMAIL + " = ?";
        } else if(PhoneNumberUtils.isGlobalPhoneNumber(input)){
            selection = Constant.COLUMN_USER_TEL + " = ?";
        } else{
            selection = Constant.COLUMN_USER_NAME + " = ?";
        }
        String[] selectionArgs = {
                input
        };
        Cursor cursor = sqLiteDatabase.query(Constant.TABLE_USER, Constant.allColumns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        if(cursorCount<=0) return null;
        cursor.moveToFirst();
        User user = new User();
        while(!cursor.isAfterLast()){
            user = cursorToUser(cursor);
            cursor.moveToNext();
        }
        cursor.close();

        return user;
    }

    public boolean checkExistedUser(EditText txtInput){
        String input = txtInput.getText().toString().trim();
        String[] columns = {
                Constant.COLUMN_USER_ID
        };

        String selection;
        if(Patterns.EMAIL_ADDRESS.matcher(input).matches()){
            selection = Constant.COLUMN_USER_EMAIL + " = ?";
        } else if(PhoneNumberUtils.isGlobalPhoneNumber(input)){
            selection = Constant.COLUMN_USER_TEL + " = ?";
        } else{
            selection = Constant.COLUMN_USER_NAME + " = ?";
        }

        String[] selectionArgs = {
                input
        };
        Cursor cursor = sqLiteDatabase.query(Constant.TABLE_USER, columns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();

        if(cursorCount>0) return true;
        return false;
    }

    private User cursorToUser(Cursor cursor){
        User user = new User();
        user.setId(cursor.getInt(0));
        user.setUsername(cursor.getString(1));
        user.setEmail(cursor.getString(2));
        user.setTel(cursor.getString(3));
        user.setAddress(cursor.getString(4));
        user.setAvatar(cursor.getString(5));
        user.setRole(cursor.getString(6));
        return user;
    }
}
