package com.example.android.data.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.android.data.model.DataItem;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

// This class takes some data, and creates a JSON file
public class JSONHelper {

    private static final String FILE_NAME = "menuitems.json";
    private static final String TAG = "JSONHelper";

    public static boolean exportToJSON(Context context, List<DataItem> dataItemList) {

        // Declare instance of that new class that does not contain a generic declaration
        DataItems menuData = new DataItems();

        // Now we have an object that can be exported easily with the GSON library
        // call setDataItems() passing the dataItemList from the MainActivity
        // The list data is now set to the classes field "dataItems".
        menuData.setDataItems(dataItemList);

        // Create an instance of the GSON class (added dependency to gradle file)
        Gson gson = new Gson();

        // Export the object to a JSON string
        // We get it's value by calling gson.toJson()
        String jsonString = gson.toJson(menuData);

        // Logcat output
        Log.i(TAG, "exportToJSON: " + jsonString);

        // Create the file and store the data in External Storage
        // Create a FileOutputStream object and set it to null
        FileOutputStream fileOutputStream = null;

        // Create a file object and create it with the file class' constructor
        File file = new File(
                // Pass the external storage directory
                Environment.getExternalStorageDirectory(),
                // Pass the name of the file which is a field of this class
                FILE_NAME);

        try {
            // Instantiate the FileOutputStream and pass in the file
            fileOutputStream = new FileOutputStream(file);

            // Write to fileOutputStream with the json String contents
            fileOutputStream.write(jsonString.getBytes());

            // Return true since the code worked
            return true;
        } catch (IOException e) {
            // print error
            e.printStackTrace();
        } finally {
            // Close the fileOutputStream
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Return false if the above code fails
        return false;
    }


    public static List<DataItem> importFromJSON(Context context) {
        return null;
    }

    // When you export data in GSON, you'll always end up exporting an object, and
    // that object can't be an instance of a List, an ArrayList, or any other item
    // that includes a generic declaration. So, we're going to take that list of data items
    // and wrap it in a strongly typed object, and declare that here as a member class.
    static class DataItems {

        // Assign a single field, which is a list of data item objects
        List<DataItem> dataItems;

        // Generate getter for the data items
        public List<DataItem> getDataItems() {
            return dataItems;
        }

        // Generate setter for the data items
        public void setDataItems(List<DataItem> dataItems) {
            // Set the data to the field of this class
            this.dataItems = dataItems;
        }
    }


}
