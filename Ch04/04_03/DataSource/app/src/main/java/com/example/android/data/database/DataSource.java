package com.example.android.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by romellbolton on 1/9/18.
 */

// Once you've created the DB helper class, the next step in following the best
// practices for managing SQLite is to create another class called a DataSource.
// The DataSource class will talk to the SQLiteOpenHelper, and it's methods will
// be called by the rest of the application.

// You typically only have 1 SQLiteOpenHelper class per db, but you can have as many
// DataSource classes as you need. For example, you might decide to create one DataSource
// for each table in a database, or one DataSource for each set of Tasks. How you
// architect that is up to you. In this simple app, I'll use a single DataSource class.

// In summation, we will manage the database with the DataSource class
// The MainActivity class is only responsible for creating the DataSource,
// opening the DataSource, and closing the DataSource

// The MainActivity can use the DataSource to go get the data, but it doesn't have
// to talk directly to the OpenHelper. That's the job of the DataSource.
// This will make the app a lot more maintainable. All of my code that
// inserts, updates, and otherwise works with data, will be in the DataSource class.
// Only the DataSource class will know where that data is actually coming from.
public class DataSource {

    // These fields will be private because only the DataSource class
    // will have to deal with them

    // Declare context field
    private Context mContext;

    // Declare database field
    private SQLiteDatabase mDatabase;

    // Declare openHelper field
    SQLiteOpenHelper mDbHelper;

    // Generate constructor method
    // Pass context so now when I create a DataSource object,
    // I'll be passing in the current context.

    // As I'm creating the DataSource object, I'm passing the context in, and that
    // can either be an Activity, or it can be the application context.
    public DataSource(Context context) {

        // Now as I create the Data source object, I'm creating all the objects I need to
        // talk to the backend database.

        // Save context reference
        mContext = context;

        // Save dbHelper reference
        mDbHelper = new DBHelper(context);

        // Get database reference
        mDatabase = mDbHelper.getWritableDatabase();
    }

    // Make code easier to use by adding a couple of methods called open and close.

    // This method explicitly opens the database
    public void open() {
        // Get database reference
        mDatabase = mDbHelper.getWritableDatabase();
    }

    // This method explicitly closes the database
    public void close() {
        // Closes any database connections that are already open
        mDbHelper.close();
    }

}
