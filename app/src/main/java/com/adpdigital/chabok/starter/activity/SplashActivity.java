package com.adpdigital.chabok.starter.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.adpdigital.chabok.starter.R;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            public void run() {
                Intent geoIntent = new Intent(SplashActivity.this, GeoActivity.class);
                SplashActivity.this.startActivity(geoIntent);
                SplashActivity.this.finish();
            }
        }, 2000);
    }

}
