package com.example.android.nutrition.activities;

import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.nutrition.R;
import com.example.android.nutrition.db.FoodContract;
import com.example.android.nutrition.food.FoodDetailItem;
import com.example.android.nutrition.food.FoodListItem;
import com.example.android.nutrition.sync.MyResultReceiver;
import com.example.android.nutrition.sync.RESTService;
import com.example.android.nutrition.utils.Utils;

public class FoodDetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Disable menu
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
     * A placeholder fragment for the detailed food view.
     */
    public static class FoodDetailFragment extends Fragment implements MyResultReceiver.Receiver {

        private static final String LOG_TAG = FoodDetailFragment.class.getSimpleName();
        private Context mContext;

        private static final int RUNNING = 100;
        private static final int FINISHED = 101;
        private static final int ERROR = 102;

        private FoodListItem mFoodListItem;
        private FoodDetailItem mFoodDetailItem;
        private MyResultReceiver mReceiver;
        private ProgressDialog mProgress;
        private boolean mIsFavorite;

        private TextView mNameView;
        private TextView mEnergyKj;
        private TextView mEnergyKcal;
        private TextView mProtein;
        private TextView mFat;
        private TextView mCarbohydrates;
        private TextView mFibres;
        private TextView mSalt;
        private TextView mWater;
        private TextView mAlcohol;
        private Button mButton;
        private long mNumber;

        public FoodDetailFragment() {
            setHasOptionsMenu(false);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Set context
            mContext = getActivity();

            // Set correct receiver
            mReceiver = new MyResultReceiver(new Handler());
            mReceiver.setReceiver(this);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_food_detail, container, false);

            // Get all text views
            mNameView = (TextView) rootView.findViewById(R.id.detail_textview_name);
            mEnergyKj = (TextView) rootView.findViewById(R.id.detail_textview_energykj_value);
            mEnergyKcal = (TextView) rootView.findViewById(R.id.detail_textview_energykcal_value);
            mProtein = (TextView) rootView.findViewById(R.id.detail_textview_protein_value);
            mFat = (TextView) rootView.findViewById(R.id.detail_textview_fat_value);
            mCarbohydrates = (TextView) rootView.findViewById(R.id.detail_textview_carbohydrates_value);
            mFibres = (TextView) rootView.findViewById(R.id.detail_textview_fibres_value);
            mSalt = (TextView) rootView.findViewById(R.id.detail_textview_salt_value);
            mWater = (TextView) rootView.findViewById(R.id.detail_textview_water_value);
            mAlcohol = (TextView) rootView.findViewById(R.id.detail_textview_alcohol_value);

            mButton = (Button) rootView.findViewById(R.id.detail_button_favorite);

            mButton.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    // If not already a favorite
                    if(!mIsFavorite) {
                        // Add to favorites

                        ContentValues values = new ContentValues();
                        values.put(FoodContract.FoodEntry.COLUMN_NAME, String.valueOf(mNameView.getText()));
                        values.put(FoodContract.FoodEntry.COLUMN_NUMBER, mNumber);
                        values.put(FoodContract.FoodEntry.COLUMN_ENERGY_KJ, Utils.charSeqToDouble(mEnergyKj.getText()));
                        values.put(FoodContract.FoodEntry.COLUMN_ENERGY_KCAL, Utils.charSeqToDouble(mEnergyKcal.getText()));
                        values.put(FoodContract.FoodEntry.COLUMN_PROTEIN, Utils.charSeqToDouble(mProtein.getText()));
                        values.put(FoodContract.FoodEntry.COLUMN_FAT, Utils.charSeqToDouble(mFat.getText()));
                        values.put(FoodContract.FoodEntry.COLUMN_CARBOHYDRATES, Utils.charSeqToDouble(mCarbohydrates.getText()));
                        values.put(FoodContract.FoodEntry.COLUMN_FIBRES, Utils.charSeqToDouble(mFibres.getText()));
                        values.put(FoodContract.FoodEntry.COLUMN_SALT, Utils.charSeqToDouble(mSalt.getText()));
                        values.put(FoodContract.FoodEntry.COLUMN_WATER, Utils.charSeqToDouble(mWater.getText()));
                        values.put(FoodContract.FoodEntry.COLUMN_ALCOHOL, Utils.charSeqToDouble(mAlcohol.getText()));

                        // Insert to db
                        Uri insertedUri = mContext.getContentResolver().insert(
                                FoodContract.FoodEntry.CONTENT_URI,
                                values
                        );

                        // Get inserted row id
                        long newRowId = ContentUris.parseId(insertedUri);

                        if(newRowId > 0) {
                            // Show toast
                            Toast toast = Toast.makeText(getActivity(), R.string.favorites_saved, Toast.LENGTH_SHORT);
                            toast.show();

                            mIsFavorite = true;
                            setButtonText();

                            // Check if saved to db
                            Log.v(LOG_TAG, "Saved with id: " + String.valueOf(newRowId));
                        } else {
                            // Could not save to db
                            Log.v(LOG_TAG, "Could not save to database");
                        }
                    } else {
                        // Delete from favorites

                        String selection = FoodContract.FoodEntry.COLUMN_NUMBER + " LIKE ?";
                        String[] selectionArgs = { String.valueOf(mNumber) };

                        // Delete from db
                        int deletedRows = mContext.getContentResolver().delete(
                                FoodContract.FoodEntry.CONTENT_URI,
                                selection,
                                selectionArgs
                        );

                        if(deletedRows > 0) {
                            // Show toast
                            Toast toast = Toast.makeText(getActivity(), R.string.favorites_removes, Toast.LENGTH_SHORT);
                            toast.show();

                            mIsFavorite = false;
                            setButtonText();
                        } else {
                            Log.v(LOG_TAG, "Could not delete from favorites");
                        }
                    }
                }
            });

            // The detail Activity called via intent.  Inspect the intent for food data.
            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra("FoodListItem")) {

                // Receive item
                mFoodListItem = intent.getExtras().getParcelable("FoodListItem");

                // Get id from food item
                String query = String.valueOf(mFoodListItem.getItemNumber());

                // Start intent to fetch data
                final Intent queryIntent = new Intent(Intent.ACTION_MAIN, null, getActivity(), RESTService.class);
                queryIntent.putExtra("receiver", mReceiver);
                queryIntent.putExtra("command", "searchItem");
                queryIntent.putExtra("query", query);
                getActivity().startService(queryIntent);

            } else if(intent != null && intent.hasExtra("FoodListItemLocal") && intent.hasExtra("fromFavorite")){
                // If coming from favorite view

                // Coming from favorites - we know this is true
                mIsFavorite = true;
                setButtonText();

                mNumber = intent.getIntExtra("FoodListItemLocal", -1);

                Uri uri = FoodContract.FoodEntry.buildFoodNumberUri(mNumber);

                Cursor cursor = mContext.getContentResolver().query(uri,
                        null,
                        null,
                        null,
                        null);

                if(cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    mNameView.setText(cursor.getString(cursor.getColumnIndex(FoodContract.FoodEntry.COLUMN_NAME)));
                    mEnergyKj.setText(cursor.getString(cursor.getColumnIndex(FoodContract.FoodEntry.COLUMN_ENERGY_KJ)));
                    mEnergyKcal.setText(cursor.getString(cursor.getColumnIndex(FoodContract.FoodEntry.COLUMN_ENERGY_KCAL)));
                    mProtein.setText(cursor.getString(cursor.getColumnIndex(FoodContract.FoodEntry.COLUMN_PROTEIN)));
                    mFat.setText(cursor.getString(cursor.getColumnIndex(FoodContract.FoodEntry.COLUMN_FAT)));
                    mCarbohydrates.setText(cursor.getString(cursor.getColumnIndex(FoodContract.FoodEntry.COLUMN_CARBOHYDRATES)));
                    mFibres.setText(cursor.getString(cursor.getColumnIndex(FoodContract.FoodEntry.COLUMN_FIBRES)));
                    mSalt.setText(cursor.getString(cursor.getColumnIndex(FoodContract.FoodEntry.COLUMN_SALT)));
                    mWater.setText(cursor.getString(cursor.getColumnIndex(FoodContract.FoodEntry.COLUMN_WATER)));
                    mAlcohol.setText(cursor.getString(cursor.getColumnIndex(FoodContract.FoodEntry.COLUMN_ALCOHOL)));

                    cursor.close();
                } else {
                    Log.v(LOG_TAG, "Item not found OR cursor is null");
                }
            }

            return rootView;
        }

        private void checkItemFavorite() {
            Uri uri = FoodContract.FoodEntry.buildFoodNumberUri(mFoodDetailItem.getNumber());

            Cursor c = mContext.getContentResolver().query(uri,
                    null,
                    null,
                    null,
                    null);

            if(c != null && c.getCount() > 0){
                c.moveToFirst();
                mIsFavorite = true;
                c.close();
            } else {
                Log.v(LOG_TAG, "Not a favorite OR cursor is null");
            }
        }

        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {
            switch (resultCode) {
                case RUNNING:

                    // Disable button
                    mButton.setEnabled(false);

                    // Show progress bar
                    mProgress = new ProgressDialog(getActivity());
                    mProgress.setTitle(R.string.progress_bar_title);
                    mProgress.setMessage(getResources().getString(R.string.progress_bar_nutrients));
                    mProgress.show();

                    break;
                case FINISHED:
                    // Receive the items
                    mFoodDetailItem = resultData.getParcelable("foodItem");

                    // Enable button
                    mButton.setEnabled(true);

                    if(mFoodDetailItem != null){
                        setTextViews();
                        checkItemFavorite();
                        setButtonText();
                        mNumber = mFoodDetailItem.getNumber();
                    } else {
                        Log.v(LOG_TAG, "Food item is null");
                    }

                    // Hide progress loading
                    mProgress.dismiss();
                    break;
                case ERROR:
                    // handle the error;
                    break;
            }
        }

        private void setButtonText() {
            int btnText = mIsFavorite ? R.string.favorite_btn_remove : R.string.favorite_btn_add;
            mButton.setText(btnText);
        }

        private void setTextViews(){
            // Set all text views to correct values
            mNameView.setText(mFoodDetailItem.getName());
            mEnergyKj.setText(String.valueOf(mFoodDetailItem.getEnergyKj()));
            mEnergyKcal.setText(String.valueOf(mFoodDetailItem.getEnergyKcal()));
            mProtein.setText(String.valueOf(mFoodDetailItem.getProtein()));
            mFat.setText(String.valueOf(mFoodDetailItem.getFat()));
            mCarbohydrates.setText(String.valueOf(mFoodDetailItem.getCarbohydrates()));
            mFibres.setText(String.valueOf(mFoodDetailItem.getFibres()));
            mSalt.setText(String.valueOf(mFoodDetailItem.getSalt()));
            mWater.setText(String.valueOf(mFoodDetailItem.getWater()));
            mAlcohol.setText(String.valueOf(mFoodDetailItem.getAlcohol()));
        }
    }
}
