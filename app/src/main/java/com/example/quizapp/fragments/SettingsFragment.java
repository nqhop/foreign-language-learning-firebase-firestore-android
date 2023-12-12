package com.example.quizapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
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
import com.example.quizapp.activities.ChangePasswordActivity;
import com.example.quizapp.activities.LoginActivity;
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
    private TextView notificationsSettingsTextView;

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
        notificationsTextView = view.findViewById(R.id.notificationsTextView);

        setListeners();
        updateUserInfo();

        return view;
    }

    private void setListeners() {
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
        notificationsTextView.setOnClickListener(v -> openNotificationSettings());
    }


    private void enableNightMode() {
        saveNightModeState(true);
    }

    private void disableNightMode() {
        saveNightModeState(false);
    }

    private void saveNightModeState(boolean isEnabled) {
        SharedPreferences sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("nightModeEnabled", isEnabled);
        editor.apply();
    }
    private void openNotificationSettings() {
        Intent intent = new Intent();
        intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", requireActivity().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
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
                            if (document != null && document.exists()) {
                                String fullName = document.getString("fullName");
                                String email = document.getString("email");
                                String userType = document.getString("userType");

                                usernameTextView.setText("Tên người dùng: " + fullName);
                                emailTextView.setText("Email: " + email);
                                accountTypeTextView.setText("Loại tài khoản:" + userType);

                                handleUserType(userType);
                            }
                        }
                    });
        }
    }

    private void handleUserType(String userType) {
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
