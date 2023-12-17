package com.example.quizapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class WordTestActivity extends AppCompatActivity {

    private TextView tvWordToTest;
    private EditText etAnswer;
    private Button btnCheckAnswer;

    private String wordToTest;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_test);

        // Initialize UI elements
        tvWordToTest = findViewById(R.id.tvWordToTest);
        etAnswer = findViewById(R.id.etAnswer);
        btnCheckAnswer = findViewById(R.id.btnCheckAnswer);

        // Get the word to test from Intent
        Intent intent = getIntent();
        wordToTest = intent.getStringExtra("wordToTest");

        // Set the word to the TextView
        tvWordToTest.setText(wordToTest);

        // Set up Firestore instance
        firestore = FirebaseFirestore.getInstance();

        // Set up click listener for the Check Answer button
        btnCheckAnswer.setOnClickListener(v -> checkAnswer());
    }

    private void checkAnswer() {
        // Get user's answer
        String userAnswer = etAnswer.getText().toString().trim();

        // Retrieve the correct Vietnamese meaning from Firestore
        String userID = "AaWZ5yEnedL7al8jRhH9";
        String topicID="X6Y6syJQFRISV3vCIMPk";
        DocumentReference vocabRef = firestore.collection("topic")
                .document(userID) // Replace with the correct userID
                .collection("topicCreated")
                .document(topicID) // Replace with the correct topicID
                .collection("vocab")
                .document(wordToTest.toLowerCase()); // Assuming wordToTest is the English word

        vocabRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Access the fields of the document
                    String correctVietnameseMeaning = document.getString("vietnamese_meaning");

                    // Check the user's answer
                    if (userAnswer.equalsIgnoreCase(correctVietnameseMeaning)) {
                        // Correct answer
                        showToast("Correct!");
                    } else {
                        // Incorrect answer
                        showToast("Incorrect! The correct meaning is: " + correctVietnameseMeaning);
                    }
                } else {
                    showToast("Word not found in the database");
                }
            } else {
                showToast("Error getting document: " + task.getException());
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
