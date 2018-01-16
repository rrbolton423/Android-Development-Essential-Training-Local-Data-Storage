package com.example.android.data;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.example.android.data.model.DataItem;
import com.example.android.data.sample.SampleDataProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Create a reference to the "List" version of the data as a persistence field
    // Get the reference to the list by calling the static field of the SampleDataProvider
    // Now we have all of the data of the list in the MainActivity class
    List<DataItem> dataItemList = SampleDataProvider.dataItemList;

    // Create and instantiate a list of itemNames
    // List containing simple strings
    List<String> itemNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Sort data alphabetically
        Collections.sort(dataItemList,
                // Since we're sorting complex objects rather than simple values,
                // we created an instance of Java's comparator interface
                new Comparator<DataItem>() {
                    @Override
                    public int compare(DataItem o1, DataItem o2) {
                        // automatically returns the appropriate value to control the sorting
                        return o1.getItemName().compareTo(o2.getItemName());
                    }
                });

        // Loop through the data
        // Each time through the loop, we'll work with an instance of the
        // DataItem class that we get from the DataItem List
//        for (DataItem item : dataItemList) {
//                // Append the name of the current data item one line at a time
////            tvOut.append(item.getItemName() + "\n");
//
//                // pass the name of the item to the list of Strings
//                // For each item in the dataItemList, I'll pass the name
//            // of the item to the list of strings
//            itemNames.add(item.getItemName());
//        }

        // Sort Data
//        Collections.sort(itemNames);

        // ArrayAdapter's jon is to cycle through the data, and then as needed,
        // create ViewObjects that represent ListItems, (rows in the list),
        // and then pass the data to those ListItems so they're displayed to the user.
        // This ArrayAdapter will be managing String values
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                // context
//                this,
//                // layout resource id (single data item from the list)
//                // this layout file has a simple textview component
//                android.R.layout.simple_list_item_1,
//                // data objects (list of strings)
//                itemNames
//        );

        // Create an instance of the Adapter
        DataItemAdapter adapter = new DataItemAdapter(this, dataItemList);

        // Bind the adapter to the RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvItems);
        recyclerView.setAdapter(adapter);
    }
}
