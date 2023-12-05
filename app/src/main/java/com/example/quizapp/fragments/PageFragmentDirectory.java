package com.example.quizapp.fragments;

        import android.os.Bundle;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import androidx.annotation.NonNull;
        import androidx.fragment.app.Fragment;

        import com.example.quizapp.R;
        import com.example.quizapp.utils.FirebaseUtils;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.firestore.CollectionReference;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.google.firebase.firestore.Query;
        import com.google.firebase.firestore.QueryDocumentSnapshot;
        import com.google.firebase.firestore.QuerySnapshot;

public class PageFragmentDirectory extends Fragment {
    FirebaseFirestore firestore;
    final String id_user = "AaWZ5yEnedL7al8jRhH9";

    public PageFragmentDirectory() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        firestore = FirebaseUtils.getFirestoreInstance();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_page_directory, container, false);
    }

    private void fetchDerectory() {
        CollectionReference collectionRef = firestore.collection("forder");
        Query query = collectionRef.whereEqualTo("id_user", id_user);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("PageFragmentTopic","name: " + document.getData().get("forder_name"));
                    }
                }
            }
        });
    }
}