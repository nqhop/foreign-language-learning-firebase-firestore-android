package com.example.quizapp.models;

public class Word {
    private String english;
    private String vietnamese;
    private String description;
    private String image;

    public Word() {
        // Constructor rỗng cần thiết để sử dụng với Firestore
    }

    public Word(String english, String vietnamese, String description, String image) {
        this.english = english;
        this.vietnamese = vietnamese;
        this.description = description;
        this.image = image;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getVietnamese() {
        return vietnamese;
    }

    public void setVietnamese(String vietnamese) {
        this.vietnamese = vietnamese;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
