package com.example.android.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.data.model.DataItem;

import java.util.List;

public class DataItemAdapter extends ArrayAdapter<DataItem> {

    List<DataItem> mDataItems;
    LayoutInflater mInflater;

    public DataItemAdapter(Context context, List<DataItem> objects) {
        super(context, R.layout.list_item, objects);

        mDataItems = objects;
        mInflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.itemNameText);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);

        DataItem item = mDataItems.get(position);

        tvName.setText(item.getItemName());
        imageView.setImageResource(R.drawable.apple_pie);

        return convertView;
    }
}
