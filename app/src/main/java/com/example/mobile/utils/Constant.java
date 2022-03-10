package com.example.mobile.utils;

public class Constant {
    private Constant(){

    }
    //name and columns names of table User
    public static final String TABLE_USER = "User";

    public static final String COLUMN_USER_ID = "Id";
    public static final String COLUMN_USER_NAME = "Username";
    public static final String COLUMN_USER_PASSWORD = "Password";
    public static final String COLUMN_USER_EMAIL = "Email";
    public static final String COLUMN_USER_ADDRESS = "Address";
    public static final String COLUMN_USER_TEL = "Tel";
    public static final String COLUMN_USER_AVATAR = "Avatar";
    public static final String COLUMN_USER_ROLE = "Role";

    public static final String[] allColumns = {COLUMN_USER_ID, COLUMN_USER_NAME, COLUMN_USER_PASSWORD, COLUMN_USER_EMAIL, COLUMN_USER_ADDRESS,
            COLUMN_USER_TEL, COLUMN_USER_AVATAR, COLUMN_USER_ROLE};

    public final String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "(" + COLUMN_USER_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USER_NAME + " TEXT," + COLUMN_USER_PASSWORD + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_ADDRESS + " TEXT," + COLUMN_USER_TEL + " TEXT,"
            + COLUMN_USER_AVATAR + " TEXT," + COLUMN_USER_ROLE+ " TEXT )";
    public final String DROP_TABLE_USER = " DROP TABLE IF EXISTS " + TABLE_USER;

    public static String KEY_USERNAME = "username";
    public static String KEY_EMAIL = "email";
    public static String KEY_TEL = "tel";
    public static String KEY_PASSWORD = "password";
}
