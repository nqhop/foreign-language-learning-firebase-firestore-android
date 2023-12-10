package com.example.quizapp.models;


import com.google.firebase.Timestamp;

public class Vocab2 {
    private String word, meaning, status;
    private Timestamp created_at, update_at;
    private boolean star;

    public Vocab2(String word, String meaning, String status, Timestamp created_at, Timestamp update_at, boolean star) {
        this.word = word;
        this.meaning = meaning;
        this.status = status;
        this.created_at = created_at;
        this.update_at = update_at;
        this.star = star;
    }

    public Vocab2(String word, String meaning) {
        this.word = word;
        this.meaning = meaning;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }
}
