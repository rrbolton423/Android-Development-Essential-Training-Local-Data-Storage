package com.example.android.data.database;

/**
 * Created by romellbolton on 1/9/18.
 */

// This class will include a number of constants. Setting the name of the table, the
// names and types of its columns, and other critical information.
public class ItemsTable {

    // The name of the table can be anything you want it to be, and the names of the
    // columns can be whatever you want as well.
    public static final String TABLE_ITEMS = "items";
    public static final String COLUMN_ID = "itemId";
    public static final String COLUMN_NAME = "itemName";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_POSITION = "sortPosition";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_IMAGE = "image";

    // You actually declare the structure of the table in the SQL_CREATE statement,
    // which is a simple String constant

    // It is important to match the data types of the columns to the types of your model fields.
    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_ITEMS + "(" +
                    // TEXT = String
                    COLUMN_ID + " TEXT PRIMARY KEY," +
                    // TEXT = String
                    COLUMN_NAME + " TEXT," +
                    // TEXT = String
                    COLUMN_DESCRIPTION + " TEXT," +
                    // TEXT = String
                    COLUMN_CATEGORY + " TEXT," +
                    // INTEGER = int
                    COLUMN_POSITION + " INTEGER," +
                    // REAL = Double
                    COLUMN_PRICE + " REAL," +
                    // TEXT = String
                    COLUMN_IMAGE + " TEXT" + ");";

    // Code to delete or 'drop' the table
    public static final String SQL_DELETE =
            "DROP TABLE " + TABLE_ITEMS;

    // We won't actually execute these lines of code here though
    // The execution of these SQL statements will go into the open helper class
}
