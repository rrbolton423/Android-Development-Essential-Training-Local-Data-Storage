package com.example.android.data.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.android.data.model.DataItem;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
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


    // This method returns a list of DataItem objects
    public static List<DataItem> importFromJSON(Context context) {

        // Import data by using the importFromJson() method

        // For the reading operation, we'll use a java FileReader object
        FileReader reader = null;

        try {

            // Find the JSON file specified in External storage to import from JSON
            File file = new File(
                    // Pass the External storage directory
                    Environment.getExternalStorageDirectory(),
                    // Pass the file name
                    FILE_NAME);

            // Instantiate the reader object, passing in the file
            // Read the existing json file
            reader = new FileReader(file);

            // The DataItems class is there to wrap my list, the arrayList of DataItems
            // objects.
            // We are going to create a new instance of that class, but this time
            // we're going to get it using the GSON library. GSON will do all the hard work

            // Create and instantiate GSON object
            Gson gson = new Gson();

            // Get the data
            // Declare a DataItems object
            // And this time instead of constructing it from scratch, I'll let
            // GSON get it from the file
            // GSON does all of the hard work. It's going to...
            // 1. Open the file
            // 2. Read the structure data into memory
            // 3. Recast the data as the appropriate types

            // Convert the json file to a DataItems object
            DataItems dataItems =
                    gson.fromJson(
                            // Pass in the reader (that is reading the json file)
                            reader,
                            // Pass in the class that I want to convert to (DataItems list)
                            DataItems.class);

            // IF ALL GOES WELL...
            // I should be able to extract my ArrayList of data from the DataItems object.
            // So I'll return dataItems.getDataItems()

            // This method returns a list
            return dataItems.getDataItems();

        } catch (FileNotFoundException e) {
            // Print the exception
            e.printStackTrace();
        } finally {
            // Close the reader
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
