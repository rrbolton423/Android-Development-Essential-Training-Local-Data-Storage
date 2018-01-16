package com.example.android.data.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.android.data.R;
import com.example.android.data.model.DataItem;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class JSONHelper {

    private static final String FILE_NAME = "menuitems.json";
    private static final String TAG = "JSONHelper";

    public static boolean exportToJSON(Context context, List<DataItem> dataItemList) {

        DataItems menuData = new DataItems();
        menuData.setDataItems(dataItemList);

        Gson gson = new Gson();
        String jsonString = gson.toJson(menuData);
        Log.i(TAG, "exportToJSON: " + jsonString);

        FileOutputStream fileOutputStream = null;
        File file = new File(Environment.getExternalStorageDirectory(), FILE_NAME);

        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(jsonString.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }


    public static List<DataItem> importFromJSON(Context context) {

        FileReader reader = null;

        try {
            File file = new File(Environment.getExternalStorageDirectory(), FILE_NAME);
            reader = new FileReader(file);
            Gson gson = new Gson();
            DataItems dataItems = gson.fromJson(reader, DataItems.class);
            return dataItems.getDataItems();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    // This method reads and imports the data from the json file that's
    // stored as a file resource. That is how you work with data that you
    // include in the project as a compiled resource. Again, this was for static
    // data that won't change, and for relatively small amounts of data.
    // Access to this sort of file is fast, robust, and easy to code using
    // java input streams.

    // This method receives a context reference, and returns a List of DataItem objects
    public static List<DataItem> importFromResource(Context context) {

        // To read a resource, you'll use an InputStream,
        // and a InputStreamReader, initializing both to null
        InputStreamReader reader = null;
        InputStream inputStream = null;

        try {
            // Initialize InputStream, inputting the raw json file from the resources directory
            inputStream = context.getResources().openRawResource(R.raw.menuitems);

            // Initialize the Reader, to read the inputStream
            reader = new InputStreamReader(inputStream);

            // Create the GSON object to convert from JSON
            Gson gson = new Gson();

            // Pass the reader, and the type to convert to
            DataItems dataItems = gson.fromJson(reader, DataItems.class);

            // Return the list of DataItems
            return dataItems.getDataItems();

        } finally { // Finally always runs it's code

            // Close the reader
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Close the input stream
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class DataItems {
        List<DataItem> dataItems;

        public List<DataItem> getDataItems() {
            return dataItems;
        }

        public void setDataItems(List<DataItem> dataItems) {
            this.dataItems = dataItems;
        }
    }

}
