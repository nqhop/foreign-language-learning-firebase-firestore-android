package com.example.quizapp.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseUtils {
    private static FirebaseFirestore firestore;

    public static FirebaseFirestore getFirestoreInstance() {
        if (firestore == null) {
            firestore = FirebaseFirestore.getInstance();
        }
        return firestore;
    }
}
