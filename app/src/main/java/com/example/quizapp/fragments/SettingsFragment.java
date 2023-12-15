package com.example.quizapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.quizapp.R;
import com.example.quizapp.activities.ChangePasswordActivity;
import com.example.quizapp.activities.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private TextView accountTypeTextView, emailTextView, usernameTextView, passwordTextView;
    private Button upgradeButton, logoutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        accountTypeTextView = view.findViewById(R.id.accountTypeTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        usernameTextView = view.findViewById(R.id.usernameTextView);
        passwordTextView = view.findViewById(R.id.passwordTextView);
        upgradeButton = view.findViewById(R.id.upgradeButton);
        logoutButton = view.findViewById(R.id.logoutButton);

        // Lấy thông tin người dùng từ Firestore
        loadUserData();

        // Thêm sự kiện click cho passwordTextView để chuyển đến trang thay đổi mật khẩu
        passwordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        // Thêm sự kiện click cho logoutButton
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        return view;
    }

    private void loadUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String uid = currentUser.getUid();

            // Tạo một tham chiếu đến tài liệu người dùng trong Firestore
            DocumentReference userRef = db.collection("Users").document(uid);

            // Lấy dữ liệu từ Firestore
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Lấy thông tin từ tài liệu Firestore và cập nhật giao diện
                        String accountType = document.getString("profile.accountType");
                        String email = document.getString("profile.email");
                        String username = document.getString("profile.name");

                        // Cập nhật giao diện
                        accountTypeTextView.setText("Loại tài khoản: " + accountType);
                        emailTextView.setText("Email: " + email);
                        usernameTextView.setText("Tên người dùng: " + username);

                        // Có thể thêm các cập nhật khác ở đây
                    }
                }
            });
        }
    }

    private void logout() {
        mAuth.signOut();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);

        getActivity().finish();
    }
}
