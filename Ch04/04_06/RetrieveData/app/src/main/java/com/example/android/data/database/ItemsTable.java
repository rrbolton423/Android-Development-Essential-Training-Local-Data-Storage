package com.example.android.data.database;

public class ItemsTable {
    public static final String TABLE_ITEMS = "items";
    public static final String COLUMN_ID = "itemId";
    public static final String COLUMN_NAME = "itemName";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_POSITION = "sortPosition";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_IMAGE = "image";

    // Add a new constant here, which will be an array of Strings, and each item in the
    // array will be one of the names of the columns of the table.
    // The order in which you declare the column names will determine the order of the
    // columns when you return data from the database. But when you actually retrieve the data.
    // the positions of the columns won't really matter.

    // Next I'll create a new method in my DataSource class that uses this String array.
    public static final String[] ALL_COLUMNS =
            {COLUMN_ID, COLUMN_NAME, COLUMN_DESCRIPTION,
                    COLUMN_CATEGORY, COLUMN_POSITION, COLUMN_PRICE, COLUMN_IMAGE};

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_ITEMS + "(" +
                    COLUMN_ID + " TEXT PRIMARY KEY," +
                    COLUMN_NAME + " TEXT," +
                    COLUMN_DESCRIPTION + " TEXT," +
                    COLUMN_CATEGORY + " TEXT," +
                    COLUMN_POSITION + " INTEGER," +
                    COLUMN_PRICE + " REAL," +
                    COLUMN_IMAGE + " TEXT" + ");";

    public static final String SQL_DELETE =
            "DROP TABLE " + TABLE_ITEMS;
}
