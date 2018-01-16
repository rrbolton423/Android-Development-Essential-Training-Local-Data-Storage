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

}
