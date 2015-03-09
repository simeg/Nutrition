package com.example.android.nutrition.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.nutrition.R;
import com.example.android.nutrition.db.FoodContract;
import com.example.android.nutrition.db.FoodDbHelper;
import com.example.android.nutrition.food.FoodListAdapter;

public class FavoritesActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();    //Call the back button's method
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment for the FavoritesFragment.
     */
    public static class FavoritesFragment extends Fragment {

        private ListView mListView;
        private FoodListAdapter mFoodListAdapter;
        private FoodDbHelper mFoodDbHelper;
        private SQLiteDatabase mDb;

        private static final String[] FOOD_COLUMNS = {
                FoodContract.FoodEntry.TABLE_NAME + "." + FoodContract.FoodEntry._ID,
                FoodContract.FoodEntry.COLUMN_NAME,
                FoodContract.FoodEntry.COLUMN_NUMBER,
                FoodContract.FoodEntry.COLUMN_ENERGY_KJ,
                FoodContract.FoodEntry.COLUMN_ENERGY_KCAL,
                FoodContract.FoodEntry.COLUMN_PROTEIN,
                FoodContract.FoodEntry.COLUMN_FAT,
                FoodContract.FoodEntry.COLUMN_CARBOHYDRATES,
                FoodContract.FoodEntry.COLUMN_FIBRES,
                FoodContract.FoodEntry.COLUMN_SALT,
                FoodContract.FoodEntry.COLUMN_WATER,
                FoodContract.FoodEntry.COLUMN_ALCOHOL
        };

        public static final int COL_FOOD_ID = 0;
        public static final int COL_FOOD_NAME = 1;
        public static final long COL_FOOD_NUMBER = 2;
        public static final double COL_FOOD_ENERGY_KJ = 3;
        public static final double COL_FOOD_ENERGY_KCAL = 4;
        public static final double COL_FOOD_PROTEIN = 5;
        public static final double COL_FOOD_FAT = 6;
        public static final double COL_FOOD_CARBOHYDRATES = 7;
        public static final double COL_FOOD_FIBRES = 8;
        public static final double COL_FOOD_SALT = 9;
        public static final double COL_FOOD_WATER = 10;
        public static final double COL_FOOD_ALCOHOL = 11;


        public FavoritesFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Init db helper
            mFoodDbHelper = new FoodDbHelper(getActivity().getApplicationContext());

            // Init database to read mode
            mDb = mFoodDbHelper.getReadableDatabase();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);

            // Get correct list view
            mListView = (ListView) rootView.findViewById(R.id.favorites_listview);

            // Fetch favorites from db
            Cursor c = mDb.rawQuery("SELECT * FROM " + FoodContract.FoodEntry.TABLE_NAME, null);
            mFoodListAdapter = new FoodListAdapter(getActivity(), c, 0);

            // Set adapter
            mListView.setAdapter(mFoodListAdapter);

            // On click open FoodDetailFragment
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);

                    // Start Detailed View
                    Intent intent = new Intent(getActivity(), FoodDetailActivity.class)
                            .putExtra("FoodListItemLocal", cursor.getInt((int) COL_FOOD_NUMBER));
                    intent.putExtra("fromFavorite", true);
                    startActivity(intent);
                }
            });

            return rootView;
        }
    }
}
