package com.example.quizapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.quizapp.fragments.HomeFragment;
import com.example.quizapp.fragments.LibraryFragment;
import com.example.quizapp.R;
import com.example.quizapp.fragments.SettingsFragment;
import com.example.quizapp.databinding.ActivityMainBinding;
import com.example.quizapp.models.TopicLibrary;
import com.example.quizapp.models.User;
import com.example.quizapp.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    String userId1 = "AaWZ5yEnedL7al8jRhH9";
    String userId2 = "VGC9WUC8CPdakSl38JBkDJPLz1r2";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //test fashcard
//        startActivity(new Intent(this, VocabActivity.class));

        // test multiple choise
//        startActivity(new Intent(this, multipleChoiceTestActivity.class));

        applyNightMode();
        demoDatabase();
//        deleteVocab(userId1, "001");
//        deleteVocab(userId1, "002");
//        deleteVocab(userId2, "001");
//        deleteVocab(userId2, "002");
//        createTopicExamble();

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(R.layout.activity_main);
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());


//        SharedPreferences pref = getSharedPreferences("MyPref", MODE_PRIVATE);
//        if (!pref.contains("email")) {
//            startActivity(new Intent(MainActivity.this, LoginActivity.class));
//            finish();
//        }


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.home){
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.library) {
                replaceFragment(new LibraryFragment());
            } else if (item.getItemId() == R.id.settings) {
                replaceFragment(new SettingsFragment());

            }
            return true;
        });
    }


    private void applyNightMode() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        boolean nightModeEnabled = sharedPreferences.getBoolean("nightModeEnabled", false);

        if (nightModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_main, fragment);
        fragmentTransaction.commit();
    }
    public void onAvatarClick(View view) {
        // Chuyển đến trang activity_profile
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
    private void checkLoginStatus() {
        SharedPreferences pref = getSharedPreferences("MyPref", MODE_PRIVATE);
        if (!pref.contains("email")) {
            // Nếu chưa đăng nhập, chuyển đến LoginActivity
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }

    // Hop's demo
    private void demoDatabase() {
        FirebaseFirestore firestore = FirebaseUtils.getFirestoreInstance();
        CollectionReference collectionRef = firestore.collection("usersDemo");

        Map<String, Object> updates = new HashMap<>();
        updates.put("age", 15);
        collectionRef.document("zmyQmvdLc3DRhAAxlMOG").update(updates);
        collectionRef.document("demo").set(updates);


        firestore.collection("Users").document(userId1).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
//                        String fieldValue = document.getString("email");
                        Log.d("demoDatabase ", document.getData().toString());
                    } else {
                        // Handle the case where the document does not exist
                    }
                } else {
                    // Handle exceptions or errors that occurred while retrieving the document
                }
            }
        });
        firestore.collection("Users").document(userId1).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("demoDatabase", " documentSnapshot: " + documentSnapshot.getData());
            }
        });
    }

    private void deleteVocab(String userID, String topicCreatedId){
        FirebaseFirestore firestore = FirebaseUtils.getFirestoreInstance();
        firestore.collection("topic").document(userID).collection("topicCreated").document(topicCreatedId).collection("vocab").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    WriteBatch batch = firestore.batch();
                    for(DocumentSnapshot documentSnapshot : task.getResult()) {
                        batch.delete(documentSnapshot.getReference());
                    }
                    batch.commit();
                    Log.d("deleteVocab", "onSuccess");
                }
            }
        });
    }

    private void createTopicExamble(){
        Map<String, Object> topicInfo = new HashMap<>();
        topicInfo.put("name", "vegatables");
        FirebaseFirestore firestore = FirebaseUtils.getFirestoreInstance();
        CollectionReference collectionRef = firestore.collection("topic");
        collectionRef.document(userId1).collection("topicCreated").document("001").set(topicInfo);

//        https://engbreaking.com/trai-cay-tieng-anh/?psafe_param=1&utm_source=google&utm_medium=cpc&utm_campaign=17-goga-viet-an-bui-performance-max-purchase-vnd&utm_term=&utm_content=&gad_source=1&gclid=Cj0KCQiA4NWrBhD-ARIsAFCKwWuJLiEMU-y3spyyeymLRcjxFwjhlAYaIQW3w31n9S7ZaNMAum64eW4aAjT6EALw_wcB
        String[] words = {"Ambarella", "Apple", "Apricot", "Avocado", "Banana", "Strawberry", "Cantaloupe"};
        String[] vietNamesesMeaning = {"cóc", "táo", "mơ", "bơ", "chuối", "dâu tây", "Dưa vàng"};
        for(int i = 0; i < words.length; i++){
            Map<String, Object> vocab = new HashMap<>();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vocab.put("created_at", Timestamp.from(Instant.now()));
                vocab.put("update_at", Timestamp.from(Instant.now()));
            }
            vocab.put("star", false);
            vocab.put("status", "new");

            vocab.put("vietnameses_meaning", vietNamesesMeaning[i]);
            vocab.put("word", words[i].toLowerCase());
            collectionRef.document(userId1).collection("topicCreated").document("001").collection("vocab").add(vocab);
        }

        Map<String, Object> topicInfo2 = new HashMap<>();
        topicInfo2.put("name", "foods");
        collectionRef.document(userId1).collection("topicCreated").document("002").set(topicInfo2);
        String[] foodsVocab = {"Cheeseburger", "Chicken nuggets", "Chili sauce", "Chips", "Donut"};
        String[] foodsVocabMeaning = {"bánh mỳ kẹp", "gà viên chiên", "tương ớt", " khoai tây chiên", "bánh vòng"};
        for(int i = 0; i < foodsVocab.length; i++){
            Map<String, Object> vocab = new HashMap<>();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vocab.put("created_at", Timestamp.from(Instant.now()));
                vocab.put("update_at", Timestamp.from(Instant.now()));
            }
            vocab.put("star", false);
            vocab.put("status", "new");

            vocab.put("vietnameses_meaning", foodsVocabMeaning[i]);
            vocab.put("word", foodsVocab[i].toLowerCase());
            collectionRef.document(userId1).collection("topicCreated").document("002").collection("vocab").add(vocab);
        }



        Map<String, Object> topicInfoForUser2 = new HashMap<>();
        topicInfoForUser2.put("name", "jobs 2");
        collectionRef.document(userId2).collection("topicCreated").document("001").set(topicInfoForUser2);
        for(int i = 0; i < 2; i++){
            Map<String, Object> vocab = new HashMap<>();
            vocab.put("created_at", "Dec 7, 2023");
            vocab.put("speech", "");
            vocab.put("star", "");
            vocab.put("status", "");
            vocab.put("update_at", "Dec 7, 2023");
            vocab.put("vietnameses_meaning", "");
            vocab.put("word", "");
            collectionRef.document(userId2).collection("topicCreated").document("001").collection("vocab").add(vocab);
        }


        Map<String, Object> topicInfoForUser2_2 = new HashMap<>();
        topicInfoForUser2_2.put("name", "works 2");
        collectionRef.document(userId2).collection("topicCreated").document("002").set(topicInfoForUser2_2);
        for(int i = 0; i < 2; i++){
            Map<String, Object> vocab = new HashMap<>();
            vocab.put("created_at", "Dec 7, 2023");
            vocab.put("speech", "");
            vocab.put("star", "");
            vocab.put("status", "");
            vocab.put("update_at", "Dec 7, 2023");
            vocab.put("vietnameses_meaning", "");
            vocab.put("word", "");
            collectionRef.document(userId2).collection("topicCreated").document("002").collection("vocab").add(vocab);
        }

        // add topic saved

        List<String> topicSaved = new ArrayList<>();
        topicSaved.add(userId2);
        Map<String, Object> topicSavedMap = new HashMap<>();
        topicSavedMap.put("userID", topicSaved);
        collectionRef.document(userId1).collection("topicSaved").document("001").set(topicSavedMap);
        createforderExamble();
    }

    public void createforderExamble(){
        FirebaseFirestore firestore = FirebaseUtils.getFirestoreInstance();
        CollectionReference collectionRef = firestore.collection("forder");
        Map<String, Object> forderInfo = new HashMap<>();
        forderInfo.put("forder_name", "java");
        collectionRef.document(userId1).set(forderInfo);
        // userUd1 saved topic of userId2
        List<String> topicAdded = new ArrayList<>();
        topicAdded.add("001");
        topicAdded.add("002");
        Map<String, Object> topicAddedMap = new HashMap<>();
        topicAddedMap.put("topicID", topicAdded);
        collectionRef.document(userId1).collection(userId2).document("topicSaved001").set(topicAddedMap);
    }
}