package com.example.android.nutrition.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Manages a local database for food data.
 */
public class FoodDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "food.db";

    public FoodDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FOOD_TABLE = "CREATE TABLE Food (" +
                FoodContract.FoodEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FoodContract.FoodEntry.COLUMN_NAME + " TEXT UNIQUE NOT NULL, " +
                FoodContract.FoodEntry.COLUMN_NUMBER + " INTEGER UNIQUE NOT NULL, " +
                FoodContract.FoodEntry.COLUMN_ENERGY_KJ + " REAL NOT NULL, " +
                FoodContract.FoodEntry.COLUMN_ENERGY_KCAL + " REAL NOT NULL, " +
                FoodContract.FoodEntry.COLUMN_PROTEIN + " REAL NOT NULL, " +
                FoodContract.FoodEntry.COLUMN_FAT + " REAL NOT NULL, " +
                FoodContract.FoodEntry.COLUMN_CARBOHYDRATES + " REAL NOT NULL, " +
                FoodContract.FoodEntry.COLUMN_FIBRES + " REAL NOT NULL, " +
                FoodContract.FoodEntry.COLUMN_SALT + " REAL NOT NULL, " +
                FoodContract.FoodEntry.COLUMN_WATER + " REAL NOT NULL, " +
                FoodContract.FoodEntry.COLUMN_ALCOHOL + " REAL NOT NULL" +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_FOOD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FoodContract.FoodEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}