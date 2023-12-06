package com.example.quizapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quizapp.R;
import com.example.quizapp.utils.FirebaseUtils;
import com.google.firebase.firestore.FirebaseFirestore;

public class PageFragmentClasses  extends Fragment {

    FirebaseFirestore firestore;
    ListView simpleList;
    TextView textView;
    final String id_user = "AaWZ5yEnedL7al8jRhH9";
    public PageFragmentClasses() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("Library", "PageFragmentClasses");
        firestore = FirebaseUtils.getFirestoreInstance();
        View rootView = inflater.inflate(R.layout.fragment_page_classes, container, false);
//        textView =(TextView) rootView.findViewById(R.id.textView15);
//        textView.setText("My classes");

        return inflater.inflate(R.layout.fragment_page_classes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView =(TextView) view.findViewById(R.id.textView15);
        textView.setText("My classes");
    }
}