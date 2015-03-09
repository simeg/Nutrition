package com.example.android.nutrition.sync;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.example.android.nutrition.R;
import com.example.android.nutrition.food.FoodDetailItem;
import com.example.android.nutrition.food.FoodListItem;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RESTService extends IntentService {

    private final String LOG_TAG = RESTService.class.getSimpleName();
    private String BASE_URL;

    private static final int STATUS_RUNNING = 100;
    private static final int STATUS_FINISHED = 101;
    private static final int STATUS_ERROR = 102;

    private DefaultHttpClient httpClient;

    public RESTService() {
        super(RESTService.class.getSimpleName());
    }

    @Override
    public void onCreate(){
        super.onCreate();
        BASE_URL = getResources().getString(R.string.api_url);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        String command = intent.getStringExtra("command");
        Bundle b = new Bundle();

        httpClient = new DefaultHttpClient(new BasicHttpParams());

        if(command != null && command.equals("searchAll")) {
            String query = intent.getStringExtra("query");

            // Tell FoodFragment that we're making request
            b.putString("query", query);
            receiver.send(STATUS_RUNNING, b);


            HttpGet httpGet = new HttpGet(BASE_URL + getResources().getString(R.string.api_url_search) + query);

            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();

                // Put Food Items in bundle
                b.putParcelableArrayList("foodItems", convertToFoodListItems(EntityUtils.toString(httpEntity)));
                receiver.send(STATUS_FINISHED, b);

            }  catch(Exception e) {
                b.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(STATUS_ERROR, b);
            }
        } else if(command != null && command.equals("searchItem")){
            // Tell FoodFragment that we're making request
            receiver.send(STATUS_RUNNING, Bundle.EMPTY);

            String query = intent.getStringExtra("query");
            HttpGet httpGet = new HttpGet(BASE_URL + getResources().getString(R.string.api_url_search_item) + query);

            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();

                // Put Food Item in bundle
                b.putParcelable("foodItem", convertToFoodDetailItem(EntityUtils.toString(httpEntity)));
                receiver.send(STATUS_FINISHED, b);

            }  catch(Exception e) {
                b.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(STATUS_ERROR, b);
            }
        }

    }

    private ArrayList<FoodListItem> convertToFoodListItems(String jsonStr) {
        ArrayList<FoodListItem> itemList = new ArrayList<>();
        FoodListItem item;
        if (jsonStr != null) {
            try {
                JSONArray items = new JSONArray(jsonStr);

                for (int i = 0; i < items.length(); i++) {
                    JSONObject c = items.getJSONObject(i);

                    // Get the information
                    String id = c.getString("number");
                    String name = c.getString("name");

                    // Create new food item list
                    item = new FoodListItem(name, Integer.valueOf(id));

                    // Add item to arraylist
                    itemList.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return itemList;
    }

    private FoodDetailItem convertToFoodDetailItem(String jsonStr) {
        FoodDetailItem foodItem = null;
        if (jsonStr != null) {
            try {
                JSONObject item = new JSONObject(jsonStr);
                JSONObject nutrientValues = item.getJSONObject("nutrientValues");

                // Get the information
                long id = item.getLong("number");
                String name = item.getString("name");
                int energyKj = nutrientValues.getInt("energyKj");
                int energyKcal = nutrientValues.getInt("energyKcal");
                int protein = nutrientValues.getInt("protein");
                int fat = nutrientValues.getInt("fat");
                int carbohydrates = nutrientValues.getInt("carbohydrates");
                int fibres = nutrientValues.getInt("fibres");
                int salt = nutrientValues.getInt("salt");
                int water = nutrientValues.getInt("water");
                int alcohol = nutrientValues.getInt("alcohol");

                // Create new food item
                foodItem = new FoodDetailItem(
                        id,
                        name,
                        energyKj,
                        energyKcal,
                        protein,
                        fat,
                        carbohydrates,
                        fibres,
                        salt,
                        water,
                        alcohol);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return foodItem;
    }


}