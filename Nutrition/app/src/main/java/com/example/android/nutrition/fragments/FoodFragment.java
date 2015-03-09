package com.example.android.nutrition.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.nutrition.R;
import com.example.android.nutrition.activities.FoodDetailActivity;
import com.example.android.nutrition.food.FoodListItem;
import com.example.android.nutrition.sync.MyResultReceiver;
import com.example.android.nutrition.sync.RESTService;

import java.util.ArrayList;

public class FoodFragment extends Fragment implements MyResultReceiver.Receiver {

    private final String LOG_TAG = FoodFragment.class.getSimpleName();

    private Callback mListener;
    private ListView mListView;
    private Button mSearchButton;
    private ArrayAdapter<String> mStringArrayAdapter;
    private ArrayList<FoodListItem> mFoodListAdapter;
    private MyResultReceiver mReceiver;
    private TextView mSearchTextView;
    private ProgressDialog mProgress;

    private static final int RUNNING = 100;
    private static final int FINISHED = 101;
    private static final int ERROR = 102;

    public FoodFragment() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();    //Call the back button's method
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set correct receiver
        mReceiver = new MyResultReceiver(new Handler());
        mReceiver.setReceiver(this);

        // Enables options menu
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Adds the top fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        mListView = (ListView) view.findViewById(R.id.listview_suggestions);

        // Initiate adapter and set it
        mStringArrayAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_food, // The name of the layout ID.
                        R.id.list_item_food_textview, // The ID of the textview to populate.
                        new ArrayList<String>());
        mListView.setAdapter(mStringArrayAdapter);

        // On click open FoodDetailFragment
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String foodName = mStringArrayAdapter.getItem(position);
                FoodListItem item = null;

                // Get corresponding food item
                for(FoodListItem foodItem : mFoodListAdapter){
                    if(foodItem.getItemName().equals(foodName)){
                        item = foodItem;
                    }
                }

                // Start Detailed View
                Intent intent = new Intent(getActivity(), FoodDetailActivity.class)
                        .putExtra("FoodListItem", item);
                startActivity(intent);
            }
        });

        mSearchButton = (Button) view.findViewById(R.id.detail_search_button);

        mSearchTextView = (TextView) view.findViewById(R.id.detail_search);

        mSearchButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                // Hide the keyboard
                hideKeyboard();

                // Get search query
                String query = String.valueOf(mSearchTextView.getText());

                // Clear search box
                mSearchTextView.setText("");

                // Start intent to fetch data
                final Intent intent = new Intent(Intent.ACTION_MAIN, null, getActivity(), RESTService.class);
                intent.putExtra("receiver", mReceiver);
                intent.putExtra("command", "searchAll");
                intent.putExtra("query", query);
                getActivity().startService(intent);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (Callback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case RUNNING:
                // Get search query string
                String query = resultData.getString("query");

                // Show progress bar
                mProgress = new ProgressDialog(getActivity());
                mProgress.setTitle(R.string.progress_bar_title);
                mProgress.setMessage(getResources().getString(R.string.progress_bar_searching) + " " + query
                        + getResources().getString(R.string.progress_bar_dots));
                mProgress.show();

                break;
            case FINISHED:
                // Receive the items
                mFoodListAdapter = resultData.getParcelableArrayList("foodItems");

                if(!mFoodListAdapter.isEmpty()) {
                    // Clear the list view
                    mStringArrayAdapter.clear();

                    // Add item names to the adapter
                    for (FoodListItem item : mFoodListAdapter) {
                        mStringArrayAdapter.add(item.getItemName());
                    }

                    // Hide progress loading
                    mProgress.dismiss();
                } else {
                    // Clear list
                    mStringArrayAdapter.clear();

                    // Hide progress loading
                    mProgress.dismiss();

                    // Show toast
                    Toast toast = Toast.makeText(getActivity(), R.string.fragment_main_no_results, Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
            case ERROR:
                // handle the error;
                break;
        }
    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSearchTextView.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public interface Callback {
        public void onItemSelect(String id);
    }

}
