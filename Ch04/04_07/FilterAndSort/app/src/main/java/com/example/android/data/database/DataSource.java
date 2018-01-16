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

    // First, it's going to receive a String argument named category.
    // The rule will be if "category" is null, that means they want all of the data,
    // but if it's a valid String, then I'll filter on that String as the category.
    public List<DataItem> getAllItems(String category) {
        List<DataItem> dataItems = new ArrayList<>();

        Cursor cursor = null;
        // If the cursor is null ...

        if (category == null) {

            cursor = mDatabase.query(ItemsTable.TABLE_ITEMS, ItemsTable.ALL_COLUMNS,
                    null, null, null, null, ItemsTable.COLUMN_NAME);
        } else {

            // If the category isn't null, that means the user is asking to filter on a particular
            // category.

            // This is how you do a filter. The third argument is called the "selection" clause.
            // This is where you put in your raw SQL statement code, and you start each filter with the name
            // of the column you're filtering on, and then either an equals or a greater than or
            // whatever other operator you want to use.
            //
            // I want to match the name of the category, so I'll replace the null value with the
            // ItemsTable.COLUMN_CATEGORY, and then I'll append to that "=?". The question mark is
            // a placeholder.
            //
            // Now the next argument is an array of Strings called "selectionArgs".
            // You pass in an array of Strings that has the same number of items as the number of
            // placeholders in your filtering clause. I'll create that code above the cursor call.

            String[] categories = {category};
            // If you have more than one placeholder, you'll have more than one argument. And you
            // simply pass them into those braces as a comma delimited list.

            // Then in the query() method, I'll pass in "categories" into the "selectionArgs" parameter.

            // And now when the query is processed, the SQLite framework will put together the SQL statement
            // for me.
            // It'll take my String, which starts with the name of the column (COLUMN_CATEGORY),
            // and ends with the operator and the placeholder, and it will insert each of the arguments
            // by their order in the array into the placeholder references.

            cursor = mDatabase.query(ItemsTable.TABLE_ITEMS, ItemsTable.ALL_COLUMNS,
                    ItemsTable.COLUMN_CATEGORY + "=?", categories, null, null, ItemsTable.COLUMN_NAME);


        }
        // So far in my sample application, I'm displaying all of my DataItems in their raw sort order,
        // but I'd like to be able to explicitly sort and filter the data. So I've added a few new
        // features to this version of the project.

        // The query() method takes a argument called orderBy. This is a straight old SQL orderBy clause.
        // If you just want to sort in ascending order by a single column, you just need to pass in
        // the column name. Sort by the Column name in alphabetical order ascending.

        // The sort order, as far as the code goes, is only a simple String. If you want to sort by more than
        // one column, pass in a comma delimited list, of column names. Or if you want to sort on descending
        // order, just pass in the appropriate SQL keyword "desc". Whatever String you pass in, just
        // becomes a part of the SQL statement.

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

        // After you finish with the cursor object, you should always close it.
        // If you forget to do that, you might see some errors creep into your code.
        cursor.close();

        return dataItems;
    }
}
