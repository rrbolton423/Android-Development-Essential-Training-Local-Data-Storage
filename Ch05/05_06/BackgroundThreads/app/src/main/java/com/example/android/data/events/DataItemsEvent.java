package com.example.android.data.events;

import com.example.android.data.model.DataItem;

import java.util.List;

// Simple POJO class that wraps a list of DataItems.
// This is the kind of packaging you need for EventBus.
// You need POJO classes that are simple wrappers around the data you
// want to move around the application.
public class DataItemsEvent {

    private List<DataItem> itemList;

    public DataItemsEvent(List<DataItem> itemList) {
        this.itemList = itemList;
    }

    public List<DataItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<DataItem> itemList) {
        this.itemList = itemList;
    }
}
