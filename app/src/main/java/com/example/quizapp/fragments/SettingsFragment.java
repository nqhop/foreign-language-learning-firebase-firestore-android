package com.example.quizapp.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.SwitchCompat;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.R;
import com.example.quizapp.activities.ChangePasswordActivity;
import com.example.quizapp.activities.LoginActivity;
import com.example.quizapp.databinding.ActivityMainBinding;
import com.example.quizapp.databinding.FragmentSettingsBinding;
import com.example.quizapp.repositories.TopicRepository;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link SettingsFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class SettingsFragment extends Fragment {
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public SettingsFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment SettingsFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static SettingsFragment newInstance(String param1, String param2) {
//        SettingsFragment fragment = new SettingsFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    TopicRepository topicRepository;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        AppCompatActivity activity = (AppCompatActivity) getActivity();
//        activity.getSupportActionBar().show();
//        if (activity != null) {
//            ActionBar actionBar = activity.getSupportActionBar();
//            if (actionBar != null) {
//                actionBar.setTitle("Cài đặt");
//            }
//        }
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_settings, container, false);
//    }
//
////    FragmentSettingsBinding fragmentSettingsBinding;
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
////        fragmentSettingsBinding = FragmentSettingsBinding.inflate(getLayoutInflater());
//        setHasOptionsMenu(true);
//        AppCompatActivity activity = (AppCompatActivity) getActivity();
//        assert activity != null;
//        activity.getSupportActionBar().show();
//        topicRepository = new TopicRepository();
//        super.onViewCreated(view, savedInstanceState);
//    }
//
//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_settings, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        if(id == R.id.addTopic){
//
//            Toast.makeText(getActivity(), "Add topics", Toast.LENGTH_SHORT).show();
//            onAddTopicsClicked();
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void onAddTopicsClicked() {
//        // add dummy topics
//        topicRepository.addDummyTopics();
//    }
//}
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quizapp.R;  // Thay thế bằng tên package thích hợp
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quizapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsFragment extends Fragment {

    private TextView accountTypeTextView;
    private TextView emailTextView;
    private TextView usernameTextView;
    private TextView passwordTextView;
    private TextView notificationsTextView;
    private Switch nightModeSwitch;
    private Switch offlineModeSwitch;
    private TextView supportCenterTextView;
    private TextView introductionTextView;
    private Button logoutButton;
    private TextView userTypeTextView;
    private Button upgradeButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Ánh xạ các thành phần trong giao diện
        accountTypeTextView = view.findViewById(R.id.accountTypeTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        usernameTextView = view.findViewById(R.id.usernameTextView);
        passwordTextView = view.findViewById(R.id.passwordTextView);
        notificationsTextView = view.findViewById(R.id.notificationsTextView);
        nightModeSwitch = view.findViewById(R.id.nightModeSwitch);
        offlineModeSwitch = view.findViewById(R.id.offlineModeSwitch);
        supportCenterTextView = view.findViewById(R.id.supportCenterTextView);
        introductionTextView = view.findViewById(R.id.introductionTextView);
        logoutButton = view.findViewById(R.id.logoutButton);
        userTypeTextView = view.findViewById(R.id.userTypeTextView);
        upgradeButton = view.findViewById(R.id.upgradeButton);

        updateUserInfo();

        logoutButton.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        });

        passwordTextView.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
        });

        nightModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                enableNightMode();
            } else {
                disableNightMode();
            }
        });

        return view;
    }

    private void enableNightMode() {
        SharedPreferences sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("nightModeEnabled", true);
        editor.apply();
    }

    private void disableNightMode() {
        SharedPreferences sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("nightModeEnabled", false);
        editor.apply();
    }

    private void updateUserInfo() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users")
                    .document(userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String fullName = document.getString("fullName");
                                String email = document.getString("email");
                                String userType = document.getString("userType");

                                usernameTextView.setText("Tên người dùng: " + fullName);
                                emailTextView.setText("Email: " + email);
                                accountTypeTextView.setText("Loại tài khoản:" + userType);

                                if ("premium".equals(userType)) {
                                    userTypeTextView.setText(userType);
                                    userTypeTextView.setTextColor(getResources().getColor(R.color.green));
                                } else {
                                    userTypeTextView.setText("regular");
                                    userTypeTextView.setTextColor(getResources().getColor(R.color.red));
                                    upgradeButton.setVisibility(View.VISIBLE);
                                    upgradeButton.setOnClickListener(v -> upgradeToPremium());
                                }
                            }
                        }
                    });
        }
    }

    private void upgradeToPremium() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            db.collection("users")
                    .document(userId)
                    .update("userType", "premium")
                    .addOnSuccessListener(aVoid -> {
                        accountTypeTextView.setText("Loại tài khoản: premium");
                        userTypeTextView.setText("premium");
                        userTypeTextView.setTextColor(getResources().getColor(R.color.green));
                        upgradeButton.setVisibility(View.GONE);
                    })
                    .addOnFailureListener(e -> {
                        // Xử lý lỗi khi cập nhật dữ liệu
                    });
        }
    }
}
