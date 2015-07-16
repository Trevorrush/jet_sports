package com.travel.reconsports;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.reconinstruments.os.HUDOS;
import com.reconinstruments.os.connectivity.HUDConnectivityManager;
import com.reconinstruments.os.connectivity.IHUDConnectivity;
import com.reconinstruments.os.connectivity.http.HUDHttpRequest;
import com.reconinstruments.os.connectivity.http.HUDHttpResponse;
import com.reconinstruments.ui.dialog.BaseDialog;
import com.reconinstruments.ui.dialog.DialogBuilder;

import com.travel.reconsports.AllTimeTickerActivity;

import java.util.ResourceBundle;


public class MainActivity extends FragmentActivity implements IHUDConnectivity{

    private static final String TAG = "mainActivity";
    private BaseDialog progressDialog;

    private HUDConnectivityManager mHUDConnectivityManager = null;
    private String urlString = "http://scores.nbcsports.msnbc.com/ticker/data/gamesMSNBC.js.asp?json=true&sport=MLB&period=20150717";
    private String jsonResponseKeyString = "JsonResponseKey";
    private String jsonResponseBodyString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Note: This following line is necessary for HUDConnectivityManager to run properly
        System.load("/system/lib/libreconinstruments_jni.so");

        //Get an instance of HUDConnectivityManager
        mHUDConnectivityManager = (HUDConnectivityManager) HUDOS.getHUDService(HUDOS.HUD_CONNECTIVITY_SERVICE);
        if(mHUDConnectivityManager == null){
            Log.e(TAG, "Failed to get HUDConnectivityManager");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //registering the IHUDConnectivity to HUDConnectivityManager
        Log.d(TAG, "START");
        mHUDConnectivityManager.register(this);

        //  Start downloading events
        showNonDismissalProgress();
        new DownloadFileTask(urlString).execute();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop(){
        //unregistering the IHUDConnectivity from HUDConnectivityManager
        mHUDConnectivityManager.unregister(this);
        super.onStop();
    }

    @Override
    public void onConnectionStateChanged(ConnectionState state) {
        if (state != null){
            Log.d(TAG, "onConnectionStateChanged : state:" + state);
            switch (state) {
                default:
                    Log.e(TAG,"onConnectionStateChanged() with unknown state:" + state);
                    break;
            }
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
                    jsonResponseBodyString = response.getBodyString();
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
                //  Update success icon
                ImageView icon = (ImageView)progressDialog.getView().findViewById(R.id.icon);
                icon.setImageResource(R.drawable.icon_checkmark);
                icon.setVisibility(View.VISIBLE);

                //  Update text
                TextView title_TextView = (TextView)progressDialog.getView().findViewById(R.id.title);
                title_TextView.setText(R.string.fetching_success);

                //  Hide subTitle
                progressDialog.getView().findViewById(R.id.subtitle).setVisibility(View.GONE);

                progressDialog.getView().findViewById(R.id.progress_bar).setVisibility(View.GONE);
                progressDialog.dismiss();

                showAllTimeTicker();

            }else {

                progressDialog.dismiss();

                showRetryProgress();

                Log.d(TAG,"Bad");
            }
        }
    }

    //  Show non dismiss download
    private void showNonDismissalProgress(){

        String fetching_string = getResources().getString(R.string.fetching_score);
        String fetching_details_string = getResources().getString(R.string.fetching_score_details);
        //  Show the fetching state
        DialogBuilder progress_builder = new DialogBuilder(this);
        progress_builder.setTitle(fetching_string);
        progress_builder.setSubtitle(fetching_details_string);

        progress_builder.showProgress();

        progressDialog = progress_builder.createDialog();
        progressDialog.show();

    }

    private void showRetryProgress(){

        String fetching_failed_string = getResources().getString(R.string.fetching_failed);
        String fetching_failed_details_string = getResources().getString(R.string.fetching_failed_details);

        //  Show the fetching failed state
        DialogBuilder progress_builder = new DialogBuilder(this);
        progress_builder.setTitle(fetching_failed_string);
        progress_builder.setSubtitle(fetching_failed_details_string);

        progress_builder.setIcon(R.drawable.icon_warning);

        //  Add onclick handler
        progress_builder.setOnKeyListener(new BaseDialog.OnKeyListener() {
            @Override
            public boolean onKey(BaseDialog dialog, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                    dialog.dismiss();
                    showNonDismissalProgress();

                    new DownloadFileTask(urlString).execute();

                    return true;

                }
                return false;
            }
        });

        progressDialog = progress_builder.createDialog();
        progressDialog.show();

    }

    private void showAllTimeTicker(){
        Intent intent = new Intent(this, AllTimeTickerActivity.class);

        //  push the response body string to next activity
        intent.putExtra(jsonResponseKeyString,jsonResponseBodyString);
        startActivity(intent);
    }
}
