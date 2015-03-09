package com.example.android.nutrition.db;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class FoodProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FoodDbHelper mOpenHelper;

    static final int FOOD_GENERAL = 100;
    static final int FOOD_NUMBER = 101;

    private Cursor getFoodByNumber(Uri uri){
        String foodNumber = FoodContract.FoodEntry.getFoodNumberFromUri(uri);

        return mOpenHelper.getReadableDatabase().rawQuery(
                "SELECT * FROM " + FoodContract.FoodEntry.TABLE_NAME + " WHERE "
                        + FoodContract.FoodEntry.COLUMN_NUMBER + " = '" + foodNumber + "'",
                null);
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FoodContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, FoodContract.PATH_FOOD, FOOD_GENERAL);
        matcher.addURI(authority, FoodContract.PATH_FOOD + "/#", FOOD_NUMBER);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new FoodDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case FOOD_GENERAL: {
                Log.v("FOOD", "FOOD GENERAL");
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FoodContract.FoodEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            } case FOOD_NUMBER: {
                Log.v("FOOD", "FOOD NUMBER");
                retCursor = getFoodByNumber(uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;

        long _id = db.insert(FoodContract.FoodEntry.TABLE_NAME, null, values);
        if ( _id > 0 )
            returnUri = FoodContract.FoodEntry.buildFoodNumberUri(_id);
        else
            throw new android.database.SQLException("Failed to insert row into " + uri);

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsDeleted;

        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";

        rowsDeleted = db.delete(
                FoodContract.FoodEntry.TABLE_NAME, selection, selectionArgs);

        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }
}
