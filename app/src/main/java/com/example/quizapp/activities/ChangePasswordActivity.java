package com.example.quizapp.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText oldPasswordEditText, newPasswordEditText, confirmPasswordEditText;
    private TextView passwordMatchTextView;
    private Button saveButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mAuth = FirebaseAuth.getInstance();

        oldPasswordEditText = findViewById(R.id.oldPasswordEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        passwordMatchTextView = findViewById(R.id.passwordMatchTextView);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });

    }

    private void changePassword() {
        String oldPassword = oldPasswordEditText.getText().toString().trim();
        String newPassword = newPasswordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Kiểm tra xem các trường đã được điền đầy đủ hay không
        if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            // Hiển thị thông báo lỗi
            passwordMatchTextView.setText("Vui lòng nhập đầy đủ thông tin.");
            return;
        }

        // Kiểm tra mật khẩu mới và xác nhận mật khẩu có giống nhau hay không
        if (!newPassword.equals(confirmPassword)) {
            // Hiển thị thông báo lỗi
            passwordMatchTextView.setText("Mật khẩu mới và xác nhận mật khẩu không giống nhau.");
            return;
        }

        // Lấy người dùng hiện tại
        FirebaseUser user = mAuth.getCurrentUser();

        // Đổi mật khẩu sử dụng Firebase Authentication
        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Hiển thị thông báo thành công
                    passwordMatchTextView.setText("Đổi mật khẩu thành công.");
                } else {
                    // Hiển thị thông báo lỗi
                    passwordMatchTextView.setText("Đổi mật khẩu không thành công. Vui lòng kiểm tra mật khẩu cũ.");
                }
            }
        });
    }
}
