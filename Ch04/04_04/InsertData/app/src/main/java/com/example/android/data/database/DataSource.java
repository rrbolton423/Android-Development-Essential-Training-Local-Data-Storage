package com.example.android.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.data.model.DataItem;

public class DataSource {

    private Context mContext;
    private SQLiteDatabase mDatabase;
    SQLiteOpenHelper mDbHelper;

    public DataSource(Context context) {
        this.mContext = context;
        mDbHelper = new DBHelper(mContext);
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void open() {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    // Once you've created your open helper and data source classes,
    // you can start adding code to the data source to insert, update, and
    // delete rows, and read data from the database tables.

    // We'll start with inserting data into the Items table
    // The createItem() method will return a reference to a DataItem object
    // It'll receive as its 1 and only argument an instance of the DataItem
    // class named "item". It'll be public so it can be called from anywhere in
    // the application. And it returns an instance of the DataItem class so I can
    // then see if anything that might've changed in that object.

    public DataItem createItem(DataItem item) {
        // To insert data into the table, you could execute an explicit
        // SQL statement. But then it would be up to you to handle all sorts
        // of special coding, including escaping special characters, using single quotes
        // where you should be using double quotes, and so on.
        // Instead you can use a set of convenience methods that are apart of the SDK
        // for inserting, updating, and deleting data.
        // To do this you need an object that's derived from a class named contentValues,
        // which is like a bundle. It's a set of key value pairs.
        // You then take that contentValues object and pass it into one of the database
        // methods. This will result in creating a well-formed SQL insert statement,
        // and you won't have to do that hard work yourself.

        // To get the content values object, you'll have to add some more code somewhere.
        // I'm going to choose to add code to my data item model class. Some developers
        // prefer to create a separate class and put the code for this in that sort of utility
        // class. The code in our case will be located in the DataItem.java class...

        // NOW ...

        // Get the "values" object returned from the toValues() method in the DataItem.java class.
        // We do this so the SQL code is written for us
        ContentValues values = item.toValues();

        // Now I'm ready to insert the data into the database.
        // The mDatabase object's insert() method takes 3 args ...
        // 1. The name of the Table
        // 2. nullColumnHack, a value that you can pass in if for some reason you're being
        // forced to pass in a contentValues object that doesn't have anything in it at all.
        // In SQLite, you always have to insert at least one column , and in that case you can pass
        // the name of the column here. That won't be a problem for this operation, so we'll pass in null.
        // 3. The ContentValues object.
        // This is a single threaded operation, so it's going to do the insert, and then immediately
        // come back to this code.
        mDatabase.insert(ItemsTable.TABLE_ITEMS, null, values);

        // Return the item
        return item;

        // Now, we have a method in the DataSource, that can add a 'single' DataItem
        // to my database table.
    }

    // It'll be public, return a long value, and be called getDataItemsCount().
    public long getDataItemsCount() {

        // I could generate a custom SQL statement that counted the rows in the database table,
        // but instead, I'm going to use a method of a class named DatabaseUtils that
        // does this for me. The DatabaseUtils class is a member of the Android.database,
        // and it has this very useful method, queryNumEntries(), passing in my database reference,
        // and the name of the table I'm looking at.
        // Now it'll be very easy to find out how many items I already have in the table
        return DatabaseUtils.queryNumEntries(mDatabase, ItemsTable.TABLE_ITEMS);
    }
}
