package com.example.quizapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.quizapp.R;
import com.example.quizapp.models.Vocab;
import com.example.quizapp.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class PageFragmentTopic extends Fragment {

    FirebaseFirestore firestore;
    final String id_user = "AaWZ5yEnedL7al8jRhH9";
    public PageFragmentTopic() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("Library", "PageFragmentTopic");
        firestore = FirebaseUtils.getFirestoreInstance();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_page_topic, container, false);
    }
}