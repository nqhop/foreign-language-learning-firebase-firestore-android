package com.example.quizapp.models;

public class Vocab {
    String name, id;

    public Vocab(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public Vocab(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
