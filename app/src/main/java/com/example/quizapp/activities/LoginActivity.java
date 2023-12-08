package com.example.quizapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.R;
import com.example.quizapp.activities.RegisterActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 123;

    private EditText etEmail, etPassword;
    private Button btnLogin, btnGoogleLogin;
    private TextView txtRegister, txtForgotPassword;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etEmailLogin);
        etPassword = findViewById(R.id.etPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoogleLogin = findViewById(R.id.btnGoogleLogin);
        txtRegister = findViewById(R.id.txtRegister);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);

        btnLogin.setOnClickListener(view -> {
            handleEmailLogin();
        });

        btnGoogleLogin.setOnClickListener(view -> {
            signInWithGoogle();
        });

        txtRegister.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });

        txtForgotPassword.setOnClickListener(view -> {
            showForgotPasswordDialog();
        });

        configureGoogleSignIn();
    }

    private void handleEmailLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        checkUserDataInFirestore(mAuth.getCurrentUser().getUid());
                    } else {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void configureGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account.getIdToken());
        } catch (ApiException e) {
            Log.e(TAG, "Google sign in failed", e);
            Toast.makeText(this, "Đăng nhập bằng Google thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        checkUserDataInFirestore(mAuth.getCurrentUser().getUid());
                    } else {
                        Toast.makeText(LoginActivity.this, "Đăng nhập bằng Google thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quên mật khẩu");

        View view = getLayoutInflater().inflate(R.layout.activity_forgot_password, null);
        EditText etEmailForgotPassword = view.findViewById(R.id.etEmail);

        builder.setView(view);

        builder.setPositiveButton("Gửi email", (dialog, which) -> {
            String email = etEmailForgotPassword.getText().toString().trim();
            if (!TextUtils.isEmpty(email)) {
                sendResetPasswordEmail(email);
            } else {
                Toast.makeText(LoginActivity.this, "Vui lòng nhập địa chỉ email", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void sendResetPasswordEmail(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Kiểm tra email để đặt lại mật khẩu", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Gửi email đặt lại mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUserDataInFirestore(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(userId);

        userRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Người dùng tồn tại trong Firestore
                        String fullName = documentSnapshot.getString("fullName");
                        String email = documentSnapshot.getString("email");


                        // Thực hiện các hành động sau khi đăng nhập thành công, ví dụ: chuyển đến màn hình chính
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class)
                                .putExtra("fullName", fullName)
                                .putExtra("email", email));
                        finish();
                    } else {
                        // Người dùng không tồn tại trong Firestore, có thể do lỗi hoặc dữ liệu bị xóa
                        Toast.makeText(LoginActivity.this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                        // Đăng xuất người dùng (nếu cần)
                        FirebaseAuth.getInstance().signOut();
                    }
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi khi truy vấn Firestore
                    Toast.makeText(LoginActivity.this, "Lỗi khi xác minh thông tin người dùng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        }
    }
}
