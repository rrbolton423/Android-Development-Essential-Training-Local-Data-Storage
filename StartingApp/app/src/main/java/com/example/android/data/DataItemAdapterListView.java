package com.example.android.data;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.data.model.DataItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by romellbolton on 1/5/18.
 */

// This ArrayAdapter Class will manage data item objects,
// rather than simple Strings
public class DataItemAdapterListView extends ArrayAdapter<DataItem> {

    // Persistent reference to the data we're working with
    List<DataItem> mDataItems;

    // Define LayoutInflater object to open and read into memory the xml layout file
    LayoutInflater mInflater;

    public DataItemAdapterListView(@NonNull Context context, List<DataItem> objects) {
        super(
                // context
                context,
                // Pass in custom layout we created ot the super class' constructor method
                R.layout.list_item,
                // list of data objects
                objects);

        // Create persistent reference to the data we're working with
        mDataItems = objects;

        // Create persistent reference to the layout inflater object
        mInflater = LayoutInflater.from(context);

    }

    // Each time the ArrayAdapter encounters a new data item and wants to display it,
    // it's going to look for the getView() method. To customize our display,
    // we must override this method.
    @NonNull
    @Override
    public View getView(
            // the position of the current data item from the data set(mDataItems)
            int position,
            // ConvertView is a reference to a layout.
            @Nullable View convertView,
            // The parent layout
            @NonNull ViewGroup parent) {

        // ConvertView might or might not be null.
        // If the adapter is recycling a view for a list row, then it wont be null. I'll actyally get a layout.
        // But if it's the first time then it'll be null.
        // I have to make sure that it's not null, and if it is null, provide a view
        // If the convertView is null...
        if (convertView == null) {

            // Instantiate the convertView
            // Inflate the need view, insuring that the convertVire it is not null
            convertView = mInflater.inflate(
                    // custom view to inflate
                    R.layout.list_item,
                    // parent layout
                    parent,
                    // attach to root
                    false);
        }

        // By the time the code gets down here, I will always have a reference to a
        // non-null view that represents the row.

        // Get references to the different view objects in the layout file using the convertView
        TextView tvName = (TextView) convertView.findViewById(R.id.itemNameText);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);

        // Get the data item I want to work with
        // I know the position of the data item in the list of data from the 'position' argument
        DataItem item = mDataItems.get(position);

        // Display the item name and image.
        tvName.setText(item.getItemName());
//        imageView.setImageResource(R.drawable.apple_pie);

        // Load image dynamically

        // Define and instantiate an InputStream object
        InputStream inputStream = null;

        try {

            // Get the name of the image file
            String imageFile = item.getImage();

            // Create an input stream using the imageFile
            inputStream = getContext().getAssets().open(imageFile);

            // Create a drawable object and instantiate it by loading the file
            Drawable d = Drawable.createFromStream(inputStream, null);

            // Load the drawable object into the imageView
            imageView.setImageDrawable(d);

        } catch (IOException e) {

            e.printStackTrace(); // log exception

        } finally {

            try {

                // If the inputStream is null...
                if (inputStream != null) {

                    // close stream
                    inputStream.close();
                }

            } catch (IOException e) {

                e.printStackTrace(); // log exception
            }
        }

        // Return the view
        return convertView;
    }
}
