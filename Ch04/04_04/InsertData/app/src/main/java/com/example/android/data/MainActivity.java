package com.example.android.data;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.data.database.DataSource;
import com.example.android.data.model.DataItem;
import com.example.android.data.sample.SampleDataProvider;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int SIGNIN_REQUEST = 1001;
    public static final String MY_GLOBAL_PREFS = "my_global_prefs";
    List<DataItem> dataItemList = SampleDataProvider.dataItemList;

    DataSource mDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDataSource = new DataSource(this);
        mDataSource.open();
        Toast.makeText(this, "Database acquired!", Toast.LENGTH_SHORT).show();

        // Before I sort the dataItemsList, I'm going to take that data that I've acquired
        // from the Java class (SampleDataProvider), and I'm going to insert it all into my database
        // table. I'll be working with the dataItemList field that was created previously.

        // When the application starts up, I want to find out whether there's data already in the database
        // table. To get that information I'll add another method to the DataSource class.
        // I return the number of items in the database table to the variable "numItems".
        long numItems = mDataSource.getDataItemsCount();

        // If numItems equals 0, or if there is no items in the database table ...
        // Only try to insert data into the database if it doesn't already exist.
        if (numItems == 0) {
            // Loop through the "dataItemList" and get each value from it and assign it to "item"
                for (DataItem item : dataItemList) {
                    // There may be problems with the database insert.
                    // For example, I might try to insert a row, where the primary key value matches
                    // the value that's already in the database table.
                    // I'll deal with that by wrapping this code and surrounding it with a try / catch
                    // block. I'll change the type of Exception to SQLiteException.
                    try {

                        // In the loop, I'll call the createItem() method from the DataSource object
                        // and I'll pass in the data item.
                    mDataSource.createItem(item);

                } catch (SQLiteException e) {

                e.printStackTrace();
            }
        }
            Toast.makeText(this, "Data inserted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Data already inserted!", Toast.LENGTH_SHORT).show();
        }

        Collections.sort(dataItemList, new Comparator<DataItem>() {
            @Override
            public int compare(DataItem o1, DataItem o2) {
                return o1.getItemName().compareTo(o2.getItemName());
            }
        });

        DataItemAdapter adapter = new DataItemAdapter(this, dataItemList);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        boolean grid = settings.getBoolean(getString(R.string.pref_display_grid), false);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvItems);
        if (grid) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }

        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDataSource.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDataSource.open();
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
}
