package com.example.bhtkompassapp;

import androidx.fragment.app.FragmentActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class HomeActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.homeFragment);
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

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#78CDCB"));
    }
}