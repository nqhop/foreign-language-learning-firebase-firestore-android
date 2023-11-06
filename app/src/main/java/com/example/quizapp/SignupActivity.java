package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private EditText etEmail, etPassword, etName;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Ánh xạ các View
//        etEmail = findViewById(R.id.etEmail);
//        etPassword = findViewById(R.id.etPassword);
//        etName = findViewById(R.id.etName);
//        btnSignup = findViewById(R.id.btnSignup);
//
//        btnSignup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Lấy dữ liệu
//                String email = etEmail.getText().toString();
//                String password = etPassword.getText().toString();
//                String name = etName.getText().toString();
//
//                // Gọi API đăng ký
//                register(email, password, name);
//            }
//        });
    }

    private void register(String email, String password, String name) {
        // Gọi API đăng ký backend
        // Nếu đăng ký thành công
        saveLoggedInUser(email);

        // Chuyển qua MainActivity
        startActivity(new Intent(SignupActivity.this, MainActivity.class));
        finish();
    }

    private void saveLoggedInUser(String email) {
        // Lưu email vào SharedPreferences
    }

}