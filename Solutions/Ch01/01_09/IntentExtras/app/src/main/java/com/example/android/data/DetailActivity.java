package com.example.android.data;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.android.data.model.DataItem;
import com.example.android.data.sample.SampleDataProvider;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String itemId = getIntent().getExtras().getString(DataItemAdapter.ITEM_ID_KEY);
        DataItem item = SampleDataProvider.dataItemMap.get(itemId);
        Toast.makeText(this, "Received item " + item.getItemName(),
                Toast.LENGTH_SHORT).show();
    }
}
