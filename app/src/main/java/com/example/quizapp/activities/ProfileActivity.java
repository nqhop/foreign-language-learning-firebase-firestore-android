package com.example.quizapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.quizapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestoreDB;
    private FirebaseStorage firebaseStorage;

    private ImageView userImageView;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firestoreDB = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        // Hiển thị thông tin người dùng
        initUserProfile();

        // Khởi tạo ImageView và gán sự kiện click để chọn hình ảnh mới
        userImageView = findViewById(R.id.userImageView);
        userImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở Intent để chọn hình ảnh từ Gallery
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
    }

    private void initUserProfile() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            // Lấy ID người dùng hiện tại
            String userId = currentUser.getUid();

            // Thực hiện truy vấn đến tài liệu người dùng trong Firestore
            DocumentReference userRef = firestoreDB.collection("Users").document(userId);

            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        // Lấy thông tin từ tài liệu Firestore
                        String name = document.getString("profile.name");
                        String email = document.getString("profile.email");

                        // Hiển thị thông tin người dùng trên giao diện
                        TextView textViewName = findViewById(R.id.textView2);
                        TextView textViewEmail = findViewById(R.id.textView3);

                        textViewName.setText(name);
                        textViewEmail.setText(email);

                        // Lấy đường dẫn ảnh từ Firestore và cập nhật ImageView bằng Glide
                        String avatarUrl = document.getString("profile.avatar");
                        if (avatarUrl != null && !avatarUrl.isEmpty()) {
                            Glide.with(this)
                                    .load(avatarUrl)
                                    .placeholder(R.drawable.ic_placeholder_image)
                                    .error(R.drawable.ic_error_image)
                                    .into(userImageView);
                        }
                    } else {
                        Toast.makeText(ProfileActivity.this, "No such document", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "Get failed with " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Hàm xử lý sau khi chọn hình ảnh từ Gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();

            // Tải hình ảnh lên Firebase Storage
            uploadImageToFirebaseStorage();
        }
    }

    // Hàm tải hình ảnh lên Firebase Storage
    private void uploadImageToFirebaseStorage() {
        if (selectedImageUri != null) {
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();

            if (currentUser != null) {
                // Lấy ID người dùng hiện tại
                String userId = currentUser.getUid();

                // Thực hiện tải lên hình ảnh lên Firebase Storage
                StorageReference storageRef = firebaseStorage.getReference().child("avatars/" + userId + ".jpg");
                storageRef.putFile(selectedImageUri)
                        .addOnCompleteListener(this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    // Lấy đường dẫn URL sau khi tải lên
                                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                        String downloadUrl = uri.toString();

                                        // Cập nhật đường dẫn ảnh mới vào Firestore
                                        updateImageInFirestore(downloadUrl);

                                        // Cập nhật ImageView bằng Glide
                                        Glide.with(ProfileActivity.this)
                                                .load(downloadUrl)
                                                .placeholder(R.drawable.ic_placeholder_image)
                                                .error(R.drawable.ic_error_image)
                                                .into(userImageView);
                                    });
                                } else {
                                    Toast.makeText(ProfileActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
    }

    private void updateImageInFirestore(String imageUrl) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            DocumentReference userRef = firestoreDB.collection("Users").document(userId);
            userRef.update("profile.avatar", imageUrl)
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, "Failed to update image in Firestore", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
