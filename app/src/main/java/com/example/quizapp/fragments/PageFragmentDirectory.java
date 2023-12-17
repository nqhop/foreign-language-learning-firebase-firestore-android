package com.example.quizapp.fragments;

        import android.content.Context;
        import android.os.Bundle;
        import android.os.CountDownTimer;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.fragment.app.Fragment;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import com.example.quizapp.R;
        import com.example.quizapp.adapters.LibraryDirectoryAdapter;
        import com.example.quizapp.models.TopicDirectory;
        import com.example.quizapp.models.User;
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
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;
        import java.util.concurrent.CountDownLatch;

public class PageFragmentDirectory extends Fragment {
    FirebaseFirestore firestore;
    RecyclerView fragmentPageDirectoryRecyclerView;
    ArrayList<TopicDirectory> myDirectories;
    final String id_user = "AaWZ5yEnedL7al8jRhH9";
    User user;

    public PageFragmentDirectory() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("Library", "PageFragmentDirectory");

        View rootView = inflater.inflate(R.layout.fragment_page_directory, container, false);
        firestore = FirebaseUtils.getFirestoreInstance();
        fragmentPageDirectoryRecyclerView = rootView.findViewById(R.id.fragmentPageDirectoryRecyclerView);

        return inflater.inflate(R.layout.fragment_page_directory, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DocumentReference collectionRef = firestore.collection("Users").document(id_user);
        collectionRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    user = new User((String) documentSnapshot.getData().get("fullName"), (String) documentSnapshot.getData().get("email"), id_user);
                    Log.d("PageFragmentDirectory", user.getName());
                    fetchDerectory(view.getContext());
                }
            }
        });

        myDirectories = new ArrayList<>();
        fragmentPageDirectoryRecyclerView = view.findViewById(R.id.fragmentPageDirectoryRecyclerView);
    }

    private void fetchDerectory(Context c) {
        String collectionName = "forder";
        DocumentReference collectionRef = firestore.collection("forder").document(id_user);
        collectionRef.collection("topicID").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot document : documents) {
                    String name = (String) document.getData().get("name");
                    Log.d("Firestore", "name -> " + name);
                    myDirectories.add(new TopicDirectory(name, user));
                }
                LibraryDirectoryAdapter libraryDirectoryAdapter = new LibraryDirectoryAdapter(getContext(), myDirectories);
                fragmentPageDirectoryRecyclerView.setAdapter(libraryDirectoryAdapter);
                fragmentPageDirectoryRecyclerView.setLayoutManager(new LinearLayoutManager(c));
            }
        });
    }
}