package com.travel.reconsports;

import android.view.View;

import com.reconinstruments.ui.UIUtils;
import com.reconinstruments.ui.carousel.CarouselItem;

/**
 * Created by raymondchen on 15-07-16.
 */
public class Carousel_Score_Item extends CarouselItem {

    String homeTeamTitle;
    String awayTeamTitle;

    public Carousel_Score_Item(String homeTeamTitle, String awayTeamTitle) {
        this.homeTeamTitle = homeTeamTitle;
        this.awayTeamTitle = awayTeamTitle;
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

}
