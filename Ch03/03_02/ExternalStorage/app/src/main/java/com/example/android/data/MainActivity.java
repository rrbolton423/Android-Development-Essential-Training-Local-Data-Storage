package com.example.android.data;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

// External storage can be seen by anyone, thus requires permissions
// External storage is public-facing and visible to the user
public class MainActivity extends AppCompatActivity {

    // Request code for permissions
    private static final int REQUEST_PERMISSION_WRITE = 1001;

    TextView output;
    public static final String FILE_NAME = "lorem_ipsum.txt";

    // Create boolean to keep track if the permission has been granted
    private boolean permissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        output = (TextView) findViewById(R.id.outputText);
        output.setText(R.string.ready_to_code);
    }

    // In order to get external storage information, use a method of the environment class
    // named getExternalStorageDirectory.
    private File getFile() {
        // Create a file object, and pass the environment variable, and calling
        // getExternalStorageDirectory() on itm and passing the file name in the directory.
        // Return the file from external storage
        return new File(
                // A file object representing my external storage directory
                Environment.getExternalStorageDirectory(),
                // String of the name of the file that I want to work with
                FILE_NAME);
    }

    public void onCreateButtonClick(View view) {

        // If the permission isn't granted...
        if (!permissionGranted) {

            // Check for permissions
            checkPermissions();

            // Return from the method
            return;
        }

        // Create content
        String string = getString(R.string.lorem_ipsum);

        // Declare fileOutputStream
        FileOutputStream fileOutputStream = null;

        // Create a file object and get the specified file from External storage
        File file = getFile();

        try {

            // Create a fileOutputStream and pass the file object created
            // Create the constructor of FileOutputStream and pass the file when working
            // with External storage
            fileOutputStream = new FileOutputStream(file);

            // Write to the fileOutputStream
            fileOutputStream.write(string.getBytes());

            Toast.makeText(this, "File written: " + FILE_NAME,
                    Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Exception: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        } finally {
            try {
                // Close the stream
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onReadButtonClick(View view) {

        // If the permission isn't granted...
        if (!permissionGranted) {

            // Check for permissions
            checkPermissions();

            // Return from the method
            return;
        }

    }

    public void onDeleteButtonClick(View view) {

        // If the permission isn't granted...
        if (!permissionGranted) {

            // Check for permissions
            checkPermissions();

            // Return from the method
            return;
        }

        // Create a file object and get the specified file from External storage
        File file = getFile();

        // If the file exists ...
        if (file.exists()) {

            // Delete the file
            // the delete method is used for External storage
            file.delete();

            Toast.makeText(this, "File deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "File doesn't exist", Toast.LENGTH_SHORT).show();
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
