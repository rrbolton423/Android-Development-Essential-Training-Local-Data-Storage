package com.example.android.data;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

// InternalStorage cannot be seen by the device user, or anyone else,
// so no permissions are required from the user
// Internal storage is private to the app
public class MainActivity extends AppCompatActivity {

    TextView output;

    // Name of the file I want to create
    public static final String FILE_NAME = "lorem_ipsum.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        output = (TextView) findViewById(R.id.outputText);
        output.setText(R.string.ready_to_code);
    }

    public void onCreateButtonClick(View view) {
        // Get content to write to a file
        String string = getString(R.string.lorem_ipsum);

        // Next I need a file output stream
        // This is a standard Java FileOutputStream
        FileOutputStream fileOutputStream = null;

        // We're going to have to wrap all of the code working directly with
        // the file output stream in a try-catch block.
        // And we're going to have to close the stream in a finally block.

        // Declare a file object, passing the file I want to create
        File file = new File(FILE_NAME);

        try {
            // Instantiate FileOutPutStream by calling openFileOutput
            // Pass the name of the file you want to write to, and a mode.
            fileOutputStream = openFileOutput(FILE_NAME, MODE_PRIVATE);

            // Write the File to storage
            // the method is looking for a byte array, and we'll get that from
            // the String by calling getBytes on it.
            fileOutputStream.write(string.getBytes());

            // Tell user the file was written to storage
            Toast.makeText(this, "File written: " + FILE_NAME, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {
            // Close the Output Stream
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public void onReadButtonClick(View view) {
    }

    public void onDeleteButtonClick(View view) {
        // Add code to delete the file

        // Create a file passing the path, (file location)
        // which is the FileDirectory, and the name of the file
        File file = new File(getFilesDir(), FILE_NAME);

        // if the file exists ...
        if (file.exists()) {

            // Call the deleteFile() method to delete the file
            // Just pass the file name, not the entire file location
            deleteFile(FILE_NAME);

            Toast.makeText(this, "File deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "File doesn't exist", Toast.LENGTH_SHORT).show();
        }
    }
}
