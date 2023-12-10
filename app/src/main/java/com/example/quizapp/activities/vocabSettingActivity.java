package com.example.quizapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.quizapp.R;

public class vocabSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a translucent overlay view
        View overlayView = new View(this);
        overlayView.setBackgroundColor(ContextCompat.getColor(this, R.color.overlay_color));
        overlayView.setAlpha(0.5f); // Adjust the transparency level as desired

        // Add the overlay view to the current activity
        ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
        decorView.addView(overlayView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // Inflate the settings layout and add it to the FrameLayout
        View settingsView = LayoutInflater.from(this).inflate(R.layout.activity_vocab_setting, decorView, false);
        decorView.addView(settingsView);



        setContentView(R.layout.activity_vocab_setting);

        // Set the position of the activity dialog
//        Window window = getWindow();
//        WindowManager.LayoutParams params = window.getAttributes();
//        params.gravity = Gravity.BOTTOM;
//        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        window.setAttributes(params);
    }
}