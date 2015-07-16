package com.travel.reconsports;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.reconinstruments.ui.UIUtils;
import com.reconinstruments.ui.carousel.CarouselItem;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.reconinstruments.os.HUDOS;
import com.reconinstruments.os.connectivity.HUDConnectivityManager;
import com.reconinstruments.os.connectivity.IHUDConnectivity;
import com.reconinstruments.os.connectivity.http.HUDHttpRequest;
import com.reconinstruments.os.connectivity.http.HUDHttpResponse;
import com.reconinstruments.os.connectivity.http.HUDHttpRequest.RequestMethod;


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

        ImageView homeLogo = (ImageView)view.findViewById(R.id.homeTeamLogoImageView);
        new DownloadHUDImageTask(homeLogo).execute(homeTeamLogo);

        ImageView awayaLogo = (ImageView)view.findViewById(R.id.awayTeamLogoImageView);
        new DownloadHUDImageTask(awayaLogo).execute(awayTeamLogo);
    }

    private class DownloadHUDImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadHUDImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
        protected void onPreExecute() {
            synchronized (this) {

            }
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap bitmapImg = null;

            try {
                //Http Get Request
                HUDHttpRequest request = new HUDHttpRequest(RequestMethod.GET, urlDisplay);
                HUDHttpResponse response = mHUDConnectivityManager.sendWebRequest(request);
                if (response.hasBody()) {
                    byte[] data = response.getBody();
                    bitmapImg = BitmapFactory.decodeByteArray(data, 0, data.length);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmapImg;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            synchronized (this) {

            }
        }
    }
}
