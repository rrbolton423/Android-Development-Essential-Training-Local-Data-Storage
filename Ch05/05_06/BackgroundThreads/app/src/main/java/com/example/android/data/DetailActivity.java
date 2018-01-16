package com.example.android.data;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.data.database.AppDatabase;
import com.example.android.data.events.DataItemEvent;
import com.example.android.data.model.DataItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@SuppressWarnings("FieldCanBeLocal")
public class DetailActivity extends AppCompatActivity {

//    Move the database operation to a background thread using an
//    executor object. After you get the data from the
//    Database, dispatch an event and handle it
//    Using Event Bus.

    private TextView tvName, tvDescription, tvPrice;
    private ImageView itemImage;

    private AppDatabase db;

    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        db = AppDatabase.getInstance(this);

        // Execute database operation on background thread
        executor.execute(new Runnable() {
            @Override
            public void run() {

                // Get the ID of the item from the intent
                final String itemId = getIntent().getStringExtra(DataItemAdapter.ITEM_ID_KEY);

                // Get data item from the database
                DataItem item = db.dataItemDao().findById(itemId);

                // Post event to EventBus so we can move this data item from the
                // background thread to the main thread for use within the class
                EventBus.getDefault().post(new DataItemEvent(item));
            }
        });

        tvName = (TextView) findViewById(R.id.tvItemName);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        itemImage = (ImageView) findViewById(R.id.itemImage);

        // Register EventBus so we can get Events that were posted to it
        EventBus.getDefault().register(this);

    }

    // Subscribe to the EventBus event,
    // And specify the event to be received on the Main Thread
    // Will receive a DataItem from the DataItemEvent POJO class
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void dataItemEventHandler(DataItemEvent event) {

        // Receive the DataItem from the event
        DataItem item = event.getDataItem();

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

    @Override
    protected void onDestroy() {
        AppDatabase.destroyInstance();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}