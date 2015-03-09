package com.example.android.nutrition.food;

import android.os.Parcel;
import android.os.Parcelable;

public class FoodDetailItem implements Parcelable {

    private long number;
    private String name;
    private int energyKj;
    private int energyKcal;
    private int protein;
    private int fat;
    private int carbohydrates;
    private int fibres;
    private int salt;
    private int water;
    private int alcohol;

    public FoodDetailItem(long number,
                          String name,
                          int energyKj,
                          int energyKcal,
                          int protein,
                          int fat,
                          int carbohydrates,
                          int fibres,
                          int salt,
                          int water,
                          int alcohol) {
        this.number = number;
        this.name = name;
        this.energyKj = energyKj;
        this.energyKcal = energyKcal;
        this.protein = protein;
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.fibres = fibres;
        this.salt = salt;
        this.water = water;
        this.alcohol = alcohol;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEnergyKj() {
        return energyKj;
    }

    public void setEnergyKj(int energyKj) {
        this.energyKj = energyKj;
    }

    public int getEnergyKcal() {
        return energyKcal;
    }

    public void setEnergyKcal(int energyKcal) {
        this.energyKcal = energyKcal;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public int getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(int carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public int getFibres() {
        return fibres;
    }

    public void setFibres(int fibres) {
        this.fibres = fibres;
    }

    public int getSalt() {
        return salt;
    }

    public void setSalt(int salt) {
        this.salt = salt;
    }

    public int getWater() {
        return water;
    }

    public void setWater(int water) {
        this.water = water;
    }

    public int getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(int alcohol) {
        this.alcohol = alcohol;
    }


    protected FoodDetailItem(Parcel in) {
        number = in.readLong();
        name = in.readString();
        energyKj = in.readInt();
        energyKcal = in.readInt();
        protein = in.readInt();
        fat = in.readInt();
        carbohydrates = in.readInt();
        fibres = in.readInt();
        salt = in.readInt();
        water = in.readInt();
        alcohol = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(number);
        dest.writeString(name);
        dest.writeInt(energyKj);
        dest.writeInt(energyKcal);
        dest.writeInt(protein);
        dest.writeInt(fat);
        dest.writeInt(carbohydrates);
        dest.writeInt(fibres);
        dest.writeInt(salt);
        dest.writeInt(water);
        dest.writeInt(alcohol);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FoodDetailItem> CREATOR = new Parcelable.Creator<FoodDetailItem>() {
        @Override
        public FoodDetailItem createFromParcel(Parcel in) {
            return new FoodDetailItem(in);
        }

        @Override
        public FoodDetailItem[] newArray(int size) {
            return new FoodDetailItem[size];
        }
    };

    @Override
    public String toString() {
        return "FoodDetailItem{" +
                "number=" + number +
                ", name='" + name + '\'' +
                ", energyKj=" + energyKj +
                ", energyKcal=" + energyKcal +
                ", protein=" + protein +
                ", fat=" + fat +
                ", carbohydrates=" + carbohydrates +
                ", fibres=" + fibres +
                ", salt=" + salt +
                ", water=" + water +
                ", alcohol=" + alcohol +
                '}';
    }
}