package com.example.quizapp.activities;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.R;
import com.example.quizapp.adapters.LibraryDirectoryAdapter;
import com.example.quizapp.adapters.VocabLearningAdapter;
import com.example.quizapp.fragments.LibraryFragment;
import com.example.quizapp.models.Directory2;
import com.example.quizapp.models.TopicDirectory;
import com.example.quizapp.models.TopicLibrary;
import com.example.quizapp.models.Vocab2;
import com.example.quizapp.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LearningActivity extends AppCompatActivity {

    TextView nameOfTopic, userName, numberOfVocab;
    CardView flashCardView, multipleChoice;
    RecyclerView learningVocabRecyclerView;
    ArrayList<Vocab2> vocabList;
    TextToSpeech toSpeech;
    String UserIDAcount = "AaWZ5yEnedL7al8jRhH9";
    String collection = "", userID, topicID;
    FirebaseFirestore firestore;
    TopicLibrary topicLibraryItem;
    ArrayList<Directory2> myDirectoriesList;
    ArrayList<String> myDirectoriesName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);
        nameOfTopic = findViewById(R.id.textView6);
        userName = findViewById(R.id.textView16);
        numberOfVocab = findViewById(R.id.textView18);
        learningVocabRecyclerView = findViewById(R.id.learningVocabRecyclerView);
        flashCardView = findViewById(R.id.cardView);
        multipleChoice = findViewById(R.id.cardView3);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Learning");
        }
        Intent intent = getIntent();
        topicLibraryItem = (TopicLibrary) intent.getSerializableExtra("TopicLibraryItem");
        collection = topicLibraryItem.isCreated() ? "topicCreated" : "topicSaved";
        userID = topicLibraryItem.getUserID();
        topicID = topicLibraryItem.getTopicID();
        Log.d("LearningActivity", "nameOfTopic: " + topicLibraryItem.getName() + " userName: " + topicLibraryItem.getUser().getName() + " numberOfVocab " + topicLibraryItem.getsizeOfVocabList());
        Log.d("LearningActivity", "userID: " + topicLibraryItem.getUserID() + " topicID: " + topicLibraryItem.getTopicID());
        nameOfTopic.setText(topicLibraryItem.getName());
        userName.setText(topicLibraryItem.getUser().getName());
        numberOfVocab.setText(topicLibraryItem.getsizeOfVocabList() + " thuật ngữ");
        setVocabList();

        // learning options
        flashCardView.setOnClickListener(v -> {
            Intent i = new Intent(this, VocabActivity.class);
            i.putParcelableArrayListExtra("vocabListExtra", vocabList);
            startActivity(i);
        });

        multipleChoice.setOnClickListener(v -> {
            Intent i = new Intent(this, multipleChoiceTestActivity.class);
            i.putParcelableArrayListExtra("vocabListExtra", vocabList);
            i.putExtra("lengthOfList", vocabList.size());
            startActivity(i);
        });
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
        Log.d("onCreateOptionsMenu", UserIDAcount);
        Log.d("onCreateOptionsMenu", userID);
        MenuItem menuItemEdit = menu.findItem(R.id.edit);
        MenuItem menuItemPrivacyTopic = menu.findItem(R.id.privacyTopic);
        if(!UserIDAcount.equals(userID)){
            menuItemEdit.setVisible(false);
            menuItemPrivacyTopic.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== R.id.edit){
            Intent intent = new Intent(this, AddTopicActivity.class);
            intent.putExtra("nameOfTopic", topicLibraryItem.getName());
            intent.putParcelableArrayListExtra("vocabListExtra", vocabList);
            intent.putExtra("topicID", topicLibraryItem.getTopicID());
            startActivityForResult(intent, 111);
            startActivity(intent);
        } else if (item.getItemId() == R.id.privacyTopic) {
            setPrivacyTopic();
        } else if (item.getItemId() == R.id.addToDirectory) {
            myAddToDirectory();
        }
        return super.onOptionsItemSelected(item);
    }

    private void myAddToDirectory() {
        myDirectoriesList = new ArrayList<>();
        myDirectoriesName = new ArrayList<>();

        String collectionName = "forder";
        String subcollectionName = "forder";

        DocumentReference documentRef = firestore.collection(collectionName).document(UserIDAcount);

        CollectionReference subcollectionRef = documentRef.collection(subcollectionName);

        subcollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                            Log.d("PageFragmentDirectory","created " + documentSnapshot.getData().get("name"));
                            Log.d("PageFragmentDirectory","ID " + documentSnapshot.getId());
                            myDirectoriesList.add(new Directory2((String) documentSnapshot.getData().get("name"), documentSnapshot.getId()));
                            myDirectoriesName.add((String) documentSnapshot.getData().get("name"));
                        }
                    }
                    showDialog();
                }
            };
        });

    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn thư mục");

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_listview, null);
        builder.setView(dialogView);

        ListView listView = dialogView.findViewById(R.id.listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myDirectoriesName);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedItem = myDirectoriesName.get(position);
//                addTopicToDirectoryInFirestore(position);
                Toast.makeText(LearningActivity.this, "Added to: " + selectedItem, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addTopicToDirectoryInFirestore(int position) {
        String collectionName = "forder";
        String subcollectionName = "forder";

        DocumentReference documentRef = firestore.collection(collectionName).document(UserIDAcount);

        DocumentReference documentReference = documentRef.collection(subcollectionName).document(myDirectoriesList.get(position).getId());
        String fieldToAdd = "my_field";
        List<String> initialValue = Arrays.asList("1", "2", "3");

        firestore.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot documentSnapshot = transaction.get(documentRef);

                        if (documentSnapshot.exists()) {
                            if (documentSnapshot.contains(fieldToAdd)) {
                                Log.d("Firestore", "Field already exists");
                                return null;
                            }
                        }
                        transaction.update(documentRef, fieldToAdd, FieldValue.arrayUnion(initialValue.toArray()));
                        return null;
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firestore", "Field created and array value added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "Failed to create field or add array value", e);
                    }
                });
    }


    private void setPrivacyTopic() {
        firestore = FirebaseUtils.getFirestoreInstance();
        DocumentReference collectionRef = firestore.collection("topic").document(userID);
        collectionRef.collection(collection).document(topicID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Object fieldValue = document.get("privacy");
                        // Use the fieldValue as needed
                        Log.d("setPrivacyTopic", "Field value: " + fieldValue);
                        showMyAlert((String) fieldValue);
                    }
                }
            }
        });
    }

    private void showMyAlert(String privacy) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Chuyển chế độ của topic");
        alertDialogBuilder.setMessage(privacy.equals("private") ? "Chuyển sang chế độ công khai" : "Chuyển sang chế độ riêng tư");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Chuyển", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                updatePrivacyForTopic(privacy);
            }
        });
        alertDialogBuilder.setNeutralButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void updatePrivacyForTopic(String privacy){
        Map<String, Object> privacyUpdate = new HashMap<>();
        privacyUpdate.put("privacy", privacy.equals("private") ? "public" : "private");
        DocumentReference collectionRef = firestore.collection("topic").document(userID);
        collectionRef.collection(collection).document(topicID).update(privacyUpdate);
    }
}