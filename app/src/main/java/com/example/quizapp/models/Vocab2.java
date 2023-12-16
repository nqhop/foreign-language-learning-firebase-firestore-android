package com.example.quizapp.models;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

public class Vocab2 implements Parcelable {
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

    protected Vocab2(Parcel in) {
        word = in.readString();
        meaning = in.readString();
        status = in.readString();
        created_at = in.readParcelable(Timestamp.class.getClassLoader());
        update_at = in.readParcelable(Timestamp.class.getClassLoader());
        star = in.readByte() != 0;
    }

    public static final Creator<Vocab2> CREATOR = new Creator<Vocab2>() {
        @Override
        public Vocab2 createFromParcel(Parcel in) {
            return new Vocab2(in);
        }

        @Override
        public Vocab2[] newArray(int size) {
            return new Vocab2[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(word);
        dest.writeString(meaning);
        dest.writeString(status);
        dest.writeParcelable(created_at, flags);
        dest.writeParcelable(update_at, flags);
        dest.writeByte((byte) (star ? 1 : 0));
    }
}
