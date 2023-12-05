package com.example.quizapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.quizapp.fragments.HomeFragment;
import com.example.quizapp.fragments.LibraryFragment;
import com.example.quizapp.R;
import com.example.quizapp.fragments.SettingsFragment;
import com.example.quizapp.databinding.ActivityMainBinding;
import com.example.quizapp.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.StructuredQuery;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        demoDatabase();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        getSupportActionBar().hide();
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

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
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
        updates.put("age", 12);
        collectionRef.document("zmyQmvdLc3DRhAAxlMOG").update(updates);
    }
}