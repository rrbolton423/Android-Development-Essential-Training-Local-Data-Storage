package com.example.android.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by romellbolton on 1/9/18.
 */

// The execution of the SQL statements in the ItemsTable class
// will go into this OpenHelper class
public class DBHelper extends SQLiteOpenHelper {

    // Need two constants that represent the name of the database file, and the current version
    // of my database structure
    public static final String DB_FILE_NAME = "nadias.db";

    // DB version is managed by the framework. It must be an integer
    // Your first DB structure can be number one, but if you need to change the DB's structure
    // after you've released an application, each time you release a new version of the structure,
    // you should increment this value by one.
    public static final int DB_VERSION = 1;

    // Generated constructor with 4 args, but that's more than what I need.
    // My constructor will only be called by my code, so I can decide how to structure it,
    // and I'd like to simplify it. I will delete the last three args, and only only save the context.
    // The only thing I'm going to save is the context, and then I'll replace the name and
    // version with my constants (DB_FILE_NAME, and DB_VERSION)
    // I don't need a factory object so I can pass in null for that.
    public DBHelper(Context context) {
        super(context, DB_FILE_NAME, null, DB_VERSION);
    }

    // Now to customize my onCreate() method
    // The first time the apps opens and you open the SQLiteOpenHelper object, I'll call the
    // onCreate() method automatically and pass in a reference to the managed database file.
    // It's up to me to actually execute the code to create the tables you need, and this is where the
    // ItemsTable class becomes important. I already have a constant available named SQL_CREATE that
    // has the required SQL statement.
    @Override
    public void onCreate(SQLiteDatabase db) {
        // To create that table call this ...
        db.execSQL(ItemsTable.SQL_CREATE);

        // If my db is going to have more than one table, I'll call the create statements from each
        // of the tables in turn. That's all we need to do in the onCreate() method
    }

    // The onUpgrade() method is slightly more complex ...
    // Let's say that I released a version of the application and it has db version 1.
    // Then I restructure the db, make changes, and I increment the db version to 2.
    // The first time the user opens the new version of the app, the onUpgrade() method will
    // be called. Once again, I'll get a db reference, but this time you'll get the old and new version.
    // It's up to me to write the custom code that's needed, say to add columns to a table, you do this...
    // but if you just want to wipe out the database and start over again, you can do this ...

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Wipe out the db structure and create it from scratch

        // Drop or delete the table
        db.execSQL(ItemsTable.SQL_DELETE);

        // Create the database again
        // If you want to maintain data that's already in the db, you could perhaps export the
        // current data, save to a JSON file, then drop the db, recreate it, and then re-import the data,
        // and have that all happen in a single method.
        // But for our purposes, simply wiping out the db structure and creating it from
        // scratch will be good enough.
        onCreate(db);

        // Now I'll add some code to my MainActivity to use this code that I've just created.
    }
}
