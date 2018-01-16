package com.example.android.data;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.data.database.DataSource;
import com.example.android.data.model.DataItem;
import com.example.android.data.sample.SampleDataProvider;

import java.util.List;

// I'm responding to a user request  to filter the data. I'm doing that by executing
// an SQL statement that's being put together by the query method of the SDK.
// The query method is returning the data set, I'm iterating through the data set,
// adding it to a list, and returning that list. And in the MAinActivity, I'm using the
// RecyclerView and the adapter to display the requested data.
public class MainActivity extends AppCompatActivity {

    private static final int SIGNIN_REQUEST = 1001;
    public static final String MY_GLOBAL_PREFS = "my_global_prefs";
    private static final String TAG = "MainActivity";
    List<DataItem> dataItemList = SampleDataProvider.dataItemList;

    DataSource mDataSource;
    List<DataItem> listFromDB;

    // Get reference to the drawer layout
    DrawerLayout mDrawerLayout;

    // Get reference to the ListView that will display the items
    ListView mDrawerList;

    // Get reference to the categories array
    String[] mCategories;

    RecyclerView mRecyclerView;
    DataItemAdapter mItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//      Code to manage sliding navigation drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mCategories = getResources().getStringArray(R.array.categories);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Using an array adapter, passing in the ID of a layout file,
        // drawer_list_item, which is simply a TextView, and is pulled from the
        // Android SDK, and then I'm passing in my data, the categories list.
        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, mCategories));

        // Then when the user selects an item, I'm getting the category from the data,
        // I'm displaying the Toast message, and then I'm closing the navigation drawer.
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Get the category that the user selected
                String category = mCategories[position];

                Toast.makeText(MainActivity.this, "You chose " + category,
                        Toast.LENGTH_SHORT).show();

                mDrawerLayout.closeDrawer(mDrawerList);

                // respond to the category request by calling ...
                displayDataItems(category);
                // and passing in the requested category
            }
        });
//      end of navigation drawer

        // Now when the user selects an item from that list, I want to display only the data items that
        // match that category. That's the work that I'm going to do in the DataSource class.

        mDataSource = new DataSource(this);
        mDataSource.open();
        mDataSource.seedDatabase(dataItemList);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        boolean grid = settings.getBoolean(getString(R.string.pref_display_grid), false);

        mRecyclerView = (RecyclerView) findViewById(R.id.rvItems);
        if (grid) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }

        // Pass in null on startup to see every data item when the app first loads
        displayDataItems(null);
    }

    // I've moved the code to retrieve data into a method named displayDataItems().
    // This will allow me to call it as many times as I need to.
    // I'm getting the data from the database, creating the adapter object, and then
    // calling setAdapter(0 on the recycler view.
    //  So each time I execute a filter, I'm recreating all of these objects.
    private void displayDataItems(String category) {
        // When I call getAllItems() I now need to pass in a String as the category.
        // I'll get that value by adding an argument to this method, and I'll pass in the category
        // there.
        listFromDB = mDataSource.getAllItems(category);
        mItemAdapter = new DataItemAdapter(this, listFromDB);
        mRecyclerView.setAdapter(mItemAdapter);
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
            case R.id.action_all_items:
                // display all items
                // Pass null as the category to see everything
                displayDataItems(null);
                return true;
            case R.id.action_choose_category:
                //open the drawer
                mDrawerLayout.openDrawer(mDrawerList);
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
