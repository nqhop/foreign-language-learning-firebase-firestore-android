package com.example.quizapp.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddTopicActivity extends AppCompatActivity {

    private EditText topicNameEditText;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageReference storageReference;
    private LinearLayout linearLayoutVocabList;
    private LayoutInflater inflater;
    private Button button;
    private List<View> rootViewList = new ArrayList<>();
    private List<String> wordList = new ArrayList<>();
    private List<String> wordMeaningList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topic);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        linearLayoutVocabList = findViewById(R.id.linearLayoutVocabList);
        topicNameEditText = findViewById(R.id.editTextTopicName);
        button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            addNewVocab();
        });

        storageReference = FirebaseStorage.getInstance().getReference("images");

        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wordList.clear();
                wordMeaningList.clear();
                for (View rootView : rootViewList) {
                    EditText word = rootView.findViewById(R.id.editTextText);
                    EditText wordMeaning = rootView.findViewById(R.id.editTextText2);
                    wordList.add(String.valueOf(word.getText()));
                    wordMeaningList.add(String.valueOf(wordMeaning.getText()));
                }
                addTopic();
            }
        });
    }

    private void addTopic() {
        String topicName = topicNameEditText.getText().toString();
        if (TextUtils.isEmpty(topicName)) {
            Toast.makeText(this, "Please fill in the topic name", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userID = auth.getCurrentUser().getUid();

        Map<String, Object> topicInfo = new HashMap<>();
        topicInfo.put("name", topicName);
        CollectionReference collectionRef = firestore.collection("topic");
        collectionRef.document(userID).collection("topicCreated").add(topicInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        for (int i = 0; i < wordList.size(); i++) {
                            Log.d("AddTopicActivity", "word:" + wordList.get(i));
                            Map<String, Object> vocab = new HashMap<>();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vocab.put("created_at", Timestamp.from(Instant.now()));
                                vocab.put("updated_at", Timestamp.from(Instant.now()));
                            }
                            vocab.put("star", false);
                            vocab.put("status", "new");
                            vocab.put("vietnamese_meaning", wordMeaningList.get(i));
                            vocab.put("word", wordList.get(i).toLowerCase());
                            collectionRef.document(userID).collection("topicCreated")
                                    .document(documentReference.getId()).collection("vocab").add(vocab);
                        }
                        Toast.makeText(AddTopicActivity.this, "Topic added successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddTopicActivity.this, "Error adding topic", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addNewVocab() {
        View rootViewNewVocabItem = inflater.inflate(R.layout.add_new_vocab_item, null);
        linearLayoutVocabList.addView(rootViewNewVocabItem);
        rootViewList.add(rootViewNewVocabItem);
        TextView deleteVocabInput = rootViewNewVocabItem.findViewById(R.id.textView41);
        deleteVocabInput.setOnClickListener(v -> {
            linearLayoutVocabList.removeView(rootViewNewVocabItem);
            rootViewList.remove(rootViewNewVocabItem);
        });
    }
}
