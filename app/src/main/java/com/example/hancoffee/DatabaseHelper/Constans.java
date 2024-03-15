package com.example.hancoffee.DatabaseHelper;
public class Constans {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "HanCoffee";

    public static final String TABLE_PRODUCT = "product";
    public static final String TABLE_CATEGORY = "category";

    // Columns for the Product table
    public static final String PRODUCT_ID = "id";
    public static final String PRODUCT_NAME = "name";
    public static final String PRODUCT_PRICE = "price";
    public static final String PRODUCT_PIC = "pic";

    // Columns for the Category table
    public static final String CATEGORY_ID = "id";
    public static final String CATEGORY_NAME = "name";

    public static final String createProductTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCT + " ("
            + PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + PRODUCT_NAME + " TEXT,"
            + PRODUCT_PRICE + " REAL)"
            + PRODUCT_PIC + "TEXT";

    public static final String createCategoryTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORY + " ("
            + CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + CATEGORY_NAME + " TEXT)";
}
