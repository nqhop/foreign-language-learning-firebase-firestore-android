package com.example.quizapp.utils;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ForderUtil {
    private String name;

    public ForderUtil() {
    }

    public ForderUtil(String name) {
        this.name = name;
    }

    public static final String FIELD_CITY = "city";
    public static final String FIELD_CATEGORY = "category";
    public static final String FIELD_PRICE = "price";
    public static final String FIELD_POPULARITY = "numRatings";
    public static final String FIELD_AVG_RATING = "avgRating";
}
