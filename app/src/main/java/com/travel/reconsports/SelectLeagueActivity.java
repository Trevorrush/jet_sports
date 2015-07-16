package com.travel.reconsports;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.reconinstruments.ui.carousel.CarouselActivity;
import com.reconinstruments.ui.carousel.CarouselItem;
import com.reconinstruments.ui.carousel.StandardCarouselItem;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import com.reconinstruments.ui.dialog.BaseDialog;
import com.travel.xmlParser.xmlHandler;
/**
 * Created by trevorrush on 15-07-16.
 */
public class SelectLeagueActivity extends CarouselActivity  {
    private static final String TAG = "LeagueActivity";
    private String jsonResponseKeyString = "JsonResponseKey";
    private String jsonResponseValueString;

    // JSON Node names
    private static final String TAG_SPORT = "sport";
    private static final String TAG_PERIOD = "period";
    private static final String TAG_GAMES = "games";

    // XML Handler
    private xmlHandler mXmlHandler;

    // contacts JSONArray
    JSONArray leagueArray = null;

    @Override
    public int getLayoutId() {
        return R.layout.carousel_host;
    }

    static class ImageCarouselItem extends StandardCarouselItem {
        public ImageCarouselItem(String title, Integer icon) {
            super(title, icon);
        }
        @Override
        public int getLayoutId() {
            return R.layout.carousel_item_title_icon_column;
        }
        public void onClick(Context context) {
            context.startActivity(new Intent(context, MainActivity.class));
        }
    }

    private CarouselItem[] getLeagueEntries(){
        int num_of_leagues = getNumberOfLeagues();
        Log.d(TAG,"n = " + num_of_leagues);
        CarouselItem[] myCarouseItemArray = new CarouselItem[num_of_leagues];
        for (int i = 0; i < num_of_leagues; i++) {
            String league_alias = null;
            try {
                league_alias = leagueArray.getJSONObject(i).getString("leagueAlias");


            } catch (JSONException e) {
                e.printStackTrace();
            }

            myCarouseItemArray[i] = new ImageCarouselItem(league_alias, getImageFromType(league_alias));
        }
        return myCarouseItemArray;
    }

    public int getImageFromType(String targetType){
        Log.d(TAG,targetType);
        if (targetType.equals("NHL")){
            return R.drawable.nhl;
        }
        else if (targetType.equals("NFL")){
            return R.drawable.nfl;
        }
        else if (targetType.equals("NBA")){
            return R.drawable.nba;
        }
        else if (targetType.equals("MLB")){
            return R.drawable.mlb;
        }
        return R.drawable.mlb;
    }

    @Override
    public List<? extends CarouselItem> createContents() {
        return Arrays.asList(getLeagueEntries());
    }

    @Override
    public void onStart() {
        super.onStart();
        //registering the IHUDConnectivity to HUDConnectivityManager
        Log.d(TAG, "START");

    }

    //  Helper methods for JSON parsing
    private int getNumberOfLeagues(){
        int league_list_length = 0;
        try {
            String object_string = loadLeagueJSONFromAsset();
            Log.d(TAG,object_string);
            JSONObject json = new JSONObject(object_string);
            leagueArray = json.getJSONArray("leagues");
            JSONArray league_list = json.getJSONArray("leagues");
            System.out.println("*****JARRAY*****" + league_list.length());
            league_list_length = league_list.length();


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return league_list_length;
    }

    public String loadLeagueJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getResources().openRawResource(R.raw.sport_types);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private String getSportType(){
        return "";
    }

    private String getDate(){
        return "";
    }
}
