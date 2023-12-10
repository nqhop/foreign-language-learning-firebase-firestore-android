package com.example.quizapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.R;

interface setTextAndFrondOrBackRunnable {
    void run(String text);
}
public class VocabActivity extends AppCompatActivity {

    TextView flipCardTextView, customTitle;

    ObjectAnimator animator1, animator2;
    boolean flashcardInFrond = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocab);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.custom_action_bar);
        customTitle = actionBar.getCustomView().findViewById(R.id.custom_title);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(null);
        customTitle.setText("Centered Title");


        flipCardTextView = findViewById(R.id.flipCardTextView);
        // close animator
        animator1 = ObjectAnimator.ofFloat(flipCardTextView, "scaleX", 1f, 0f);
        // open animator
        animator2 = ObjectAnimator.ofFloat(flipCardTextView, "scaleX", 0f, 1f);
        animator1.setInterpolator(new DecelerateInterpolator());
        animator2.setInterpolator(new AccelerateInterpolator());

        setOnClickForFlipCard();
    }

    private void createTheDialog(){
        // set the translucent overlay on the current activity
        ViewGroup rootView = findViewById(android.R.id.content);
        View overlayView = new View(this);
        overlayView.setBackgroundColor(ContextCompat.getColor(this, R.color.overlay_color));
        overlayView.setAlpha(0.5f); // Adjust the transparency level as desired
        rootView.addView(overlayView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        // Create the dialog
        Dialog dialog = new Dialog(this, R.style.AppTheme_TranslucentDialog);
        dialog.setContentView(R.layout.activity_vocab_setting);

        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.BOTTOM;
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(layoutParams);
        }

        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.flash_card_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.flashCardSettings){
            Toast.makeText(this, "flashCardSettings", Toast.LENGTH_SHORT).show();
            createTheDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setOnClickForFlipCard() {

        setTextAndFrondOrBackRunnable setTextAndFrondOrBack = new setTextAndFrondOrBackRunnable() {
            @Override
            public void run(String text) {
                animator1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        flipCardTextView.setText(text);
                        animator2.start();
                    }
                });
                animator1.start();
            }
        };
        flipCardTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTextAndFrondOrBack.run(flashcardInFrond ? "Back" : "frond");
                flashcardInFrond = !flashcardInFrond;
            }
        });
    }
}