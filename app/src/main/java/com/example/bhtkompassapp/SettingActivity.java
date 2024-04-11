package com.example.bhtkompassapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Calendar;

public class SettingActivity extends AppCompatActivity {

    LinearLayout logout, dataPrivacy, aboutUs, deleteSchedule, editProfile, changePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        logout = findViewById(R.id.logout);
        dataPrivacy = findViewById(R.id.dataPrivacy);
        aboutUs = findViewById(R.id.aboutUs);
        deleteSchedule = findViewById(R.id.deleteSchedule);
        editProfile = findViewById(R.id.editProfile);
        changePassword = findViewById(R.id.changePassword);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#78CDCB"));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.settingsFragment);
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

        logout.setOnClickListener(view -> {
            ParseUser.logOutInBackground(e -> {
                if (e == null) {
                    startPasswordAlert("Abmeldung", "Sind sie sich sicher, dass sie sich abmelden wollen?");
                }
            });
        });

        deleteSchedule.setOnClickListener(view -> {
            startScheduleAlert("Stundenplan", "Sind sie sich sicher, dass sie ihren Stundenplan zurücksetzen wollen?");
        });

        dataPrivacy.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), DataPrivacyActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });

        aboutUs.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), AboutUsActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });

        editProfile.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), EditProfileActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });

        changePassword.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), ChangePasswordActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });
    }

    private void startPasswordAlert (String alert, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(SettingActivity.this).create();
        alertDialog.setTitle(alert);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ja",
                (dialog, which) -> {
                    dialog.dismiss();
                    startLoginActivity();
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Nein",
                (dialog, which) -> {
                    dialog.dismiss();
                });
        alertDialog.show();
    }

    private void startScheduleAlert (String alert, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(SettingActivity.this).create();
        alertDialog.setTitle(alert);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ja",
                (dialog, which) -> {
                    deleteSchedule();
                    dialog.dismiss();
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Nein",
                (dialog, which) -> {
                    dialog.dismiss();
                });
        alertDialog.show();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    public void deleteSchedule () {
        ParseQuery<ParseObject> query = new ParseQuery<>("Schedule");
        query.whereEqualTo("user", ParseUser.getCurrentUser().getUsername());
        query.findInBackground((objects, e) -> {
            if (e == null) {
                for (int i = 0; i < objects.size(); i++) {
                    try {
                        objects.get(i).delete();
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
}