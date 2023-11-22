package com.example.quizapp.activities;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etEmail;
    private Button btnResetPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Ánh xạ các view từ layout
        etEmail = findViewById(R.id.etEmail);
        btnResetPassword = findViewById(R.id.btnResetPassword);

        // Xử lý sự kiện khi nhấn nút Reset Password
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy giá trị từ EditText
                String email = etEmail.getText().toString().trim();

                // Kiểm tra validation
                if (email.isEmpty()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Vui lòng nhập địa chỉ email", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Xử lý đặt lại mật khẩu (Bạn cần thêm mã xử lý đặt lại mật khẩu thực tế ở đây, ví dụ: sử dụng Firebase Authentication)

                // Hiển thị thông báo thành công và chuyển đến màn hình đăng nhập
                Toast.makeText(ForgotPasswordActivity.this, "Đã gửi yêu cầu đặt lại mật khẩu", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
