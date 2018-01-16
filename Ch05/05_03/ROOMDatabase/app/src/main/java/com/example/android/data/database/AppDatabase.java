package com.example.android.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.android.data.model.DataItem;

/**
 * Created by romellbolton on 1/13/18.
 */

// AppDatabase class must extend RoomDatabase, and you should mark this class as
// abstract because you won't be instantiating this class directly. Instead,
// you'll be adding annotations to the class definitions that Room uses
// to generate code in the background. It'll create an implementing class,
// and that's what will be used at run time.
// You don't make the app database yourself, you just create a singleton.
// The AppDatabase class will become an implementing class at runtime, due
// to the annotation, and the ROOM db background code will implement this class
// run the background code.

// Add required annotation for a ROOM Database class
// Required properties include ...

// Arg 1: an array called "Entities", which is a comma-delimited list of your
// Entity classes. Each Entity is the name of the class, followed by ".class".
// If you have more than one, separate them with commas

// Arg 2: version number of the database structure
@Database(entities = {DataItem.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    // This class should behave like a conventional java singleton.
    // A singleton ensures a class has only one instance

    // Create an instance field of the RoomDatabase
    private static AppDatabase instance;

    // Create factory method
    // Create a getInstance() method that returns the single instance of this RoomDatabase
    public static AppDatabase getInstance(Context context) {

        // As with all singletons, its goal's to make sure that it always returns
        // the same reference.
        if (instance == null) {

            // If the instance is null, initialize it by using a Builder object
            // That's available from the Room class
            // The first argument is the context, but it should be the application context.
            // So use the expression "context.getApplicationContext()".
            // Next we need the class that needs to be instantiated to define the
            // database itself, which would be this class, AppDatabase.
            // And finally, pass in the name of the database as it will be named in
            // persistent storage.
            // Call .build() to build the Builder object.
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "app-database")
                    // By default, a Room database requires all queries to be executed in a background thread.
                    // This can be done with an AsyncTask, with a service, or with a number of other
                    // architectures. But during initial development and testing, it can be useful to allow
                    // query execution on the main thread. And to do just that, call the method
                    // "allowMainThreadQueries()". You'll want to get rid of that line of code before
                    // you go to production though.
                    .allowMainThreadQueries()
                    .build();

            // Now you've initialized the instance
        }

        // After the conditional logic, return the instance with a simple return statement
        return instance;
    }

    // This method will be used to de-reference the database object within the singleton.
    // This lets you clean up the reference and avoid leaks.
    // It will have a single line of code setting the instance to null.
    public static void destroyInstance() {

        // Set the instance of the RoomDatabase to be null
        instance = null;

    }

    // This class is very close to behaving like a classic java singleton, but one
    // thing you shouldn't do is to create a private constructor method. Classic java
    // singletons frequently have a private constructor to prevent the class from being
    // instantiated incorrectly from other parts on the app. But the Room library needs a
    // no-arguments constructor to work properly.

    // To test all of this code, select Build, Make Module app. That will cause all of
    // the Room annotations to be interpreted. If you don't see any errors, your code should
    // work. If you want to see all of the possible warnings though, select Build, Rebuild Project.
    // Go to the messages window to view the warnings.
}



