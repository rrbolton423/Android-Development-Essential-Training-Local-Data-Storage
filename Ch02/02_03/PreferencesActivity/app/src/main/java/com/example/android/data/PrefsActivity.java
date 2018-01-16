package com.example.android.data;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/*
PreferenceActivity's supports the simple handling of preferences.
It can show a set of preferences to the user
It is usually used to create a settings screen for applications.
The values are stored in SharedPreferences automatically as soon as
user selects a preference setting. It frees the programmer from explicitly
saving preference values.

You can manage preferences through preference activities. These are activities
with an automated user interface that's managed by the framework. You define your
preferences in an XML file, similar to an XML menu file, and then access the
values with java code.

You should wrap your preferences screen in a fragment, so that it maintains it's
state as the devices orientation or other configuration changes.

 */
public class PrefsActivity extends AppCompatActivity {

    private static final String TAG = "PrefsActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefs);

        // Load the fragment from the activity
        getFragmentManager()
                .beginTransaction()
                // Provide container layout
                // ID of Frame layout in the xml file
                // Also instantiate new SettingsFragment class and pass it in
                .add(R.id.prefs_content, new SettingsFragment())
                // commit the transaction
                .commit();

        // So now as the activity loads, it loads the fragment into the container.
        // The fragment itself is responsible for going and getting the preference
        // definition and loading that.

    }

    // Define member class that extends PreferenceFragment
    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Add preferences
            // Fragment loads the settings
            // Inflates the given XML resource and
            // adds the preference hierarchy to the current preference hierarchy.
            addPreferencesFromResource(R.xml.settings);

        }
    }
}

