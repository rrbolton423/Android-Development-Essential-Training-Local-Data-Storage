package com.example.android.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

    public void seedDatabase(List<DataItem> dataItemList) {

        // Database Transactions let you ensure your data's integrity.
        // SQLite supports simple transaction management.

        // This code will add the data to the Database as long as there is
        // no "House Salads" being added to the database. If there is, I will
        // roll back all changes made to the Database by ending the transaction.
        // Otherwise, all of the data will be added and all changes will be saved
        // as a successful transaction, and will be committed when ending the transaction.

        try {

            // Create the transaction at the beginning
            // After you've done a bunch of operations, if you want to save the
            // transaction or commit it, here are the steps...
            mDatabase.beginTransaction();

            // Loop through all of the data, inserting 1 row at a time
            for (DataItem item : dataItemList) {

                try { // Try to create an item and add it to the database 1 at a time

                    // If you want to rollback a transaction ...

                    // But if I get to an item that has the name of "House Salad",
                    // I'll throw an exception or some might say a fit, and the code
                    // will jump to the Exception block.
                    // The key thing to note is that I would not have called
                    // setTransactionSuccessful(), and that will roll back the changes,
                    // instead of committing them to the Database.
                    if (item.getItemName().equals("House Salad")) {

                        // Throw a new exception
                        throw new Exception("I don't like salad!");
                    }

                    // Create item if it is not a "House Salad"
                    createItem(item);

                } catch (SQLiteException e) {
                    
                    e.printStackTrace();

                }
            }

            // Then if everything goes well, say the transaction was successful
            mDatabase.setTransactionSuccessful();

            // End the transaction, everything will be saved
            // Commit the transaction
            mDatabase.endTransaction();

        } catch (Exception e) {

            e.printStackTrace();

            // Display error message
            Log.i("DataSource", "seedDatabase: " + e.getMessage());

            // Do not "commit" the transaction,
            // instead, "rollback" the transaction
            // There is no specific rollback method.
            // Instead, the rule is if I don't say that the transaction
            // was successful, then it wasn't. And if I end the transaction
            // without having called setTransactionSuccessful(), then it will
            // be like nothing happened.

            // Nothing will end up in the Database
            mDatabase.endTransaction();
        }
    }

    public List<DataItem> getAllItems(String category) {
        List<DataItem> dataItems = new ArrayList<>();

        Cursor cursor = null;
        if (category == null) {
            cursor = mDatabase.query(ItemsTable.TABLE_ITEMS, ItemsTable.ALL_COLUMNS,
                    null, null, null, null, ItemsTable.COLUMN_NAME);
        } else {
            String[] categories = {category};
            cursor = mDatabase.query(ItemsTable.TABLE_ITEMS, ItemsTable.ALL_COLUMNS,
                    ItemsTable.COLUMN_CATEGORY + "=?", categories, null, null, ItemsTable.COLUMN_NAME);
        }

        while (cursor.moveToNext()) {
            DataItem item = new DataItem();
            item.setItemId(cursor.getString(
                    cursor.getColumnIndex(ItemsTable.COLUMN_ID)));
            item.setItemName(cursor.getString(
                    cursor.getColumnIndex(ItemsTable.COLUMN_NAME)));
            item.setDescription(cursor.getString(
                    cursor.getColumnIndex(ItemsTable.COLUMN_DESCRIPTION)));
            item.setCategory(cursor.getString(
                    cursor.getColumnIndex(ItemsTable.COLUMN_CATEGORY)));
            item.setSortPosition(cursor.getInt(
                    cursor.getColumnIndex(ItemsTable.COLUMN_ID)));
            item.setPrice(cursor.getDouble(
                    cursor.getColumnIndex(ItemsTable.COLUMN_PRICE)));
            item.setImage(cursor.getString(
                    cursor.getColumnIndex(ItemsTable.COLUMN_IMAGE)));
            dataItems.add(item);
        }
        cursor.close();
        return dataItems;
    }
}
