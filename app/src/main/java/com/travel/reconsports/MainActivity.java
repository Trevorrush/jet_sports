package com.travel.reconsports;

import android.app.Activity;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.reconinstruments.os.HUDOS;
import com.reconinstruments.os.connectivity.HUDConnectivityManager;
import com.reconinstruments.os.connectivity.IHUDConnectivity;
import com.reconinstruments.os.connectivity.http.HUDHttpRequest;
import com.reconinstruments.os.connectivity.http.HUDHttpResponse;

public class MainActivity extends Activity implements View.OnClickListener , IHUDConnectivity{

    private static final String TAG = "mainActivity";
    private Button downloadButton;

    private HUDConnectivityManager mHUDConnectivityManager = null;
    private String urlString = "http://scores.nbcsports.msnbc.com/ticker/data/gamesMSNBC.js.asp?json=true&sport=MLB&period=20150718";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  Setup button
        downloadButton = (Button) findViewById(R.id.tempDownloadButton) ;
        downloadButton.setOnClickListener(this);

        //Note: This following line is necessary for HUDConnectivityManager to run properly
        System.load("/system/lib/libreconinstruments_jni.so");

        //Get an instance of HUDConnectivityManager
        mHUDConnectivityManager = (HUDConnectivityManager) HUDOS.getHUDService(HUDOS.HUD_CONNECTIVITY_SERVICE);
        if(mHUDConnectivityManager == null){
            Log.e(TAG, "Failed to get HUDConnectivityManager");
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        //registering the IHUDConnectivity to HUDConnectivityManager
        Log.d(TAG, "START");
        mHUDConnectivityManager.register(this);
    }

    @Override
    public void onStop(){
        //unregistering the IHUDConnectivity from HUDConnectivityManager
        mHUDConnectivityManager.unregister(this);
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (v == downloadButton) {
            Log.d(TAG,"ME!");
            new DownloadFileTask(urlString).execute();
        }
    }

    @Override
    public void onConnectionStateChanged(ConnectionState state) {
        Log.d(TAG, "onConnectionStateChanged : state:" + state);
        switch (state) {
            default:
                Log.e(TAG,"onConnectionStateChanged() with unknown state:" + state);
                break;
        }
    }

    @Override
    public void onNetworkEvent(NetworkEvent networkEvent, boolean hasNetworkAccess) {
        Log.d(TAG, "onNetworkEvent : networkEvent:" + networkEvent + " hasNetworkAccess:" + hasNetworkAccess);
        switch (networkEvent) {
            default:
                Log.e(TAG,"onNetworkEvent() with unknown networkEvent:" + networkEvent);
                break;
        }
    }

    @Override
    public void onDeviceName(String deviceName) {
        Log.d(TAG, "onDeviceName : deviceName:" + deviceName);
    }

    private class DownloadFileTask extends AsyncTask<Void, Void, Boolean> {

        String mUrl;
        String mComment;

        public DownloadFileTask(String url) {
            mUrl = url;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean result = false;
            try {

                //Get Request
                HUDHttpRequest request = new HUDHttpRequest(HUDHttpRequest.RequestMethod.GET, mUrl);
                HUDHttpResponse response = mHUDConnectivityManager.sendWebRequest(request);
                if (response.hasBody()) {
                    mComment = "response bodySize:" + response.getBodyString();
                    Log.d(TAG, mComment);
                    result = true;
                }
            } catch (Exception e) {
                mComment = "failed to download file: " + e.getMessage();
                Log.d(TAG, mComment);
                e.printStackTrace();
                return false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                Log.d(TAG,"Good");
            }else {
                Log.d(TAG,"Bad");
            }
        }
    }
}