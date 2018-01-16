package com.example.android.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.data.model.DataItem;

import java.util.ArrayList;
import java.util.List;

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

    public DataItem createItem(DataItem item) {
        ContentValues values = item.toValues();
        mDatabase.insert(ItemsTable.TABLE_ITEMS, null, values);
        return item;
    }

    public long getDataItemsCount() {
        return DatabaseUtils.queryNumEntries(mDatabase, ItemsTable.TABLE_ITEMS);
    }

    // We used to have the code to insert the data in the MainActivity class,
    // now, it's been moved into the DataSource class into a method named seedDatabase().
    // It's exactly the same code as before. It checks to see whether there's any data
    // in the database table, and if not, it adds all that data in.
    // This method is now called from the MainActivity's onCreate() method right
    // after instantiating th DataSource class.
    public void seedDatabase(List<DataItem> dataItemList) {
        long numItems = getDataItemsCount();
        if (numItems == 0) {
            for (DataItem item :
                    dataItemList) {
                try {
                    createItem(item);
                } catch (SQLiteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Now that I've stored the data  in the Database, I now want to retrieve the data from the
    // database for display, instead of using the original sample data. In order to
    // retrieve data, you could use a raw SQL statement, but as I described previously,
    // that would involve a lot of code. And many times, you can use the convenience methods of the
    // database class to do the work for you. That's what I'm going to do here.

    // Next I'll create a new method in my DataSource class that uses the "ALL_COLUMNS" String array.

    // The method is called getAllItems, and It'll return a list of data item objects.
    // It will also be public so it can be called anywhere from within the application
    public List<DataItem> getAllItems() {

        // It will start be declaring and instantiating ArrayList
        // The list will contain DataItem objects
        List<DataItem> dataItems = new ArrayList<>();

        // In order to retrieve data, the simplest method to use is called "query()".
        // It's a member of the database object, and we've already declared our database object
        // as a field of this class. So we can call the query() method on the database now.
        // In the query method, I'm going to pass in the name of the table, and the string array
        // of column names. I'll also pass in 5 values of null, not worried about filtering and sorting quite yet.
        // The query method returns an instance of the Cursor class form android.database.
        // Now I'm getting back a cursor object that I can use to traverse the data set.
        // The cursor is a reference to the data that's returned from the query.
        Cursor cursor = mDatabase.query(ItemsTable.TABLE_ITEMS, ItemsTable.ALL_COLUMNS, null, null, null, null, null);

        // Use while loop here, and the condition will be to moveToNext().
        // The moveToNext() method returns a boolean value. If it succeeds in moving to a
        // valid row of data, It'll return true, otherwise it'll return false and you'll exit
        // the loop.
        while (cursor.moveToNext()) {

            // Within the loop, I'll create a DataItem object.
            // I'll use the no args constructor of my class.
            // Create a DataItem
            DataItem item = new DataItem();

            // The basic code you'll use for each column is as follows...
            // You'll start by referencing the item object, then you'll call the setter
            // method for one of the fields.

            // I'll set the item ID
            // Then you need to pass in either the name or the position of the column where the
            // data is stored. We know the order of the columns because we declared them in the
            // ALL_COLUMNS array. But instead of doing that, you should always use this bit of code,
            // where you pass in the name of the column.
            // Use the cursor.get() method, to get the type of data your're retrieving.
            // Then pass in cursor.getColumnIndex(), and finally pass in the name of the column,
            // which are the constants from the ItemsTable class. This needs to be done for every column.

            // Add the data to the DataItem from the database using the cursor
            item.setItemId(cursor.getString(cursor.getColumnIndex(ItemsTable.COLUMN_ID)));

            item.setItemName(cursor.getString(cursor.getColumnIndex(ItemsTable.COLUMN_NAME)));

            item.setDescription(cursor.getString(cursor.getColumnIndex(ItemsTable.COLUMN_DESCRIPTION)));

            item.setCategory(cursor.getString(cursor.getColumnIndex(ItemsTable.COLUMN_CATEGORY)));

            item.setSortPosition(cursor.getInt(cursor.getColumnIndex(ItemsTable.COLUMN_ID)));

            item.setPrice(cursor.getDouble(cursor.getColumnIndex(ItemsTable.COLUMN_PRICE)));

            item.setImage(cursor.getString(cursor.getColumnIndex(ItemsTable.COLUMN_IMAGE)));

            // We now have 1 setter for each of the columns. I'm using the getString()
            // method where I know I'm getting back a String, getInt() where I know I'm getting an
            // integer, and getDouble whn the column is set to the type of real in the sqlite database.

            // Finally after I set all of the column values to the data item, I'm adding that data item to my list.
            dataItems.add(item);
        }

        // At the end of all the code, I'm returning the dataItems list
        return dataItems;
    }
}
