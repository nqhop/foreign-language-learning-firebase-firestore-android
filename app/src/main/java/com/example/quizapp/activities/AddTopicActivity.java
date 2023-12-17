package com.example.quizapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.quizapp.R;
import com.example.quizapp.fragments.AddFragment;
import com.example.quizapp.fragments.LibraryFragment;
import com.example.quizapp.models.Vocab2;
import com.example.quizapp.models.Word;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddTopicActivity extends AppCompatActivity {

    private EditText topicNameEditText;

    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri imageUri;
    private StorageReference storageReference;

    LinearLayout linearLayoutVocabList;
    LayoutInflater inflater;
    Button button;
    List<View> rootViewList = new ArrayList<>();
    List<String> wordList = new ArrayList<>();
    List<String> wordMeaningList = new ArrayList<>();
    String topicName, topicID;
    Button addButton;
    Boolean isUpdate = false;
    ArrayList<Vocab2> vocabListExtra;
    FirebaseFirestore firestore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topic);
        firestore = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
//      userID = auth.getCurrentUser().getUid();
        userID = "AaWZ5yEnedL7al8jRhH9";
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        linearLayoutVocabList = findViewById(R.id.linearLayoutVocabList);
        topicNameEditText = findViewById(R.id.editTextTopicName);
        button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            addNewVocab("", "");
        });

        // Initialize StorageReference
        storageReference = FirebaseStorage.getInstance().getReference("images");

        addButton = findViewById(R.id.addButton);
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
                addTopic2();
                Log.d("AddTopicActivity", "finish--------");
                finish();
            }

        });
        getMyIntent();
    }

    private void getMyIntent() {
        Intent intent = getIntent();
        if(intent.hasExtra("nameOfTopic")){
            isUpdate = true;
            topicName = intent.getStringExtra("nameOfTopic");
            vocabListExtra = intent.getParcelableArrayListExtra("vocabListExtra");
            topicID = intent.getStringExtra("topicID");
            topicNameEditText.setText(topicName);
            addButton.setText("Update");
            Log.d("AddTopicActivity", topicName);
            Log.d("AddTopicActivity", topicID);
            for(int i = 0; i < vocabListExtra.size(); i++){
                addNewVocab(vocabListExtra.get(i).getWord(), vocabListExtra.get(i).getMeaning());
            }
        }


    }

    private void addTopic2() {
        String topicName = topicNameEditText.getText().toString();
        if (TextUtils.isEmpty(topicName)) {
            Toast.makeText(this, "Please fill in the topic name", Toast.LENGTH_SHORT).show();
            return;
        }
        CollectionReference collectionRef = firestore.collection("topic");
        if(isUpdate){
            collectionRef.document(userID).collection("topicCreated").document(topicID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    addOrUpdateTopic();
                }
            });
        }else {
            addOrUpdateTopic();
        }
        finish();


//        DocumentReference topicRef = firestore.collection("Users").document(userId)
//                .collection("topics").document(UUID.randomUUID().toString());
//
//        // Set the topic data including the image URL
//        topicRef.set(word)
//                .addOnCompleteListener(this, task -> {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(AddTopicActivity.this, "Topic added successfully", Toast.LENGTH_SHORT).show();
//                        finish();
//                    } else {
//                        Toast.makeText(AddTopicActivity.this, "Error adding topic", Toast.LENGTH_SHORT).show();
//                    }
//                });
    }
    private void addOrUpdateTopic(){
        Map<String, Object> topicInfo = new HashMap<>();
        topicName = topicNameEditText.getText().toString();
        topicInfo.put("name", topicName);
        CollectionReference collectionRef = firestore.collection("topic");
        collectionRef.document(userID).collection("topicCreated").add(topicInfo).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                for(int i = 0; i < wordList.size(); i++){
                    Log.d("addTopicActivity","word 2:" + wordList.get(i));
                    Map<String, Object> vocab = setMapOfVocab(i);
                    collectionRef.document(userID).collection("topicCreated").document(documentReference.getId()).collection("vocab").add(vocab);
                }
                Toast.makeText(AddTopicActivity.this, "Successfully", Toast.LENGTH_SHORT).show();

                // Restart the app
                Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

        }).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddTopicActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private Map<String, Object> setMapOfVocab(int index){
        Map<String, Object> vocab = new HashMap<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vocab.put("created_at", Timestamp.from(Instant.now()));
            vocab.put("update_at", Timestamp.from(Instant.now()));
        }
        vocab.put("star", false);
        vocab.put("status", "new");

        vocab.put("vietnameses_meaning", wordMeaningList.get(index));
        vocab.put("word", wordList.get(index).toLowerCase());
        return vocab;
    }

    private void addNewVocab(String word, String wordMeaning) {
        View rootViewNewVocabItem = inflater.inflate(R.layout.add_new_vocab_item, null);
        linearLayoutVocabList.addView(rootViewNewVocabItem);
        rootViewList.add(rootViewNewVocabItem);
        TextView deleteVocabInput = rootViewNewVocabItem.findViewById(R.id.textView41);
        deleteVocabInput.setOnClickListener(v -> {
            linearLayoutVocabList.removeView(rootViewNewVocabItem);
            rootViewList.remove(rootViewNewVocabItem);
        });

        if(isUpdate){
            TextView wordTextView = rootViewNewVocabItem.findViewById(R.id.editTextText);
            TextView wordMeaningTextView = rootViewNewVocabItem.findViewById(R.id.editTextText2);
            wordTextView.setText(word);
            wordMeaningTextView.setText(wordMeaning);
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
//            imageUri = data.getData();
//            imageEditText.setText(imageUri.toString());
//        }
//    }

//    private void uploadImageAndAddTopic() {
//        if (imageUri != null) {
//            // Create a unique image filename
//            String imageName = UUID.randomUUID().toString();
//            // Set the path to the "images" folder and the unique filename
//            StorageReference imageRef = storageReference.child(imageName);
//
//            // Upload the image to Firebase Storage
//            imageRef.putFile(imageUri)
//                    .addOnCompleteListener(this, task -> {
//                        if (task.isSuccessful()) {
//                            // Image upload successful, get the download URL
//                            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
//                                // Create Word object with the download URL
//                                Word word = new Word(
//                                        englishEditText.getText().toString(),
//                                        vietnameseEditText.getText().toString(),
//                                        descriptionEditText.getText().toString(),
//                                        uri.toString()
//                                );
//
//                                addTopic(word);
//                            });
//                        } else {
//                            Toast.makeText(this, "Error uploading image", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        } else {
//            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
//        }
//    }

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
