package com.example.quizapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.R;
import com.example.quizapp.models.TopicLibrary;
import com.example.quizapp.models.User;
import com.example.quizapp.models.Vocab2;
import com.google.firebase.Timestamp;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

interface setTextAndFrondOrBackRunnable {
    void run(String text);
}
public class VocabActivity extends AppCompatActivity {

    TextView customTitle, flipCardTextView;
    LinearLayout flipCardLinearLayout, playArrowWrapper;
    ImageView arrowLeft, arrowRight, playArrow, speechIcon;
    ObjectAnimator animator1, animator2;
    boolean flashcardInFrond = true;
    TopicLibrary topicLibrary = new TopicLibrary("vegatables", "AaWZ5yEnedL7al8jRhH9", "001", 7, new User("H 2023", "hop@gmail.com", "AaWZ5yEnedL7al8jRhH9"));
    ArrayList<Vocab2> vocabList = new ArrayList<>();
    int currentVocabIndex = 0;
    TextToSpeech toSpeech, toSpeechMeaning;
    boolean autoScroll = false;
    String word = "", meaning = "";
    Timer timer = new Timer();
    boolean isAutoClickFlag = false, isClickByUser = false;
    setTextAndFrondOrBackRunnable setTextAndFrondOrBack;
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


        arrowLeft = findViewById(R.id.imageView13);
        arrowRight = findViewById(R.id.imageView14);
        playArrow = findViewById(R.id.imageView15);
        flipCardTextView = findViewById(R.id.textView13);
        flipCardLinearLayout = findViewById(R.id.flipCardTextView);
        playArrowWrapper = findViewById(R.id.playArrowWrapper);
        speechIcon = findViewById(R.id.imageView12);
        // close animator
        animator1 = ObjectAnimator.ofFloat(flipCardLinearLayout, "scaleX", 1f, 0f);
        // open animator
        animator2 = ObjectAnimator.ofFloat(flipCardLinearLayout, "scaleX", 0f, 1f);
        animator1.setInterpolator(new DecelerateInterpolator());
        animator2.setInterpolator(new AccelerateInterpolator());

        initData();
        fexData();
        word = vocabList.get(0).getWord();
        flipCardTextView.setText(word);
        meaning = vocabList.get(0).getMeaning();
        setTitleActionBar();
        setVocabFeature();

        speechIcon.setOnClickListener(v -> {
            speech();
        });

