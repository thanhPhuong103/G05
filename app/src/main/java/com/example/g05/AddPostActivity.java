package com.example.g05;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.g05.Helpers.ImageManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
public class AddPostActivity extends AppCompatActivity {

    private EditText editTitle, editContent;
    private Button buttonAddImage, buttonAddPost;
    private ImageView imageViewPreview;
    private Uri imageUri;
    private DatabaseReference postsRef;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editContent);
        buttonAddImage = findViewById(R.id.buttonAddImage);
        imageViewPreview = findViewById(R.id.imageViewPreview);
        buttonAddPost = findViewById(R.id.buttonAddPost);

        postsRef = FirebaseDatabase.getInstance().getReference().child("posts");
        storageRef = FirebaseStorage.getInstance().getReference("post_images");

        buttonAddImage.setOnClickListener(v -> openGallery());
        buttonAddPost.setOnClickListener(v -> addPost());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageViewPreview.setVisibility(View.VISIBLE);
            Picasso.get().load(imageUri).into(imageViewPreview);
            editTitle.setVisibility(View.VISIBLE);

        }
    }

    private void addPost() {
        String title = editTitle.getText().toString().trim();
        String content = editContent.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        final String postId = postsRef.push().getKey();

        if (imageUri != null) {
//            final StorageReference fileRef = storageRef.child(postId);
//            fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
//                fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
//                    String imageUrl = uri.toString();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String imageUrl= ImageManager.convertBitmapToBase64(bitmap);
                    Post post = new Post(title, content, imageUrl);
                    postsRef.child(postId).setValue(post)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AddPostActivity.this, "Thêm bài đăng thành công", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(AddPostActivity.this, "Thêm bài đăng thất bại", Toast.LENGTH_SHORT).show();
                                }
                            });
//                });
//            }).addOnFailureListener(e -> Toast.makeText(AddPostActivity.this, "Upload hình ảnh thất bại", Toast.LENGTH_SHORT).show());
        } else {
            Post post = new Post(title, content, null);
            postsRef.child(postId).setValue(post)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddPostActivity.this, "Thêm bài đăng thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(AddPostActivity.this, "Thêm bài đăng thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
