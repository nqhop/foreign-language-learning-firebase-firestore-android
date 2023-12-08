package com.example.quizapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.RecyclerViewInterface;
import com.example.quizapp.adapters.Topic_RecyclerViewAdapter;
import com.example.quizapp.adapters.Vocab_RecyclerViewAdapter;
import com.example.quizapp.models.Topic;
import com.example.quizapp.models.Vocab;
import com.example.quizapp.repositories.TopicRepository;
import com.example.quizapp.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements RecyclerViewInterface {

    private TextView welcomeTextView;
    private TextView usernameTextView;

    private FirebaseFirestore firestore;
    private
    ArrayList<Topic> topicModels = new ArrayList<>();
    ArrayList<Vocab> vocabModels = new ArrayList<>();
    RecyclerView homeTopicRecycleView, homeVocabRecycleView;
    TopicRepository topicRepository;
    private String keyForder = "forder_name";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Fragment", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Fragment", "onCreateView");
        // Hide the action bar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.getSupportActionBar().hide();
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        welcomeTextView = view.findViewById(R.id.textView7);
        usernameTextView = view.findViewById(R.id.textView8);

        // Update user info
        updateUserInfo();

        homeTopicRecycleView = view.findViewById(R.id.homeTopicRecycleView);
        homeVocabRecycleView = view.findViewById(R.id.homeVocabRecycleView);

        LinearLayoutManager layoutManagerForTopic = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagerForVocab = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        homeTopicRecycleView.setLayoutManager(layoutManagerForTopic);
        homeVocabRecycleView.setLayoutManager(layoutManagerForVocab);

        topicRepository = new TopicRepository();

        setUpHomeVocabModels();
        fetchTopicFromFirestore2();

        return view;
    }

    private void updateUserInfo() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String fullName = currentUser.getDisplayName();
            String email = currentUser.getEmail();

            Log.d("updateUserInfo", "fullName: " + fullName);
            Log.d("updateUserInfo", "email: " + email);

            String welcomeMessage = "Welcome, " + email + "!";
            welcomeTextView.setText(welcomeMessage);
            usernameTextView.setText(fullName);
        } else {
            welcomeTextView.setText("Welcome!");
            usernameTextView.setText("Guest");
        }
    }


    private void fetchTopicFromFirestore2() {
        TopicRepository.getTopics(getContext(), "forder", new TopicRepository.FirestoreCallback<List<Topic>>() {
            @Override
            public void onSuccess(List<Topic> result) {
                Log.d("AsyncTask", "onSuccessL " + result.size());
                setUpHomeTopicModels(result);
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("AsyncTask", errorMessage);
            }
        });
    }

    private void setUpHomeVocabModels() {
        String[] homeVocabsNameItem = getResources().getStringArray(R.array.vocabs);
        for (String vocab : homeVocabsNameItem) {
            vocabModels.add(new Vocab(vocab));
            Vocab_RecyclerViewAdapter vocabAdapter = new Vocab_RecyclerViewAdapter(getContext(), vocabModels);
            homeVocabRecycleView.setAdapter(vocabAdapter);
        }
    }

    private void setUpHomeTopicModels(List<Topic> topics) {
        topicModels.addAll(topics);
        Topic_RecyclerViewAdapter adapter = new Topic_RecyclerViewAdapter(getContext(), topicModels, this);
        homeTopicRecycleView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int posotion) {
        String path = topicModels.get(posotion).getId();
        Log.d("getListVocab", "from home frament " + topicModels.get(posotion).getId());

        vocabModels.clear();
        firestore.collection(path + "/topic").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String vocabPath = document.getReference().getPath() + "vocab";
                        String vocabName = document.getData().toString();
                        Log.d("getListVocab", vocabPath);
                        Log.d("getListVocab", vocabName);

                        vocabModels.add(new Vocab((String) document.getData().get("name"), document.getReference().getPath()));
                    }
                    Vocab_RecyclerViewAdapter vocabAdapter = new Vocab_RecyclerViewAdapter(getContext(), vocabModels);
                    homeVocabRecycleView.setAdapter(vocabAdapter);

                }
            }
        });
    }
}