        setOnClickForFlipCard();
    }
    private void initData(){
        setTextAndFrondOrBack = new setTextAndFrondOrBackRunnable() {
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

        toSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                // if No error is found then only it will run
                if(status!=TextToSpeech.ERROR){
                    // To Choose language of speech
                    toSpeech.setLanguage(Locale.UK);
                }
            }
        });

        toSpeechMeaning = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                // if No error is found then only it will run
                if(status!=TextToSpeech.ERROR){
                    // To Choose language of speech
                    toSpeechMeaning.setLanguage(Locale.forLanguageTag("vi-VN"));
                }
            }
        });
    }

    private void fexData(){

        vocabList.add(new Vocab2("Apple", "qủa táo", "new", Timestamp.now(), Timestamp.now(), false));
        vocabList.add(new Vocab2("Avocado", "qủa bơ", "new", Timestamp.now(), Timestamp.now(), false));
        vocabList.add(new Vocab2("Strawberry", "dâu tây", "new", Timestamp.now(), Timestamp.now(), false));
        vocabList.add(new Vocab2("watermelon", "dưa hấu", "new", Timestamp.now(), Timestamp.now(), false));
        vocabList.add(new Vocab2("mango", "trái xoài", "new", Timestamp.now(), Timestamp.now(), false));
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

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                    rootView.removeView(overlayView);
                }
        });

        // dialog action
        TextView resetVocab = dialog.findViewById(R.id.textView29);
        resetVocab.setOnClickListener(v -> {
            resetFlashCard();
            dialog.dismiss();
        });

        RadioButton radioButtonWord, radioButtonMeaning;
        radioButtonWord = dialog.findViewById(R.id.radio_a);
        radioButtonMeaning = dialog.findViewById(R.id.radio_b);

        radioButtonWord.setOnCheckedChangeListener(listenerRadio);
        radioButtonMeaning.setOnCheckedChangeListener(listenerRadio);
    }

    // Dialog helper
    CompoundButton.OnCheckedChangeListener listenerRadio
            = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                setWordAndMeaning();
                setTextInFlipCard();
                Toast.makeText(VocabActivity.this, "Bạn chọn:" + compoundButton.getText(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void resetFlashCard(){
        Toast.makeText(this, "resetFlashCard", Toast.LENGTH_SHORT).show();
        currentVocabIndex = 0;
        flashcardInFrond = true;
        setWordAndMeaning();
        setTitleActionBar();
        setTextInFlipCard();

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
            createTheDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setVocabFeature(){
        arrowRight.setOnClickListener(v -> {
            if(currentVocabIndex < vocabList.size() - 1){
                currentVocabIndex++;
                setWordAndMeaning();
                setTextInFlipCard();
                setTitleActionBar();
            }
        });
        arrowLeft.setOnClickListener(v -> {
            if(currentVocabIndex >0){
                currentVocabIndex--;
                setWordAndMeaning();
                setTextInFlipCard();
                setTitleActionBar();
            }
        });
        playArrowWrapper.setOnClickListener(v -> {
            if(!isClickByUser){
                isClickByUser = true;
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                playArrow.performClick();
                            }
                        },
                        2000
                );
            }else {
                isClickByUser = false;
            }
        });
        playArrow.setOnClickListener(v -> {
            if(!isAutoClickFlag){
                if(autoScroll) {
                    playArrow.setImageResource(R.drawable.baseline_play_arrow_24);
                }else{
                    playArrow.setImageResource(R.drawable.baseline_pause_24);
                }
            }

            autoScroll = !autoScroll;
            autoScroll();
            if(currentVocabIndex < vocabList.size() - 1){
                isAutoClickFlag = true;
                clickEventArrowRightDelay();
                clickEventPlayArrowDelay();
            }else {
                new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            playArrow.setImageResource(R.drawable.baseline_play_arrow_24);
                        }
                    },
                    5000
                );
            }
        });
    }
    private void clickEventArrowRightDelay(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                flashcardInFrond = !flashcardInFrond;
                arrowRight.performClick();
            }
        }, 5000);
    }
    private void clickEventPlayArrowDelay(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                playArrow.performClick();
            }
        }, 7000);
    }

    private void autoScroll(){
        int duration = 2000;


        speech();
        flipCardTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                flipCard();
            }
        }, duration);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                speech();
            }
        };
        timer.schedule(task, 3000);

    }
    private void setWordAndMeaning(){
        word = vocabList.get(currentVocabIndex).getWord();
        meaning = vocabList.get(currentVocabIndex).getMeaning();
    }
    private void setTextInFlipCard(){
        flipCardTextView.setText(flashcardInFrond ? word : meaning);
    }
    private void setTitleActionBar(){
        customTitle.setText(currentVocabIndex + 1 + " / " + vocabList.size());
    }

    private void speech(){
        if(flashcardInFrond)
            toSpeech.speak(word, toSpeech.QUEUE_FLUSH, null);
        else
            toSpeechMeaning.speak(meaning, toSpeechMeaning.QUEUE_FLUSH, null);
    }

    private void setOnClickForFlipCard() {

        flipCardTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCard();
            }
        });
    }

    private void flipCard(){
    //   setTextAndFrondOrBack.run(flashcardInFrond ? "Back" : "frond");
        setTextAndFrondOrBack.run(flashcardInFrond ? meaning : word);
        flashcardInFrond = !flashcardInFrond;
    }
}