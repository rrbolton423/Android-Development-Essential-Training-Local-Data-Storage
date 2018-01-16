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

import com.example.android.data.model.DataItem;
import com.example.android.data.sample.SampleDataProvider;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int SIGNIN_REQUEST = 1001;
    public static final String MY_GLOBAL_PREFS = "my_global_prefs";
    List<DataItem> dataItemList = SampleDataProvider.dataItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Collections.sort(dataItemList, new Comparator<DataItem>() {
            @Override
            public int compare(DataItem o1, DataItem o2) {
                return o1.getItemName().compareTo(o2.getItemName());
            }
        });

        DataItemAdapter adapter = new DataItemAdapter(this, dataItemList);

        // Before we inflate the RecyclerView, I am going to create an instance
        // of the SharedPreferences class, and get its reference.

        // When you define and manage your references in an Activity, those preferences are
        // saved in something called the default preference set.
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        // Find out whether the user wants to display things in a grid.
        // That's a boolean value, a check box in the settings screen.
        // Get the resource id of the checkBox from the layout (prefs_display_grid),
        // and provide a default value to be returned if this preference isn't found (false)
        boolean grid = settings.getBoolean(getString(R.string.pref_display_grid), false);

        // Now we can find out as the Activity starts up, what the user wants to see.
        // We'll implement this by adding an if condition here ...

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvItems);

        // If the user's SharedPreferences settings are for the Grid layout ...
        if (grid) {

            // override the default layout manager of the recyclerView,
            // which was a LinearLayoutManager in the xml file.
            recyclerView.setLayoutManager(new GridLayoutManager(
                    // context
                    this,
                    // will create a display of 3 columns
                    3));
        }

        // Set the adapter
        recyclerView.setAdapter(adapter);

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
