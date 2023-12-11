package com.example.quizapp.models;

import androidx.annotation.NonNull;

import com.example.quizapp.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;

public class TopicLibrary implements Serializable {
    private String name, userID, topicID;
    private int sizeOfVocabList;
    private User user;
    private boolean isCreated = false;

    public TopicLibrary(String name, String userID, String topicID, int sizeOfVocabList, User user) {
        this.name = name;
        this.userID = userID;
        this.topicID = topicID;
        this.sizeOfVocabList = sizeOfVocabList;
        this.user = user;
        this.isCreated = true;
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

    public boolean isCreated() {
        return isCreated;
    }

    public void setCreated(boolean created) {
        isCreated = created;
    }
}
