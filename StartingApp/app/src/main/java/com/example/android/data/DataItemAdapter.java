package com.example.android.data;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.data.model.DataItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

// The DataItemAdapter class extends a class named RecyclerView.Adapter
// The generic is a subclass or member class called ViewHolder
public class DataItemAdapter extends RecyclerView.Adapter<DataItemAdapter.ViewHolder> {

    // Key to access data items
    // Public so the entire app can access it
    public static final String ITEM_ID_KEY = "item_id_key";
    public static final String ITEM_KEY = "item_key";

    private List<DataItem> mItems;
    private Context mContext;

    public DataItemAdapter(Context context, List<DataItem> items) {
        // Save data persistently to the fields
        this.mContext = context;
        this.mItems = items;
    }

    // This method is called automatically by the adapter each time it needs a new visual
    // representation of a data item.
    @Override
    public DataItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // When you inflate the xml layout file you get a view
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.list_item, parent, false);

        // Wrap that view in an instance of your ViewHolder class
        ViewHolder viewHolder = new ViewHolder(itemView);

        // return the ViewHolder object
        return viewHolder;
    }

    // This method is called each time the adapter encounters a new data item it needs to display
    // It passes in a reference to the ViewHolder and the position of the data item in the collection

    // This is also where you set up your event handlers
    @Override
    public void onBindViewHolder(DataItemAdapter.ViewHolder holder, int position) {

        // The job of onBindViewHolder is to take that data object and display it's values
        // When I reference views from the xml layout file to display data, I'm getting those from the
        // ViewHolder object
        final DataItem item = mItems.get(position);

        try {
            holder.tvName.setText(item.getItemName());
            String imageFile = item.getImage();
            InputStream inputStream = mContext.getAssets().open(imageFile);
            Drawable d = Drawable.createFromStream(inputStream, null);
            holder.imageView.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Add click listener to the given view
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create Toast message when the item is clicked
//                Toast.makeText(mContext, "You selected " + item.getItemName(), Toast.LENGTH_SHORT).show();

                // Get the Id of the item selected
//                String itemId = item.getItemId();

                // Create intent object, with the context and class I want to navigate to
                Intent intent = new Intent(mContext, DetailActivity.class);

                // Pass extra value; this will be the id that uniquely identifies
                // that data item

                // Instead of passing just the primary key, I'm passing the entire data object
//                intent.putExtra(ITEM_KEY, itemId);
                intent.putExtra(ITEM_KEY, item);

                // Use the context to start the activity passing in the intent
                mContext.startActivity(intent);

                // In this method, I've handled the event and I'm sending the information
                // that's needed to the DetailActivity
            }
        });

        // Add long click listener to the given view
        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {// Create Toast message when the item is clicked
                // Create Toast message when the item is long clicked
                Toast.makeText(mContext, "You long clicked " + item.getItemName(), Toast.LENGTH_SHORT).show();
                // Because this method returns a boolean return value, return false
                return false;
            }
        });
    }

    // Method returns the number of items in the data collection
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    // ViewHolder class is responsible for setting up the bindings to the views
    // in the xml layout file
    public static class ViewHolder extends RecyclerView.ViewHolder {

        // Get references to my visual widgets and saving them as public fields of the
        // ViewHolder class
        public TextView tvName;
        public ImageView imageView;

        // Get a reference to the view that represents a single data item
        // Expose it as a member of the ViewHolder class
        public View mView;

        public ViewHolder(View itemView) {
            super(itemView);

            // Get the views from the xml file
            tvName = (TextView) itemView.findViewById(R.id.itemNameText);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);

            // Set the reference in the constructor method
            // itemView is the reference we're already getting as an argument
            // Now the mView reference will be available to the rest of the adapter
            mView = itemView;
        }
    }
}
