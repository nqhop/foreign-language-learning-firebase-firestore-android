package com.example.quizapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.R;
import com.example.quizapp.models.Word;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class AddTopicActivity extends AppCompatActivity {

    private EditText topicNameEditText;
    private EditText englishEditText;
    private EditText vietnameseEditText;
    private EditText descriptionEditText;
    private EditText imageEditText;
    private ImageButton addImageButton;

    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri imageUri;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topic);

        topicNameEditText = findViewById(R.id.editTextTopicName);
        englishEditText = findViewById(R.id.editTextEnglish);
        vietnameseEditText = findViewById(R.id.editTextVietnamese);
        descriptionEditText = findViewById(R.id.editTextDescription);
        imageEditText = findViewById(R.id.editTextImage);
        addImageButton = findViewById(R.id.addImageButton);

        // Initialize StorageReference
        storageReference = FirebaseStorage.getInstance().getReference("images");

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImageAndAddTopic();
            }
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageEditText.setText(imageUri.toString());
        }
    }

    private void uploadImageAndAddTopic() {
        if (imageUri != null) {
            // Create a unique image filename
            String imageName = UUID.randomUUID().toString();
            // Set the path to the "images" folder and the unique filename
            StorageReference imageRef = storageReference.child(imageName);

            // Upload the image to Firebase Storage
            imageRef.putFile(imageUri)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Image upload successful, get the download URL
                            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                // Create Word object with the download URL
                                Word word = new Word(
                                        englishEditText.getText().toString(),
                                        vietnameseEditText.getText().toString(),
                                        descriptionEditText.getText().toString(),
                                        uri.toString()
                                );

                                addTopic(word);
                            });
                        } else {
                            Toast.makeText(this, "Error uploading image", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }

    private void addTopic(Word word) {
        String topicName = topicNameEditText.getText().toString();

        if (TextUtils.isEmpty(topicName)) {
            Toast.makeText(this, "Please fill in the topic name", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();

        DocumentReference topicRef = firestore.collection("Users").document(userId)
                .collection("topics").document(UUID.randomUUID().toString());

        // Set the topic data including the image URL
        topicRef.set(word)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddTopicActivity.this, "Topic added successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddTopicActivity.this, "Error adding topic", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
