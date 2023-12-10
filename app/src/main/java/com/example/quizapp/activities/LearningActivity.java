package com.example.quizapp.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import com.example.quizapp.R;
import com.example.quizapp.models.TopicLibrary;

public class LearningActivity extends AppCompatActivity {

    TextView nameOfTopic, userName, numberOfVocab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);
        nameOfTopic = findViewById(R.id.textView6);
        userName = findViewById(R.id.textView16);
        numberOfVocab = findViewById(R.id.textView18);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Learning");
        }
        Intent intent = getIntent();
        TopicLibrary TopicLibraryItem = (TopicLibrary) intent.getSerializableExtra("TopicLibraryItem");
        Log.d("LearningActivity", "nameOfTopic: " + TopicLibraryItem.getName() + " userName: " + TopicLibraryItem.getUser().getName() + " numberOfVocab " + TopicLibraryItem.getsizeOfVocabList());
        nameOfTopic.setText(TopicLibraryItem.getName());
        userName.setText(TopicLibraryItem.getUser().getName());
        numberOfVocab.setText(TopicLibraryItem.getsizeOfVocabList() + " thuật ngữ");
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.vocab_menu, menu);
        return true;
    }
}