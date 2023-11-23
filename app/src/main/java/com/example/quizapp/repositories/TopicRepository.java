package com.example.quizapp.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.quizapp.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TopicRepository {
    private CollectionReference topicCollection;
    private FirebaseFirestore firestore;

    public TopicRepository() {
        firestore = FirebaseUtils.getFirestoreInstance();
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

    public void getVocabsByTopicName() {
        Query subcollectionQuery = topicCollection.whereEqualTo("forder_name", "foods");
        subcollectionQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {

                        Log.d("getVocabsByTopicName", "id: " +  document.getId());
                        Log.d("getVocabsByTopicName", document.getData().toString());
                        // Process each document in the subcollection
                        String documentId = document.getId();
                        // Extract desired fields from the document
                        String fieldValue = document.getString("field_name");
                        // ...
                    }
                } else {
                    // Subcollection is empty or no documents match the condition
                }
            } else {
                // Error retrieving subcollection
            }
        });
//        Log.d("getVocabsByTopicName", topicCollection.get().toString());
    }
    public void getVocabsByTopicName(String path) {


    }
}
