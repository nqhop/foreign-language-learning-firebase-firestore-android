package com.example.quizapp.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.quizapp.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TopicRepository {
    private CollectionReference topicCollection;

    public TopicRepository() {
        FirebaseFirestore firestore = FirebaseUtils.getFirestoreInstance();
        topicCollection = firestore.collection("forder");
    }

    public ArrayList<String> getListTopic() {
        ArrayList<String> topics = new ArrayList<>();
        topicCollection
            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()
                        ) {
                            topics.add(document.getData().toString());
                        }
                        Log.d("getListTopic", "OK");
                    } else {
                        Log.d("getListTopic", "Failed");
                    }
                }
            });
        return  topics;
    }

//    asynchronous operation
    public Task<QuerySnapshot> getTopics(){
        return topicCollection.get();
    }
}
