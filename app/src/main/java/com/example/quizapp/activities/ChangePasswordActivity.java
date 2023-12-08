package com.example.quizapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText oldPasswordEditText, newPasswordEditText, confirmPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Ánh xạ các thành phần trong giao diện
        oldPasswordEditText = findViewById(R.id.oldPasswordEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);

        // Xử lý sự kiện khi người dùng ấn nút "Lưu"
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> saveNewPassword());
    }

    // Hàm xử lý khi người dùng ấn nút "Lưu"
    private void saveNewPassword() {
        String oldPassword = oldPasswordEditText.getText().toString();
        String newPassword = newPasswordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null && newPassword.equals(confirmPassword)) {
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);

            // Xác thực mật khẩu cũ
            user.reauthenticate(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Xác thực thành công, cập nhật mật khẩu mới
                            user.updatePassword(newPassword)
                                    .addOnCompleteListener(updateTask -> {
                                        if (updateTask.isSuccessful()) {
                                            // Cập nhật mật khẩu thành công
                                            // Hiển thị thông báo hoặc chuyển về màn hình trước đó
                                            Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            // Xử lý lỗi khi cập nhật mật khẩu
                                            Toast.makeText(ChangePasswordActivity.this, "Lỗi khi đổi mật khẩu", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // Xử lý lỗi khi xác thực mật khẩu cũ
                            Toast.makeText(ChangePasswordActivity.this, "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Xử lý lỗi khi nhập mật khẩu mới không khớp hoặc người dùng không đăng nhập
            Toast.makeText(ChangePasswordActivity.this, "Kiểm tra lại mật khẩu", Toast.LENGTH_SHORT).show();
        }
    }
}
