package com.example.quizapp.models;

import com.example.quizapp.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class TopicLibrary {
    private String name, userID, topicID;
    private int sizeOfVocabList;
    private User user;

    public TopicLibrary(String name, String userID, String topicID, int sizeOfVocabList, User user) {
        this.name = name;
        this.userID = userID;
        this.topicID = topicID;
        this.sizeOfVocabList = sizeOfVocabList;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTopicID() {
        return topicID;
    }

    public void setTopicID(String topicID) {
        this.topicID = topicID;
    }

    public int getsizeOfVocabList() {
        return sizeOfVocabList;
    }

    public void setsizeOfVocabList(int sizeOfVocabList) {
        this.sizeOfVocabList = sizeOfVocabList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
