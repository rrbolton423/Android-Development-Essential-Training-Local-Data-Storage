package com.example.android.data.events;

import com.example.android.data.model.DataItem;

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
