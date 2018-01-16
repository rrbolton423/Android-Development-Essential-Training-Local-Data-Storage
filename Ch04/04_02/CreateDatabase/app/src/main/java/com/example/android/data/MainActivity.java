package com.example.android.data;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.data.database.DBHelper;
import com.example.android.data.model.DataItem;
import com.example.android.data.sample.SampleDataProvider;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int SIGNIN_REQUEST = 1001;
    public static final String MY_GLOBAL_PREFS = "my_global_prefs";
    List<DataItem> dataItemList = SampleDataProvider.dataItemList;

    // Create a reference to a SQLite db object
    // Will initialise the db in the onCreate() method.
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create an instance of my OpenHelper object, and cast it as it's super class,
        // SQLiteOpenHelper. This will allow me to call methods of the super class easily.
        // But when I instantiate the object, I'll use my class, DBHelper, and pass "this" as the context.
        SQLiteOpenHelper dbHelper = new DBHelper(this);

        // Then to open the db, I'll call this ...
        // When you call the getWritable database method, that will automatically call the
        // onCreate method if the db hasn't been created previously. That will result in executing
        // the required SQL and the db will have been created.
        database = dbHelper.getWritableDatabase();

        // Add toast message to see that it has happened successfully
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
