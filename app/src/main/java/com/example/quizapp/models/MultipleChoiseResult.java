package com.example.quizapp.models;

public class MultipleChoiseResult {
    private String question, correctOption, wrongOption;
    private boolean EnglishIsSelected;

    public MultipleChoiseResult(Boolean EnglishIsSelected, String question, String correctOption, String wrongOption) {
        this.question = question;
        this.EnglishIsSelected = EnglishIsSelected;
        this.correctOption = correctOption;
        this.wrongOption = wrongOption;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    public String getWrongOption() {
        return wrongOption;
    }

    public void setWrongOption(String wrongOption) {
        this.wrongOption = wrongOption;
    }

    public boolean isEnglishIsSelected() {
        return EnglishIsSelected;
    }

    public void setEnglishIsSelected(boolean englishIsSelected) {
        EnglishIsSelected = englishIsSelected;
    }
}
