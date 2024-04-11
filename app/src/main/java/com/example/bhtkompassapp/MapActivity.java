package com.example.bhtkompassapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class MapActivity extends AppCompatActivity {


    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new Callback());
        webView.loadUrl("https://map-viewer.situm.com/?email=guenesg25%40gmail.com&apikey=04b6f4f4a258da85ad1d0acbb28cfee7acf9b41dbd863857c8cffefaef3dce05&lng=en&buildingid=12048&floorid=36969&poiid=130193");
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.roomMapFragment);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.homeFragment:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                    break;
                case R.id.roomMapFragment:
                    startActivity(new Intent(getApplicationContext(), MapActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                    break;
                case R.id.scheduleFragment:
                    Calendar calendar = Calendar.getInstance();
                    int day = calendar.get(Calendar.DAY_OF_WEEK);
                    switch (day) {
                        case Calendar.TUESDAY:
                            startActivity(new Intent(getApplicationContext(), ScheduleTuesdayActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                            break;
                        case Calendar.WEDNESDAY:
                            startActivity(new Intent(getApplicationContext(), ScheduleWednesdayActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                            break;
                        case Calendar.THURSDAY:
                            startActivity(new Intent(getApplicationContext(), ScheduleThursdayActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                            break;
                        case Calendar.FRIDAY:
                            startActivity(new Intent(getApplicationContext(), ScheduleFridayActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                            break;
                        default:
                            startActivity(new Intent(getApplicationContext(), ScheduleMondayActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                            break;
                    }
                    break;
                case R.id.settingsFragment:
                    startActivity(new Intent(getApplicationContext(), SettingActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                    break;
            }
            return true;
        });

    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return false;
        }
    }
}