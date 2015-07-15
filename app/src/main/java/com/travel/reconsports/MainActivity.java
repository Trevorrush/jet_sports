package com.travel.reconsports;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends Activity{

    private Button downloadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  Setup button
        downloadButton = (Button) findViewById(R.id.tempDownloadButton) ;
        
    }

}
