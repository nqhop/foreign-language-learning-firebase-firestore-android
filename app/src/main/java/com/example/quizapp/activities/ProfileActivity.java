package com.example.quizapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestoreDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firestoreDB = FirebaseFirestore.getInstance();

        // Hiển thị thông tin người dùng
        initUserProfile();
    }

    private void initUserProfile() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            // Lấy ID người dùng hiện tại
            String userId = currentUser.getUid();

            // Thực hiện truy vấn đến tài liệu người dùng trong Firestore
            DocumentReference userRef = firestoreDB.collection("users").document(userId);

            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        // Lấy thông tin từ tài liệu Firestore
                        String fullName = document.getString("fullName");
                        String email = document.getString("email");

                        // Hiển thị thông tin người dùng trên giao diện
                        TextView textViewName = findViewById(R.id.textView2);
                        TextView textViewEmail = findViewById(R.id.textView3);

                        textViewName.setText(fullName);
                        textViewEmail.setText(email);
                    } else {
                        Log.d("ProfileActivity", "No such document");
                    }
                } else {
                    Log.d("ProfileActivity", "get failed with ", task.getException());
                }
            });
        }
    }
}
