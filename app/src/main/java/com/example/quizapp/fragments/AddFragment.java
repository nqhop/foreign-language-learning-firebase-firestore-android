package com.example.quizapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quizapp.R;
import com.example.quizapp.activities.AddTopicActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddFragment extends Fragment {

    public AddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        // Tìm nút trong layout và thiết lập sự kiện click
        View addButton = view.findViewById(R.id.btnAddCourse);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddCourseClick(v);
            }
        });

        View addForderTutton = view.findViewById(R.id.btnAddFolder);
        addForderTutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddDirectoryClick(v);
            }
        });

        return view;
    }

    private void onAddDirectoryClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Tên thư mục");

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_input, null);

        EditText editText = dialogView.findViewById(R.id.edit_text_input);
        builder.setView(dialogView);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String inputText = editText.getText().toString();
                addDirectoryToFireStore(inputText);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

// Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addDirectoryToFireStore(String directoryName) {
        String userID = "AaWZ5yEnedL7al8jRhH9";

        String subcollectionName = UUID.randomUUID().toString();
        Map<String, Object> data = new HashMap<>();
        List<String> topicAdded = new ArrayList<>();
        data.put("name", directoryName);
        data.put("topic", topicAdded);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firestore.collection("forder").document(userID).collection("topicID");
        collectionReference.document(subcollectionName).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Some thing went wrong", Toast.LENGTH_SHORT).show();
                }
            });


//        CollectionReference subcollectionRef = documentRef.collection(subcollectionName);
//
//        subcollectionRef.add(data)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getContext(), "Some thing went wrong", Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

    public void onAddCourseClick(View view) {
        // Chuyển đến AddTopicActivity
        Intent intent = new Intent(getContext(), AddTopicActivity.class);
        startActivity(intent);
    }
}
