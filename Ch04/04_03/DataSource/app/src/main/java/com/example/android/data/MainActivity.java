package com.example.android.data;

import android.content.Intent;
import android.content.SharedPreferences;
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

    // The MainActivity can use the DataSource to go get the data, but it doesn't have
    // to talk directly to the OpenHelper. That's the job of the DataSource.
    // This will make the app a lot more maintainable. All of my code that
    // inserts, updates, and otherwise works with data, will be in the DataSource class.
    // Only the DataSource class will know where that data is actually coming from.
    // Define DataSource object
    DataSource mDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate DataSource object
        mDataSource = new DataSource(this);

        // Open the DataSource object when the activity starts up
        mDataSource.open();

        // Despite the redundancy, the point is that whenever I start up the
        // activity, I will have a valid reference to the DataSource object.

        // But that connection can be interrupted, when the device configuration changes,
        // such as when the orientation goes from portrait to landscape.
        // So I'm also going to add code to the onResume() and onPause()
        // lifecycle methods.

        Toast.makeText(this, "Database acquired!", Toast.LENGTH_SHORT).show();

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

    // Now no matter what happens, whenever the activity is active,
    // that is whenever the user's interacting with it, I'll have a
    // valid database connection.

    // But if they close the activity, or the Activity simply goes away
    // so that another one can be displayed, then I'll close the DataSource
    // and that will close the Database.

    // It is very important to prevent database leaks. A database leak is where
    // where you create a database reference and then you never close it.
    // By making sure I close it in the onPause() method, I'm making sure that
    // that doesn't happen.

    @Override
    protected void onPause() {
        super.onPause();
        // Close the DataSource object's connection when the app is paused
        mDataSource.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Open the DataSource object's connection when the app resumes
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
