package com.example.android.data;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.data.model.DataItem;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.Locale;

@SuppressWarnings("FieldCanBeLocal")
public class DetailActivity extends AppCompatActivity {

    // Declaration of Views as fields
    private TextView tvName, tvDescription, tvPrice;
    private ImageView itemImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // In this method I can get to the intent that launched the activity
        // by calling the method getIntent(). And from the intent, I can get
        // extras, and from there, I can get to any of the extra values that
        // were passed in.
        // Get the unique key and assign it to itemId
//        String itemId = getIntent().getExtras().getString(DataItemAdapter.ITEM_ID_KEY);
//
//        // Use Id to get the selected data item from the data store, (SampleDataProvider)
//        // We will use the dataItemMap that we created in SampleDataProvider
//        // Use the value to get the complex data I need from the persistent data store
//        DataItem item = SampleDataProvider.dataItemMap.get(itemId);


        // Get the parcelable item object that was passed as an extra to this intent
        DataItem item = getIntent().getExtras().getParcelable(DataItemAdapter.ITEM_KEY);
        if (item == null) {
            throw new AssertionError("Null data item received!");
        }

        // Initialize view fields
        tvName = (TextView) findViewById(R.id.tvItemName);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        itemImage = (ImageView) findViewById(R.id.itemImage);

        // Set name and description of the item
        tvName.setText(item.getItemName());
        tvDescription.setText(item.getDescription());

        // Use NumberFormat object to format and present the price
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
        tvPrice.setText(nf.format(item.getPrice()));

        // Retrieve the requested image from the assets directory
        // and display it in the ImageView
        InputStream inputStream = null;
        try {
            String imageFile = item.getImage();
            inputStream = getAssets().open(imageFile);
            Drawable d = Drawable.createFromStream(inputStream, null);
            itemImage.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
