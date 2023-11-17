package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

interface setTextAndFrondOrBackRunnable {
    void run(String text);
}
public class VocabActivity extends AppCompatActivity {

    TextView flipCardTextView;

    ObjectAnimator animator1, animator2;
    boolean flashcardInFrond = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocab);
        flipCardTextView = findViewById(R.id.flipCardTextView);
        // close animator
        animator1 = ObjectAnimator.ofFloat(flipCardTextView, "scaleX", 1f, 0f);
        // open animator
        animator2 = ObjectAnimator.ofFloat(flipCardTextView, "scaleX", 0f, 1f);
        animator1.setInterpolator(new DecelerateInterpolator());
        animator2.setInterpolator(new AccelerateInterpolator());

        setOnClickForFlipCard();
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