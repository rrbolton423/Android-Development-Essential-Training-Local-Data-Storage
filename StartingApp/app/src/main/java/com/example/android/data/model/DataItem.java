package com.example.android.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

/**
 * Created by romellbolton on 1/4/18.
 */

public class DataItem implements Parcelable {
    // model class for each particular data item

    // will be used as a primary key, a unique value that always
    // identifies a particular data item
    private String itemId;
    private String itemName;
    private String description;
    private String category;
    private int sortPosition;
    private double price;
    private String image;

    public DataItem() {
    }

    public DataItem(String itemId, String itemName, String category, String description, int sortPosition, double price, String image) {

        // If I am creating a new object and it does not have a universal unique ID,
        // assign it right now!
        // Now in the Adapter class, I'll always have a unique ID to pass between activities.
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

    // By making DataItem a parcelable object, we can now pass objects that are instances of this class,
    // DataItem, between activities as intent extras.
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
