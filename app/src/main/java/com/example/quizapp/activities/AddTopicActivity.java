package com.example.quizapp.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.R;
import com.example.quizapp.models.Topic;
import com.example.quizapp.models.Word;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddTopicActivity extends AppCompatActivity {

    private EditText topicNameEditText;
    private EditText englishEditText;
    private EditText vietnameseEditText;
    private EditText descriptionEditText;
    private EditText imageEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topic);

        topicNameEditText = findViewById(R.id.editTextTopicName);
        englishEditText = findViewById(R.id.editTextEnglish);
        vietnameseEditText = findViewById(R.id.editTextVietnamese);
        descriptionEditText = findViewById(R.id.editTextDescription);
        imageEditText = findViewById(R.id.editTextImage);

        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTopic();
            }
        });
    }

    private void addTopic() {
        String topicName = topicNameEditText.getText().toString();
        String english = englishEditText.getText().toString();
        String vietnamese = vietnameseEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String image = imageEditText.getText().toString();

        if (TextUtils.isEmpty(topicName) || TextUtils.isEmpty(english) || TextUtils.isEmpty(vietnamese)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng Word
        Word word = new Word(english, vietnamese, description, image);

        // Tạo đối tượng Topic
        Map<String, Word> wordsMap = new HashMap<>();
        wordsMap.put(UUID.randomUUID().toString(), word);

        Topic topic = new Topic(topicName, wordsMap);

        // Lưu topic vào Firestore
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();

        firestore.collection("Users").document(userId)
                .collection("topics")
                .add(topic)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            // Thành công
                            Toast.makeText(AddTopicActivity.this, "Topic added successfully", Toast.LENGTH_SHORT).show();
                            finish(); // Kết thúc activity sau khi thêm thành công
                        } else {
                            // Xử lý khi không thành công
                            Exception e = task.getException();
                            if (e != null) {
                                e.printStackTrace();
                            }
                            Toast.makeText(AddTopicActivity.this, "Failed to add topic", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
