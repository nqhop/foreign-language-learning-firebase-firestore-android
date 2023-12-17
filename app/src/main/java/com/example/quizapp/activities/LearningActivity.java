package com.example.quizapp.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.R;
import com.example.quizapp.adapters.VocabLearningAdapter;
import com.example.quizapp.models.TopicLibrary;
import com.example.quizapp.models.Vocab2;
import com.example.quizapp.utils.FirebaseUtils;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class LearningActivity extends AppCompatActivity {

    private TextView nameOfTopic, userName, numberOfVocab;
    private RecyclerView learningVocabRecyclerView;
    private CardView flashCardView, multipleChoice, goTuCardView;

    private ArrayList<Vocab2> vocabList;
    private TextToSpeech toSpeech;
    private String collection = "", userID, topicID;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);

        // Initialize UI elements
        nameOfTopic = findViewById(R.id.textView6);
        userName = findViewById(R.id.textView16);
        numberOfVocab = findViewById(R.id.textView18);
        learningVocabRecyclerView = findViewById(R.id.learningVocabRecyclerView);
        flashCardView = findViewById(R.id.cardView);
        multipleChoice = findViewById(R.id.cardView3);
        goTuCardView = findViewById(R.id.cardView2);

        // Set up ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Learning");
        }

        // Get topic details from Intent
        Intent intent = getIntent();
        TopicLibrary topicLibraryItem = (TopicLibrary) intent.getSerializableExtra("TopicLibraryItem");
        collection = topicLibraryItem.isCreated() ? "topicCreated" : "topicSaved";
        userID = topicLibraryItem.getUserID();
        topicID = topicLibraryItem.getTopicID();

        // Display topic details
        nameOfTopic.setText(topicLibraryItem.getName());
        userName.setText(topicLibraryItem.getUser().getName());
        numberOfVocab.setText(topicLibraryItem.getsizeOfVocabList() + " thuật ngữ");

        // Set up vocabulary list
        setVocabList();

        // Set up click listeners for learning options
        flashCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVocabActivity();
            }
        });

        multipleChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMultipleChoiceTestActivity();
            }
        });

        // Set up click listener for "Gõ từ" CardView
        goTuCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGoTuActivity();
            }
        });
    }

    private void setVocabList() {
        vocabList = new ArrayList<>();
        firestore = FirebaseUtils.getFirestoreInstance();
        DocumentReference collectionRef = firestore.collection("topic").document(userID);

        // Retrieve vocabulary list from Firestore
        collectionRef.collection(collection).document(topicID).collection("vocab").get().addOnSuccessListener(querySnapshot -> {
            for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                Map<String, Object> data = documentSnapshot.getData();
                // Access the fields of the document
                Log.d("LearningActivity", (String) data.get("word"));
                vocabList.add(new Vocab2((String) data.get("word"), (String) data.get("vietnamese_meaning"), (String) data.get("status"), (Timestamp) data.get("created_at"), (Timestamp) data.get("update_at"), (Boolean) data.get("star")));
            }

            // Set up RecyclerView with the vocabulary list
            VocabLearningAdapter adapter = new VocabLearningAdapter(this, vocabList);
            adapter.setOnItemClickListener(new VocabLearningAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Vocab2 vocab) {
                    startWordTestActivity(vocab.getWord(), true); // Start testing from Vietnamese to English
                }
            });

            learningVocabRecyclerView.setAdapter(adapter);
            learningVocabRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        });

        // Set up Text-to-Speech
        speech();
    }

    private void startWordTestActivity(String word, boolean isVietnameseToEnglish) {
        Intent intent = new Intent(this, WordTestActivity.class);
        intent.putExtra("word", word);
        intent.putExtra("isVietnameseToEnglish", isVietnameseToEnglish);
        startActivity(intent);
    }

    private void speech() {
        toSpeech = new TextToSpeech(getApplicationContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                // Set language for speech
                toSpeech.setLanguage(Locale.UK);
            }
        });

        nameOfTopic.setOnClickListener(v -> {
            Toast.makeText(LearningActivity.this, "Speech", Toast.LENGTH_SHORT).show();
            toSpeech.speak("How are you?", TextToSpeech.QUEUE_FLUSH, null);
        });
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

    private void startVocabActivity() {
        Intent intent = new Intent(this, VocabActivity.class);
        intent.putParcelableArrayListExtra("vocabListExtra", vocabList);
        startActivity(intent);
    }

    private void startMultipleChoiceTestActivity() {
        Intent intent = new Intent(this, multipleChoiceTestActivity.class);
        intent.putParcelableArrayListExtra("vocabListExtra", vocabList);
        intent.putExtra("lengthOfList", vocabList.size());
        startActivity(intent);
    }

    private void startGoTuActivity() {
        // Start the activity you want to navigate to
        Intent intent = new Intent(this, WordTestActivity.class);
        // Add any extras or data you want to pass to the new activity
        startActivity(intent);
    }
}
