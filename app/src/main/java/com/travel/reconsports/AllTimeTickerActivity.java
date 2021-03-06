package com.travel.reconsports;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.reconinstruments.ui.carousel.CarouselActivity;
import com.reconinstruments.ui.carousel.CarouselItem;
import com.reconinstruments.ui.carousel.StandardCarouselItem;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import com.travel.xmlParser.xmlHandler;

public class AllTimeTickerActivity extends CarouselActivity {

    private static final String TAG = "TickerActivity";
    private String jsonResponseKeyString = "JsonResponseKey";
    private String jsonResponseValueString;

    // JSON Node names
    private static final String TAG_SPORT = "sport";
    private static final String TAG_PERIOD = "period";
    private static final String TAG_GAMES = "games";

    // XML Handler
    private xmlHandler mXmlHandler;

    // contacts JSONArray
    JSONArray gameArray = null;

    @Override
    public int getLayoutId() {
        return R.layout.carousel_host;
    }
//
//    static class ImageCarouselItem extends StandardCarouselItem {
//        public ImageCarouselItem(String title, Integer icon) {
//            super(title, icon);
//        }
//        @Override
//        public int getLayoutId() {
//            return R.layout.carousel_item_title_icon_column;
//        }
//    }

    private Carousel_Score_Item[] getGameEntries(){
        int num_of_games = getNumberOfGames();
        Carousel_Score_Item[] myCarouseItemArray = new Carousel_Score_Item[num_of_games];
        for (int i = 0; i < gameArray.length(); i++) {

            //  Get home team title
            try {

                String current_object_string = (String)gameArray.get(i);

                mXmlHandler = null;
                mXmlHandler = new xmlHandler(current_object_string);
                mXmlHandler.fetchXML();


                myCarouseItemArray[i] = new Carousel_Score_Item(mXmlHandler.getHome_team_name_value(), mXmlHandler.getVisiting_team_name_value(), mXmlHandler.getHome_team_logo_value(), mXmlHandler.getVisiting_team_logo_value());

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        return myCarouseItemArray;
    }

    @Override
    public List<? extends CarouselItem> createContents() {
        return Arrays.asList(getGameEntries());
    }

    @Override
    public void onStart() {
        super.onStart();
        //registering the IHUDConnectivity to HUDConnectivityManager
        Log.d(TAG, "START");

    }

    //  Helper methods for JSON parsing
    private int getNumberOfGames(){
        //  Get the json string
        Bundle extras = getIntent().getExtras();
        if(extras !=null && jsonResponseValueString == null) {
            jsonResponseValueString= extras.getString(jsonResponseKeyString);
            Log.d(TAG, jsonResponseValueString);
            Log.d(TAG, "number = " + getNumberOfGames());

        }

        if (jsonResponseValueString != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonResponseValueString);

                // Getting JSON Array node
                gameArray = jsonObj.getJSONArray(TAG_GAMES);

                return gameArray.length();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }

        return 0;
    }

    private String getSportType(){
        return "";
    }

    private String getDate(){
        return "";
    }

}
