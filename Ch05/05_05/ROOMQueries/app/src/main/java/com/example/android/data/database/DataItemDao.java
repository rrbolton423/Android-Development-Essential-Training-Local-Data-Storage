package com.example.android.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.data.model.DataItem;

import java.util.List;

@Dao
public interface DataItemDao {

    @Insert
    void insertAll(List<DataItem> items);

    @Insert
    void insertAll(DataItem... items);

    @Query("SELECT COUNT(*) from dataitem")
    int countItems();

    // Will select all data from the DataItem table
    // Define method name that will return a list of DataItem objects
    @Query("SELECT * FROM dataitem ORDER BY itemName")
    List<DataItem> getAll();

    // This query will return a single DataItem object
    // This query will have a filter with a WHERE clause,
    // filtering by the itemId column. We pass a SQL parameter
    // by putting a colon in front of the parameter.
    // In the method declaration, we receive a itemId of type
    // String. The itemId we get must exactly match the parameter in the
    // SQL query. The Room library will put it all together on the back end.
    // It will take the value that's passed in, and replace it in the SQl
    // statement and return a single object.
    @Query("SELECT * FROM dataitem WHERE itemId = :itemId")
    DataItem findById(String itemId);

}
