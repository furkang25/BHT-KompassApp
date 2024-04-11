package com.example.bhtkompassapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Timer;
import java.util.TimerTask;

public class LandingActivity extends AppCompatActivity {
    RelativeLayout layout;
    ImageView bhtIcon, homeImage;
    TextView name, welcome, homeText;
    String firstName, lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        layout = (RelativeLayout) findViewById(R.id.layout);
        bhtIcon = findViewById(R.id.bhtIcon);
        name = findViewById(R.id.nameText);
        welcome = findViewById(R.id.welcomeTextView);
        homeImage = findViewById(R.id.homeImage);
        homeText = findViewById(R.id.homeText);

        Bundle b = getIntent().getExtras();
        String firstName = b.getString("firstname");
        String lastName = b.getString("lastname");

        name.setText(firstName + " " + lastName);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.parseColor("#78CDCB"));

        final ObjectAnimator backgroundColorAnimator = ObjectAnimator.ofObject(layout,
                "backgroundColor",
                new ArgbEvaluator(),
                Color.WHITE,
                Color.parseColor("#78CDCB"));

        backgroundColorAnimator.setDuration(3000);
        backgroundColorAnimator.start();
        @SuppressLint("ResourceType") Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.drawable.slide_up);

        bhtIcon.startAnimation(slide_up);
        welcome.setAnimation(slide_up);
        name.setAnimation(slide_up);
        homeImage.setAnimation(slide_up);
        homeText.setAnimation(slide_up);

        new Handler(Looper.getMainLooper()).postDelayed(() -> startHomeActivity(), 3000);
    }

    private void startHomeActivity() {
        Intent intent = new Intent(LandingActivity.this, MainActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

}