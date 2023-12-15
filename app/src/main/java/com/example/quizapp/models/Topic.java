package com.example.quizapp.models;

import java.util.Map;

public class Topic {
    private String id;
    private String name;
    private Map<String, Word> words; // Map để lưu danh sách từ vựng

    public Topic() {

    }

    public Topic(String id, String name, Map<String, Word> words) {
        this.id = id;
        this.name = name;
        this.words = words;
    }

    public Topic(String name, Map<String, Word> words) {
        this.name = name;
        this.words = words;
    }

    public Topic(String path, String forderName) {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Word> getWords() {
        return words;
    }

    public void setWords(Map<String, Word> words) {
        this.words = words;
    }
}
