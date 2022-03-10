package com.example.mobile.controller.UserDAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.PhoneNumberUtils;
import android.util.Patterns;
import android.widget.EditText;


import com.example.mobile.model.User;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class UserDAOImpl extends SQLiteAssetHelper implements UserDAO {
    public static final String DATABASE_NAME="mobile.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USER = "User";

    private static final String COLUMN_USER_ID = "Id";
    private static final String COLUMN_USER_NAME = "Username";
    private static final String COLUMN_USER_PASSWORD = "Password";
    private static final String COLUMN_USER_EMAIL = "Email";
    private static final String COLUMN_USER_ADDRESS = "Address";
    private static final String COLUMN_USER_TEL = "Tel";
    private static final String COLUMN_USER_AVATAR = "Avatar";
    private static final String COLUMN_USER_ROLE = "Role";

    private final String[] allColumns = {COLUMN_USER_ID, COLUMN_USER_NAME, COLUMN_USER_PASSWORD, COLUMN_USER_EMAIL, COLUMN_USER_ADDRESS,
            COLUMN_USER_TEL, COLUMN_USER_AVATAR, COLUMN_USER_ROLE};

    private final String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "(" + COLUMN_USER_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USER_NAME + " TEXT," + COLUMN_USER_PASSWORD + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_ADDRESS + " TEXT," + COLUMN_USER_TEL + " TEXT,"
            + COLUMN_USER_AVATAR + " TEXT," + COLUMN_USER_ROLE+ " TEXT )";
    private final String DROP_TABLE_USER = " DROP TABLE IF EXISTS " + TABLE_USER;

    public UserDAOImpl(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
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

    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getUsername());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_ADDRESS, user.getAddress());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_ADDRESS, user.getAddress());
        values.put(COLUMN_USER_TEL, user.getTel());
        values.put(COLUMN_USER_AVATAR, user.getAvatar());
        values.put(COLUMN_USER_ROLE, user.getRole());
        db.insert(TABLE_USER,null, values);
        db.close();
    }

    public boolean checkLogin(EditText txtIdentity, EditText txtPassword){
        String identity = txtIdentity.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = getWritableDatabase();
        String selection = "";
        if(Patterns.EMAIL_ADDRESS.matcher(identity).matches()){
            selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";
        }
        else if(PhoneNumberUtils.isGlobalPhoneNumber(identity)){
            selection = COLUMN_USER_TEL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";
        }
        else{
            selection = COLUMN_USER_NAME + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";
        }
        String[] selectionArgs = {
                identity, password
        };
        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if(cursorCount>0) return true;
        return false;
    }

    public User getUser(EditText txtInput){
        String input = txtInput.getText().toString().trim();
        SQLiteDatabase db = getWritableDatabase();
        String selection = "";
        if(Patterns.EMAIL_ADDRESS.matcher(input).matches() && input.contains("@")){
            selection = COLUMN_USER_EMAIL + " = ?";
        } else if(PhoneNumberUtils.isGlobalPhoneNumber(input)){
            selection = COLUMN_USER_TEL + " = ?";
        } else{
            selection = COLUMN_USER_NAME + " = ?";
        }
        String[] selectionArgs = {
                input
        };
        Cursor cursor = db.query(TABLE_USER, allColumns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        if(cursorCount<=0) return null;
        cursor.moveToFirst();
        User user = new User();
        while(!cursor.isAfterLast()){
            user = cursorToUser(cursor);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return user;
    }

    public boolean checkExistedUser(EditText txtInput){
        String input = txtInput.getText().toString().trim();
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = getWritableDatabase();

        String selection = "";
        if(Patterns.EMAIL_ADDRESS.matcher(input).matches()){
            selection = COLUMN_USER_EMAIL + " = ?";
        } else if(PhoneNumberUtils.isGlobalPhoneNumber(input)){
            selection = COLUMN_USER_TEL + " = ?";
        } else{
            selection = COLUMN_USER_NAME + " = ?";
        }

        String[] selectionArgs = {
                input
        };
        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
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
