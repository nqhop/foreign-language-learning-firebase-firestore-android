package com.example.quizapp.repositories;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.quizapp.models.Topic;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class TopicRepository {
    public interface FirestoreCallback<T> {
        void onSuccess(T result);
        void onFailure(String errorMessage);
    }
    public static void getTopics(Context context, String collectionName, FirestoreCallback<List<Topic>> callback) {
        FirestoreAsyncTask asyncTask = new FirestoreAsyncTask(context, callback);
        asyncTask.execute(collectionName);
    }
    private static class FirestoreAsyncTask extends AsyncTask<String, Void, List<Topic>> {

        private FirestoreCallback<List<Topic>> callback;
        private Context context;
        ProgressDialog progressDialog;
        FirebaseFirestore firestore = FirebaseUtils.getFirestoreInstance();



        public FirestoreAsyncTask(Context context, FirestoreCallback<List<Topic>> callback) {
            this.callback = callback;
            this.context = context;
        }

        @Override
        protected List<Topic> doInBackground(String... params) {
            Log.d("AsyncTask", "doInBackground");
            String collectionName = params[0];
            CollectionReference topicCollection = firestore.collection(collectionName);



            try {
                List<Topic> topics = new ArrayList<>();
                CountDownLatch latch = new CountDownLatch(1);
                topicCollection
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()
                                ) {
                                    topics.add(new Topic(document.getReference().getPath(), (String) document.getData().get("forder_name")));
                                }
                                Log.d("AsyncTask", "OK");
                                latch.countDown();
                            } else {
                                Log.d("AsyncTask", "Failed");
                                latch.countDown();
                            }
                        }
                    });
                Log.d("AsyncTask", "onCompleteListener");
                latch.await();
                return topics;
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(context,
                    "ProgressDialog",
                    "Loading");
        }

        @Override
        protected void onPostExecute(List<Topic> documentSnapshots) {
            progressDialog.dismiss();
            Log.d("AsyncTask", "onPostExecute");
            if (documentSnapshots != null) {
                callback.onSuccess(documentSnapshots);
            } else {
                callback.onFailure("Failed to retrieve data from Firestore.");
            }
        }
    }




    private CollectionReference topicCollection, topicSelected;
    private FirebaseFirestore firestore;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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

    //    asynchronous operation
    //from 01/12/23, this function no longer used
    public Task<QuerySnapshot> getVocabsByTopicName(String path) {
        String documentPath = "/forder/" + path;

        DocumentReference documentRef = firestore.document(documentPath);

        CollectionReference collectionRef = documentRef.collection("topic");

        collectionRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                        Log.d("onItemClick", "subDocumentData " +  documentSnapshot.getData().get("name"));
                    }
                }
            }
        });
        return null;
    }

    public void addDummyTopics() {
        CollectionReference topicsReference =  firestore.collection("mydemo");
        topicsReference.add(new Topic("1", "name"));
    }

    // quite cumbersome, so I don't use this function
    public List<Vocab> getListVocab(String path) throws InterruptedException {
        Log.d("getListVocab", "from: " + path);

        // below query just for test
        DocumentReference docRef = db.document(path);
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Document exists, retrieve its data
                            Map<String, Object> data = documentSnapshot.getData();
                            // Process the retrieved data
                            Log.d("getListVocab", (String) data.get("forder_name"));
                        } else {
                            // Document doesn't exist

                        }
                    }
                });

        try {
            List<Vocab> vocabs = new ArrayList<>();
            CountDownLatch latch = new CountDownLatch(1);
            firestore.collection(path + "/topic").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("getListVocab", (String) document.getData().get("name"));
                            Log.d("getListVocab", (String) document.getReference().getPath());
                            vocabs.add(new Vocab((String) document.getData().get("name"), document.getReference().getPath()));
                        }
                        latch.countDown();
                    } else {
                        latch.countDown();
                    }

                }
            });
            latch.wait();
            return vocabs;
        } catch (InterruptedException e) {
            return null;
        } catch (Exception e) {
            Log.d("getListVocab", "Exception " + e.toString());
            return null;
        }
    }
}
