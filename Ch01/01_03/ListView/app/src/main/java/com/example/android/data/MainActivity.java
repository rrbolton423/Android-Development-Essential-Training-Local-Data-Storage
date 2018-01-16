package com.example.android.data;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.data.model.DataItem;
import com.example.android.data.sample.SampleDataProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//    TextView tvOut;
    List<DataItem> dataItemList = SampleDataProvider.dataItemList;
    List<String> itemNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        tvOut = (TextView) findViewById(R.id.out);
//        tvOut.setText("");

//        Collections.sort(dataItemList, new Comparator<DataItem>() {
//            @Override
//            public int compare(DataItem o1, DataItem o2) {
//                return o1.getItemName().compareTo(o2.getItemName());
//            }
//        });

        for (DataItem item : dataItemList) {
//            tvOut.append(item.getItemName() + "\n");
            itemNames.add(item.getItemName());
        }
        Collections.sort(itemNames);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, itemNames);

        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(adapter);
    }
}
