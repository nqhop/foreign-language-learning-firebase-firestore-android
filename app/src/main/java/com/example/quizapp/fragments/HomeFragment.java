package com.example.quizapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements RecyclerViewInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private FirebaseFirestore firestore;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private
    ArrayList<Topic> topicModels = new ArrayList<>();
    ArrayList<Vocab> vocabModels = new ArrayList<>();
    RecyclerView homeTopicRecycleView, homeVocabRecycleView;
    TopicRepository topicRepository;
    private boolean isFragmentCreated = false;
    private String keyForder = "forder_name";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Fragment", "onCreate");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("Fragment", "onCreateView");
//         Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firestore = FirebaseUtils.getFirestoreInstance();
        Log.d("Fragment", "onViewCreated");
        homeTopicRecycleView = view.findViewById(R.id.homeTopicRecycleView);
        homeVocabRecycleView = view.findViewById(R.id.homeVocabRecycleView);
        LinearLayoutManager layoutManagerForTopic = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagerForVocab = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        homeTopicRecycleView.setLayoutManager(layoutManagerForTopic);
        homeVocabRecycleView.setLayoutManager(layoutManagerForVocab);

        topicRepository = new TopicRepository();

        // vocab
        setUpHomeVocabModels();

//        fetchTopicFromFirestore();
        fetchTopicFromFirestore2();

    }

    private void fetchTopicFromFirestore2(){
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

    private void fetchTopicFromFirestore() {

        topicRepository.getTopics().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        Dictionary<String, String > topics = new Hashtable<>();
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            Log.d("fetchTopicFromFirestore", document.getReference().getPath());
                            Log.d("fetchTopicFromFirestore", "id " + document.getId());
                            topics.put(document.getReference().getPath(), (String) document.getData().get(keyForder));
                        }
//                        setUpHomeTopicModels(topics);
                    }
                }
            }
        });
    }

    private void fetchVocabFromFirestore(){
        topicRepository.getVocabsByTopicName();
    }

    private void setUpHomeVocabModels() {
        String[] homeVocabsNameItem = getResources().getStringArray(R.array.vocabs);
        for(String vocab : homeVocabsNameItem){
            vocabModels.add(new Vocab(vocab));
            Vocab_RecyclerViewAdapter vocabAdapter = new Vocab_RecyclerViewAdapter(getContext(), vocabModels);
            homeVocabRecycleView.setAdapter(vocabAdapter);
        }
    }

//    private void setUpHomeTopicModels(Dictionary<String, String> dictionary){
    private void setUpHomeTopicModels(List<Topic> topicsopics){

        topicsopics.forEach(topic -> topicModels.add(topic));
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