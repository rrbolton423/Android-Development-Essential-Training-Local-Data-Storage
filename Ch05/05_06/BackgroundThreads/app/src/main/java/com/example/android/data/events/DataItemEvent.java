package com.example.android.data.events;

import com.example.android.data.model.DataItem;

// Simple POJO class that wraps a single instance of the DataItem class.
// This is the kind of packaging you need for EventBus.
// You need POJO classes that are simple wrappers around the data you
// want to move around the application.

public class DataItemEvent {
    private DataItem dataItem;

    public DataItemEvent(DataItem dataItem) {
        this.dataItem = dataItem;
    }

    public DataItem getDataItem() {
        return dataItem;
    }

    public void setDataItem(DataItem dataItem) {
        this.dataItem = dataItem;
    }
}
