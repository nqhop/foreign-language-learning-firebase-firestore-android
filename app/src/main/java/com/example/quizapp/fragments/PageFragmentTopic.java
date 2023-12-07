package com.example.quizapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.adapters.LibraryTopicAdapter;
import com.example.quizapp.models.TopicLibrary;
import com.example.quizapp.models.User;
import com.example.quizapp.models.Vocab;
import com.example.quizapp.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class PageFragmentTopic extends Fragment {

    FirebaseFirestore firestore;
    RecyclerView fragmentPageTopicRecyclerView;
    final String id_user = "AaWZ5yEnedL7al8jRhH9";
    final String userId2 = "VGC9WUC8CPdakSl38JBkDJPLz1r2";
    int totalCount = 0;
    ArrayList<TopicLibrary> myTopicLibraries;
    public PageFragmentTopic() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("Library", "PageFragmentTopic");
        firestore = FirebaseUtils.getFirestoreInstance();
        View rootView = inflater.inflate(R.layout.fragment_page_topic, container, false);
        return inflater.inflate(R.layout.fragment_page_topic, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentPageTopicRecyclerView = view.findViewById(R.id.fragmentPageTopicRecyclerView);
        myTopicLibraries = new ArrayList<>();

//        myTopicLibraries.add(new TopicLibrary("name", id_user, "001", 1, new User("fullName", "email", id_user)));
        getTopicSavedByUser(id_user, 0, 0);
    }

    private void getTopicSavedByUser(String userID, int total, int count){
        DocumentReference collectionRef = firestore.collection("topic").document(userID);
        collectionRef.collection("topicCreated").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                int index = 0;
                for(DocumentSnapshot document : documents){
                    index++;
                    String name = (String) document.getData().get("name");
                    String id = document.getId();
                    int finalIndex = index;
                    document.getReference().collection("vocab").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                totalCount++;

                                // check get all topic (created and saved)
                                boolean isGetAllTopic = userID != id_user && total == count && finalIndex == documents.size();
                                getUserAndAddToList(userID, id, name, task.getResult().size(), isGetAllTopic);
                            }
                        }
                    });
                }
            }
        });
        if(userID == id_user){
            collectionRef.collection("topicSaved").document("001").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    ArrayList<String> userIDList = (ArrayList<String>) documentSnapshot.getData().get("userID");
                    Log.d("PageFragmentTopic", "--------saved size----------" + userIDList.size());
                    for(int i = 0; i < userIDList.size(); i++){
                        Log.d("PageFragmentTopic", "...--> " + userIDList.get(i));
                        getTopicSavedByUser(userIDList.get(i), userIDList.size() - 1, i);
                    }
                }
            });
        }
    }
    private void getUserAndAddToList(String userID, String topicID, String name, int size, boolean isGetAllTopic){
        firestore.collection("users").document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                myTopicLibraries.add(new TopicLibrary(name, userID, topicID, size, new User((String) documentSnapshot.getData().get("fullName"), (String) documentSnapshot.getData().get("email"), documentSnapshot.getId())));

                if(isGetAllTopic){
                    updateUI();
                    Log.d("PageFragmentTopic", "********** myTopicLibraries: " + myTopicLibraries.size());
                }
            }
        });
    }

    private void updateUI(){
        Log.d("updateUI", "size: " + myTopicLibraries.size());
        LibraryTopicAdapter adapter = new LibraryTopicAdapter(getContext(), myTopicLibraries);
        fragmentPageTopicRecyclerView.setAdapter(adapter);
        fragmentPageTopicRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}