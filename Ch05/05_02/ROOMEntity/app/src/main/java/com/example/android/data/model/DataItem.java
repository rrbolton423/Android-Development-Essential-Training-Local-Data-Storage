package com.example.android.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.UUID;

// Define ROOM entity class
// In ROOM, you define a database table using an Entity class.
// An Entity class looks just like a conventional POJO class,
// designed to represent a Data Structure. It has private fields, and public getters
// and setters, but it also has ROOM annotations that are interpreted by the library
// to generate some code in the background

// To mark a class as an Entity, add an annotation called "Entity"
// If your database will have more than one table, create more than one Entity
// Each Entity defines a single table in your SQLite database
@Entity
public class DataItem implements Parcelable {

    // For each private field in the class, add an annotation that describes the
    // equivalent SQLite column definition

    @PrimaryKey() // Annotation for a primary key field
    @NonNull // Annotation so this value cannot be null
    private String itemId;
    @ColumnInfo // Takes whatever the name of the field is, and turns it into the column name
    private String itemName;
    @ColumnInfo // Takes whatever the name of the field is, and turns it into the column name
    private String description;
    @ColumnInfo // Takes whatever the name of the field is, and turns it into the column name
    private String category;
    @ColumnInfo // Takes whatever the name of the field is, and turns it into the column name
    private int sortPosition;
    @ColumnInfo // Takes whatever the name of the field is, and turns it into the column name
    private double price;
    @ColumnInfo // Takes whatever the name of the field is, and turns it into the column name
    private String image;

    public DataItem() {
    }

    public DataItem(String itemId, String itemName, String category, String description, int sortPosition, double price, String image) {

        if (itemId == null) {
            itemId = UUID.randomUUID().toString();
        }

        this.itemId = itemId;
        this.itemName = itemName;
        this.description = description;
        this.category = category;
        this.sortPosition = sortPosition;
        this.price = price;
        this.image = image;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getSortPosition() {
        return sortPosition;
    }

    public void setSortPosition(int sortPosition) {
        this.sortPosition = sortPosition;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "DataItem{" +
                "itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", sortPosition=" + sortPosition +
                ", price=" + price +
                ", image='" + image + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.itemId);
        dest.writeString(this.itemName);
        dest.writeString(this.description);
        dest.writeString(this.category);
        dest.writeInt(this.sortPosition);
        dest.writeDouble(this.price);
        dest.writeString(this.image);
    }

    protected DataItem(Parcel in) {
        this.itemId = in.readString();
        this.itemName = in.readString();
        this.description = in.readString();
        this.category = in.readString();
        this.sortPosition = in.readInt();
        this.price = in.readDouble();
        this.image = in.readString();
    }

    public static final Parcelable.Creator<DataItem> CREATOR = new Parcelable.Creator<DataItem>() {
        @Override
        public DataItem createFromParcel(Parcel source) {
            return new DataItem(source);
        }

        @Override
        public DataItem[] newArray(int size) {
            return new DataItem[size];
        }
    };
}
