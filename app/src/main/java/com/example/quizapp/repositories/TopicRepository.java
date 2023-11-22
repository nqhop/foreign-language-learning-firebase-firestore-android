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

    public ArrayList<String> getListTopic(String topic) {
        topicCollection.whereEqualTo("forder_name", topic)
            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()
                        ) {
                            Log.d("fetchData", document.getId() + "->" + document.getData());
                        }
                    } else {
                        Log.d("fetchData", "Failed");
                    }
                }
            });
        return  null;
    }

    public void test(){
        topicCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document:task.getResult()
                    ) {
                        Log.d("fetchData", document.getId() + "->"+document.getData());
                    }
                }else{
                    Log.d("fetchData", "Failed");
                }
            }
        });
    }
}
