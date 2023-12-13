package com.example.quizapp.activities;

import static java.security.AccessController.getContext;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class LearningActivity extends AppCompatActivity {

    TextView nameOfTopic, userName, numberOfVocab;
    CardView flashCardView;
    RecyclerView learningVocabRecyclerView;
    ArrayList<Vocab2> vocabList;
    TextToSpeech toSpeech;
    String collection = "", userID, topicID;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);
        nameOfTopic = findViewById(R.id.textView6);
        userName = findViewById(R.id.textView16);
        numberOfVocab = findViewById(R.id.textView18);
        learningVocabRecyclerView = findViewById(R.id.learningVocabRecyclerView);
        flashCardView = findViewById(R.id.cardView);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Learning");
        }
        Intent intent = getIntent();
        TopicLibrary topicLibraryItem = (TopicLibrary) intent.getSerializableExtra("TopicLibraryItem");
        collection = topicLibraryItem.isCreated() ? "topicCreated" : "topicSaved";
        userID = topicLibraryItem.getUserID();
        topicID = topicLibraryItem.getTopicID();
        Log.d("LearningActivity", "nameOfTopic: " + topicLibraryItem.getName() + " userName: " + topicLibraryItem.getUser().getName() + " numberOfVocab " + topicLibraryItem.getsizeOfVocabList());
        Log.d("LearningActivity", "userID: " + topicLibraryItem.getUserID() + " topicID: " + topicLibraryItem.getTopicID());
        nameOfTopic.setText(topicLibraryItem.getName());
        userName.setText(topicLibraryItem.getUser().getName());
        numberOfVocab.setText(topicLibraryItem.getsizeOfVocabList() + " thuật ngữ");
        setVocabList();
    }

    private void setVocabList() {
        vocabList = new ArrayList<>();
        firestore = FirebaseUtils.getFirestoreInstance();
        DocumentReference collectionRef = firestore.collection("topic").document(userID);
        collectionRef.collection(collection).document(topicID).collection("vocab").get().addOnSuccessListener(querySnapshot -> {
            for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                Map<String, Object> data = documentSnapshot.getData();
                // Access the fields of the document
                Log.d("LearningActivity", (String) data.get("word"));
                vocabList.add(new Vocab2((String) data.get("word"), (String) data.get("vietnameses_meaning"), (String) data.get("status"), (Timestamp) data.get("created_at"), (Timestamp) data.get("update_at"), (Boolean) data.get("star")));
            }
            VocabLearningAdapter adapter = new VocabLearningAdapter(this, vocabList);
            learningVocabRecyclerView.setAdapter(adapter);
            learningVocabRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        });
        speech();
    }

    private void speech(){
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

        nameOfTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LearningActivity.this, "speech", Toast.LENGTH_SHORT).show();
                toSpeech.speak("how are you?", toSpeech.QUEUE_FLUSH, null);
            }
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
}