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

    public static final String[] allUserColumns = {COLUMN_USER_ID, COLUMN_USER_NAME, COLUMN_USER_PASSWORD, COLUMN_USER_EMAIL, COLUMN_USER_ADDRESS,
            COLUMN_USER_TEL, COLUMN_USER_AVATAR, COLUMN_USER_ROLE};

    public final String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "(" + COLUMN_USER_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USER_NAME + " TEXT," + COLUMN_USER_PASSWORD + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_ADDRESS + " TEXT," + COLUMN_USER_TEL + " TEXT,"
            + COLUMN_USER_AVATAR + " TEXT," + COLUMN_USER_ROLE+ " TEXT )";
    public final String DROP_TABLE_USER = " DROP TABLE IF EXISTS " + TABLE_USER;

    //name and columns for table Electronics
    public static final String TABLE_ELECTRONICS = "Electronics";
    public static final String COLUMN_ELECTRONIC_ID = "Id";
    public static final String COLUMN_ELECTRONICS_NAME = "Name";
    public static final String COLUMN_ELECTRONICS_DESCRIPTION = "Description";
    public static final String COLUMN_ELECTRONICS_UNIT_PRICE = "UnitPrice";
    public static final String COLUMN_ELECTRONICS_QUANTITY = "Quantity";
    public static final String COLUMN_ELECTRONICS_IMAGE_LINK = "ImageLink";

    public static final String[] allElectronicsColumns = {COLUMN_ELECTRONIC_ID, COLUMN_ELECTRONICS_NAME, COLUMN_ELECTRONICS_DESCRIPTION,
            COLUMN_ELECTRONICS_UNIT_PRICE, COLUMN_ELECTRONICS_QUANTITY, COLUMN_ELECTRONICS_IMAGE_LINK};

    //name and columns for table Item
    public static final String TABLE_ITEM = "Item";
    public static final String COLUMN_ITEM_ID = "Id";
    public static final String COLUMN_ITEM_UNIT_PRICE = "UnitPrice";
    public static final String COLUMN_ITEM_QUANTITY = "Quantity";
    public static final String COLUMN_ITEM_TOTAL_BUY = "TotalBuy";
    public static final String COLUMN_ITEM_ELECTRONICS_ID = "ElectronicsId";

    public static final String[] allItemColumns = {COLUMN_ITEM_ID, COLUMN_ITEM_UNIT_PRICE, COLUMN_ITEM_QUANTITY,
            COLUMN_ITEM_TOTAL_BUY,COLUMN_ITEM_ELECTRONICS_ID};

    //name and columns for table Category
    public static final String TABLE_CATEGORY = "Category";
    public static final String COLUMN_CATEGORY_ID = "Id";
    public static final String COLUMN_CATEGORY_NAME = "Name";

    public static final String[] allCategoryColumns = {COLUMN_CATEGORY_ID, COLUMN_CATEGORY_NAME};

    //name and columns for table CategoryElectronics
    public static final String TABLE_CATEGORY_ELECTRONICS = "CategoryElectronics";
    public static final String COLUMN_CATEGORY_ELECTRONICS_CATEGORY_ID = "CategoryId";
    public static final String COLUMN_CATEGORY_ELECTRONICS_ELECTRONICS_ID = "ElectronicsId";

    public final String[] allCategoryElectronicsColumns = {COLUMN_CATEGORY_ELECTRONICS_CATEGORY_ID, COLUMN_CATEGORY_ELECTRONICS_ELECTRONICS_ID};

    //
    //key for SharedPreferences
    public static String KEY_USERNAME = "username";
    public static String KEY_EMAIL = "email";
    public static String KEY_TEL = "tel";
    public static String KEY_PASSWORD = "password";
}
