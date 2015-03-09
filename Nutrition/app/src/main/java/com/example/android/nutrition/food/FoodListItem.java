package com.example.android.nutrition.food;

import android.os.Parcel;
import android.os.Parcelable;

public class FoodListItem implements Parcelable {

    private String itemName;
    private int itemNumber;

    public FoodListItem(String title, int number){
        this.itemName = title;
        this.itemNumber = number;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemNumber() { return itemNumber; }

    public void setItemNumber(int itemNumber) { this.itemNumber = itemNumber; }

    @Override
    public String toString() {
        return "FoodListItem{" +
                "itemName='" + itemName + '\'' +
                ", itemNumber=" + itemNumber +
                '}';
    }

    protected FoodListItem(Parcel in) {
        itemName = in.readString();
        itemNumber = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemName);
        dest.writeInt(itemNumber);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FoodListItem> CREATOR = new Parcelable.Creator<FoodListItem>() {
        @Override
        public FoodListItem createFromParcel(Parcel in) {
            return new FoodListItem(in);
        }

        @Override
        public FoodListItem[] newArray(int size) {
            return new FoodListItem[size];
        }
    };
}
