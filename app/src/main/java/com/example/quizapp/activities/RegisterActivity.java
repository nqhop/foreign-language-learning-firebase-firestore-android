package com.example.quizapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFullName, etEmail, etPassword;
    private Button btnRegister;
    private TextView txtBackToLogin;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmailRegister);
        etPassword = findViewById(R.id.etPasswordRegister);
        btnRegister = findViewById(R.id.btnRegister);
        txtBackToLogin = findViewById(R.id.txtBackToLogin);

        btnRegister.setOnClickListener(view -> {
            // Lấy giá trị từ các EditText
            String fullName = etFullName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Kiểm tra validation
            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Đăng ký người dùng mới bằng email và mật khẩu
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterActivity.this, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            saveUserDataToFirestore(fullName, email);
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        txtBackToLogin.setOnClickListener(view -> {
            // Chuyển về màn hình đăng nhập
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void saveUserDataToFirestore(String fullName, String email) {
        // Tạo một đối tượng Map để lưu thông tin người dùng
        Map<String, Object> user = new HashMap<>();
        user.put("fullName", fullName);
        user.put("email", email);

        // Lưu thông tin người dùng vào Firestore
        db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .set(user)
                .addOnSuccessListener(aVoid -> {

                    Toast.makeText(RegisterActivity.this, "Lưu thông tin người dùng thành công", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RegisterActivity.this, "Lưu thông tin người dùng thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });


    }
}
