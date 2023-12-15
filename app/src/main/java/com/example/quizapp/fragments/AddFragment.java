package com.example.quizapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.quizapp.R;
import com.example.quizapp.activities.AddTopicActivity;

public class AddFragment extends Fragment {

    public AddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        // Tìm nút trong layout và thiết lập sự kiện click
        View addButton = view.findViewById(R.id.btnAddCourse);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddCourseClick(v);
            }
        });

        return view;
    }

    // Phương thức xử lý khi nút được click
    public void onAddCourseClick(View view) {
        // Chuyển đến AddTopicActivity
        Intent intent = new Intent(getContext(), AddTopicActivity.class);
        startActivity(intent);
    }
}
