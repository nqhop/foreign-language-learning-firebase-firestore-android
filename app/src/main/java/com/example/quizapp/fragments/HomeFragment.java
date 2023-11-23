package com.example.quizapp.fragments;

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

    private
    ArrayList<Topic> topicModels = new ArrayList<>();
    ArrayList<Vocab> vocabModels = new ArrayList<>();
    RecyclerView homeTopicRecycleView, homeVocabRecycleView;
    TopicRepository topicRepository;
    private String keyForder = "forder_name";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//         Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeTopicRecycleView = view.findViewById(R.id.homeTopicRecycleView);
        homeVocabRecycleView = view.findViewById(R.id.homeVocabRecycleView);
        LinearLayoutManager layoutManagerForTopic = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagerForVocab = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        homeTopicRecycleView.setLayoutManager(layoutManagerForTopic);
        homeVocabRecycleView.setLayoutManager(layoutManagerForVocab);

        topicRepository = new TopicRepository();

        // vocab
        setUpHomeVocabModels();

        fetchTopicFromFirestore();
//        fetchVocabFromFirestore();
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
                            topics.put(document.getId(), (String) document.getData().get(keyForder));
                        }
                        setUpHomeTopicModels(topics);
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

    private void setUpHomeTopicModels(Dictionary<String, String> dictionary){
        Enumeration<String> keys = dictionary.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            topicModels.add(new Topic(key, dictionary.get(key)));
        }

        Topic_RecyclerViewAdapter adapter = new Topic_RecyclerViewAdapter(getContext(), topicModels, this);
        homeTopicRecycleView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int posotion) {
        Log.d("onItemClick", "from home frament " + topicModels.get(posotion).getId());
        topicRepository.getVocabsByTopicName(topicModels.get(posotion).getId());
    }
}