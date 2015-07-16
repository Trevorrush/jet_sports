package com.travel.reconsports;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.reconinstruments.ui.UIUtils;
import com.reconinstruments.ui.carousel.CarouselItem;

import com.reconinstruments.os.HUDOS;
import com.reconinstruments.os.connectivity.HUDConnectivityManager;
import com.reconinstruments.os.connectivity.IHUDConnectivity;
import com.reconinstruments.os.connectivity.http.HUDHttpRequest;
import com.reconinstruments.os.connectivity.http.HUDHttpResponse;

/**
 * Created by raymondchen on 15-07-16.
 */
public class Carousel_Score_Item extends CarouselItem {

    String homeTeamTitle;
    String awayTeamTitle;
    String homeTeamLogo;
    String awayTeamLogo;

    private HUDConnectivityManager mHUDConnectivityManager = null;

    public Carousel_Score_Item(String homeTeamTitle, String awayTeamTitle, String homeTeamLogo, String awayTeamLogo) {
        this.homeTeamTitle = homeTeamTitle;
        this.awayTeamTitle = awayTeamTitle;
        this.homeTeamLogo = homeTeamLogo;
        this.awayTeamLogo = awayTeamLogo;

        //Get an instance of HUDConnectivityManager
        mHUDConnectivityManager = (HUDConnectivityManager) HUDOS.getHUDService(HUDOS.HUD_CONNECTIVITY_SERVICE);

        new DownloadFileTask(homeTeamLogo).execute();
        new DownloadFileTask(awayTeamLogo).execute();
    }

    /**
     * @return Id for layout containing one or both of a TextView with id R.id.title and an ImageView R.id.icon
     */
    public int getLayoutId() {
        return R.layout.carousel_game_score_column;
    }

    public void updateView(View view) {
        UIUtils.setOptionalText(homeTeamTitle, view, R.id.homeTeamNameTextView);
        UIUtils.setOptionalText(awayTeamTitle, view, R.id.awayTeamNameTextView);
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

                    result = true;

                }

            } catch (Exception e) {
                mComment = "failed to download file: " + e.getMessage();
                e.printStackTrace();
                return false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){

            }else {

            }
        }
    }
}
