package com.example.quizapp.fragments;

        import android.content.Context;
        import android.os.Bundle;
        import android.os.CountDownTimer;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import androidx.annotation.NonNull;
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
        myDirectories = new ArrayList<>();
        fragmentPageDirectoryRecyclerView = rootView.findViewById(R.id.fragmentPageDirectoryRecyclerView);
        myDirectories.add(new TopicDirectory("foods" , new User("hop", "hop@gamil.com", "123")));
        myDirectories.add(new TopicDirectory("foods" , new User("hop", "hop@gamil.com", "123")));

        LibraryDirectoryAdapter libraryDirectoryAdapter = new LibraryDirectoryAdapter(getContext(), myDirectories);
        fragmentPageDirectoryRecyclerView.setAdapter(libraryDirectoryAdapter);
        fragmentPageDirectoryRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));



        firestore = FirebaseUtils.getFirestoreInstance();

        DocumentReference collectionRef = firestore.collection("users").document(id_user);
        collectionRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    user = new User((String) documentSnapshot.getData().get("fullName"), (String) documentSnapshot.getData().get("email"), id_user);
                    Log.d("GetUserInfo", user.toString());
//                    fetchDerectory(rootView.getContext());
                }
            }
        });



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_page_directory, container, false);
    }

    private void fetchDerectory(Context c) {
        CollectionReference collectionRef = firestore.collection("forder");
        collectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot document : documents) {
                    String documentName = (String) document.getData().get("forder_name");
                    String documentUserId = (String) document.getData().get("user_id");
                    if(documentUserId != null){
                        myDirectories.add(new TopicDirectory(documentName, user));
                        Log.d("dataUser", "documentName " + documentName);
                        Log.d("dataUser","documentUserId " + documentUserId);
                        Log.d("dataUser","user name " + user.getName());
                    }
                }
                Log.d("dataUser","myDirectories size " + myDirectories.size());
                Log.d("dataUser","user name " + user.getName());

//                LibraryDirectoryAdapter libraryDirectoryAdapter = new LibraryDirectoryAdapter(getContext(), myDirectories);
//                fragmentPageDirectoryRecyclerView.setAdapter(libraryDirectoryAdapter);
//                fragmentPageDirectoryRecyclerView.setLayoutManager(new LinearLayoutManager(c));
            }
        });
    }
}