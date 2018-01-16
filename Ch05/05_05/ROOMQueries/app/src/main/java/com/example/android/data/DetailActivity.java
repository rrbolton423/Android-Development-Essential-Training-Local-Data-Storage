package com.example.android.data;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.data.database.AppDatabase;
import com.example.android.data.model.DataItem;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.Locale;

@SuppressWarnings("FieldCanBeLocal")
public class DetailActivity extends AppCompatActivity {

    private TextView tvName, tvDescription, tvPrice;
    private ImageView itemImage;

    // In DetailActivity I need access to my database
    // So just like in the MainActivty, I'll declare the field
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Initialize Database by getting it's instance
        db = AppDatabase.getInstance(this);

        // Get the Id passed in as an Extra to this Intent
        String itemId = getIntent().getStringExtra(DataItemAdapter.ITEM_ID_KEY);

        // Then I'll retrieve the DataItem object from the Database
        // Now I have the entire DataItem object, but I'm retrieving it from
        // persistent storage instead or receiving it from the MainActivity.
        DataItem item = db.dataItemDao().findById(itemId);

        tvName = (TextView) findViewById(R.id.tvItemName);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        itemImage = (ImageView) findViewById(R.id.itemImage);

        tvName.setText(item.getItemName());
        tvDescription.setText(item.getDescription());

        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
        tvPrice.setText(nf.format(item.getPrice()));

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

    // As DetailActivity closes down, I'll clean up by adding an onDestroy()
    // lifecycle method, and I'll call "AppDatabase.destroyInstance();"
    @Override
    protected void onDestroy() {
        AppDatabase.destroyInstance();
        super.onDestroy();
    }
}