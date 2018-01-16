package com.example.android.data;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.data.database.AppDatabase;
import com.example.android.data.events.DataItemsEvent;
import com.example.android.data.model.DataItem;
import com.example.android.data.utils.JSONHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final int SIGNIN_REQUEST = 1001;
    public static final String MY_GLOBAL_PREFS = "my_global_prefs";
    private static final int REQUEST_PERMISSION_WRITE = 1002;
    private static final String TAG = "MainActivity";
    List<DataItem> dataItemList;
    private boolean permissionGranted;

    private AppDatabase db;

    // The goal is to move data from the background thread, to the
    // foreground thread.
    // The EventBus library works with background and foreground threads in Android apps.

    // Use Executor class that manages threads for you.
    // Apart of the Java concurrency framework.
    // Then I'll need to be able to communicate between the threads.
    // When I get data from a database, I'll need a way to send it back
    // to the main thread in some way. There are a number of ways to do this,
    // but I'm going to use an open-source library named EventBus,that lets
    // you easily dispatch and handle custom events. You can use EventBus to
    // communicate between different tiers of your app, such as between services
    // and activities, or, as in this example, between different threads.

    // I'm going to be using a Java executor class. The executor can manage a pool
    // of threads. Now, I want to make sure that I'm only running one database
    // operation at a time, so I'm going to create an executor that works like this...

    // By instantiating it with "Executors.newSingleThreadExecutor();", I can now
    // reuse this this executor object as many times as I want, and the different
    // operations will be executed one at a time.
    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(this);

        /*
        STEP 1. When the app first starts up, insert the data if needed on the
        background thread.
         */

        // Use executor object and call it's execute() method
        // Then I'll pass in a new instance of the Runnable interface
        // That generates a run() method.
        executor.execute(new Runnable() {
            @Override
            public void run() {

                // Now, these operations, retrieving the count of rows,
                // and then possibly inserting data, will be executed in the background
                // thread.
                // All of this code is being executed in a background thread.

                int itemCount = db.dataItemDao().countItems();
                if (itemCount == 0) {
                    List<DataItem> itemList = JSONHelper.importFromResource(MainActivity.this);
                    db.dataItemDao().insertAll(itemList);
                    Log.i(TAG, "onCreate: data inserted");
                } else {
                    Log.i(TAG, "onCreate: data already exists");
                }

                /*
        STEP 2. Then I retrieve the data and dispatch the event
         */

                // Now I need to do the same thing for the code that retrieves the data
                // from the database.
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        // Take the one line of code that's retrieving the data

                        dataItemList = db.dataItemDao().getAll();
                        // Now, these operations, retrieving the data, will be
                        // executed in the background thread.

                        // Now when I get the data from the database, I need a way of
                        // getting it to the main thread. And this is where EventBus
                        // will be incredibly valuable.

                        // To dispatch an event, I'll use this code.
                        EventBus.getDefault().
                                // Then I'll create a new instance of my class DataItemsEvent,
                                // and I'll pass in the DataItem list.
                                        // Store list of db data in a POJO class
                                post(new DataItemsEvent(dataItemList));

                        // Doing this dispatches the event, and now it's up to me to
                        // also receive and handle the event.

                    }
                });
            }
        });

        checkPermissions();

        // Finally I need to register EventBus itself. Ill do this at the end
        // of the onCreate() method.
        // This allows me to receive events using EventBus
        EventBus.getDefault().register(this);

    }

     /*
        STEP 3. EventBus then calls the method "dataItemEventHandler()", that
        I've subscribed, but because of my thread mode declaration, it calls it
        on the main thread.
         */

    // To receive and handle the event...

    // This method will receive an instance of the Event class.

    // To cause this method to be called, I'll add an EventBus annotation
    // name @Subscribe.
    // The @Subscribe annotation is an instruction to EventBus to register this
    // method and to call it when the event is received.
    // Now by default, the event will be called on the same thread from which it was
    // dispatched. But I want to change that. I want to call this method on
    // the main thread. To do that, I'll set a property called threadMode,
    // and set it to a constant of ThreadMode.MAIN.
    // So now I'm sending the event where I called
    // "EventBus.getDefault().post(new DataItemsEvent(dataItemList));",
    // and I'm receiving it on the main thread here.
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void dataItemsEventHandler(DataItemsEvent event) {

        // As the event is called, I'll retrieve the data from the event
        // with this code below ...
        dataItemList = event.getItemList();

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        boolean grid = settings.getBoolean(getString(R.string.pref_display_grid), false);

        DataItemAdapter adapter = new DataItemAdapter(this, dataItemList);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvItems);
        if (grid) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }

        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        AppDatabase.destroyInstance();

        // Unregister EventBus as the activity shuts down in the onDestroy() method
        // This allows me to stop receiving events using EventBus
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_signin:
                Intent intent = new Intent(this, SigninActivity.class);
                startActivityForResult(intent, SIGNIN_REQUEST);
                return true;
            case R.id.action_settings:
                // Show the settings screen
                Intent settingsIntent = new Intent(this, PrefsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.action_export:
                boolean result = JSONHelper.exportToJSON(this, dataItemList);
                if (result) {
                    Toast.makeText(this, "Data exported", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Export failed", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_import:
                List<DataItem> dataItems = JSONHelper.importFromResource(this);
                if (dataItems != null) {
                    for (DataItem dataItem:
                         dataItems) {
                        Log.i(TAG, "onOptionsItemSelected: " + dataItem.getItemName());
                    }
                }
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == SIGNIN_REQUEST) {
            String email = data.getStringExtra(SigninActivity.EMAIL_KEY);
            Toast.makeText(this, "You signed in as " + email, Toast.LENGTH_SHORT).show();

            SharedPreferences.Editor editor =
                    getSharedPreferences(MY_GLOBAL_PREFS, MODE_PRIVATE).edit();
            editor.putString(SigninActivity.EMAIL_KEY, email);
            editor.apply();

        }

    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }

    // Initiate request for permissions.
    private boolean checkPermissions() {

        if (!isExternalStorageReadable() || !isExternalStorageWritable()) {
            Toast.makeText(this, "This app only works on devices with usable external storage",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_WRITE);
            return false;
        } else {
            return true;
        }
    }

    // Handle permissions result
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_WRITE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = true;
                    Toast.makeText(this, "External storage permission granted",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "You must grant permission!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
