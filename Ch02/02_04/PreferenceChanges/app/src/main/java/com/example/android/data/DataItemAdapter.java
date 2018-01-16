package com.example.android.data;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class DataItemAdapter extends RecyclerView.Adapter<DataItemAdapter.ViewHolder> {

    public static final String ITEM_ID_KEY = "item_id_key";
    public static final String ITEM_KEY = "item_key";
    private List<DataItem> mItems;
    private Context mContext;

    // Listener for changes in the SharedPrefs set
    private SharedPreferences.OnSharedPreferenceChangeListener prefsListener;

    public DataItemAdapter(Context context, List<DataItem> items) {
        this.mContext = context;
        this.mItems = items;
    }

    @Override
    public DataItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Determine whether or not the user wants to see a grid ...

        // Before we inflate the RecyclerView, I am going to create an instance
        // of the SharedPreferences class, and get its reference.
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext);

        // After you've created the settings variable ...
        // Create the listener object
        prefsListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                // In this method, I'll get a SharedPreference reference,
                // and I'll get a string based key. That will be the key to whatever
                // preference changed 

                // Log the key that changed
                Log.i("preferences", "onSharedPreferenceChanged: " + key);
            }
        };

        // Now we need to register this listener with the appropriate preferences set.
        settings.registerOnSharedPreferenceChangeListener(prefsListener);
        // Now anytime any values changes in that preferences set, I'll be notified

        // Find out whether the user wants to display things in a grid.
        // That's a boolean value, a check box in the settings screen.
        // Get the resource id of the checkBox from the layout (prefs_display_grid),
        // and provide a default value to be returned if this preference isn't found (false)
        boolean grid = settings.getBoolean(mContext.getString(R.string.pref_display_grid), false);

        // Determine which layout file to use
        int layoutId = grid ? R.layout.grid_item : R.layout.list_item;

        // Inflate the specified layout from SharedPreferences
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DataItemAdapter.ViewHolder holder, int position) {
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

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "You selected " + item.getItemName(),
//                        Toast.LENGTH_SHORT).show();
//                String itemId = item.getItemId();
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(ITEM_KEY, item);
                mContext.startActivity(intent);
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mContext, "You long clicked " + item.getItemName(),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public ImageView imageView;
        public View mView;
        public ViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.itemNameText);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            mView = itemView;
        }
    }
}