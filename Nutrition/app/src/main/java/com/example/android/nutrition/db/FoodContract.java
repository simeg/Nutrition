package com.example.android.nutrition.db;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the food database.
 */
public class FoodContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.nutrition";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FOOD = "food";

    /* Inner class that defines the table contents of the food table */
    public static final class FoodEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FOOD).build();

        public static final String TABLE_NAME = "food";

        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ENERGY_KJ = "energy_kj";
        public static final String COLUMN_ENERGY_KCAL = "energy_kcal";
        public static final String COLUMN_PROTEIN = "protein";
        public static final String COLUMN_FAT = "fat";
        public static final String COLUMN_CARBOHYDRATES = "carbohydrates";
        public static final String COLUMN_FIBRES = "fibres";
        public static final String COLUMN_SALT = "salt";
        public static final String COLUMN_WATER = "water";
        public static final String COLUMN_ALCOHOL = "alcohol";

        public static Uri buildFoodNumberUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getFoodNumberFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }

    }
}
