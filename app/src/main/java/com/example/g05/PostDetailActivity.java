package com.example.g05;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.g05.Helpers.ImageManager;
import com.squareup.picasso.Picasso;

public class PostDetailActivity extends AppCompatActivity {

    private TextView textViewTitle, textViewContent;
    private ImageView imageViewPost;
    private ImageButton imageBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        // Khởi tạo các view
        textViewTitle = findViewById(R.id.textTitle);
        textViewContent = findViewById(R.id.textContent);
        imageViewPost = findViewById(R.id.imagePost);
        imageBack = findViewById(R.id.imagebuttonBack);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        String imageUrl = intent.getStringExtra("imageUrl");

        // Hiển thị dữ liệu lên các view
        if (title != null) {
            textViewTitle.setText(title);
        }

        if (content != null) {
            textViewContent.setText(content);
        }

        if (imageUrl != null && !imageUrl.isEmpty()) {
            // Tải hình ảnh từ URL nếu có
            Picasso.get().load(imageUrl).into(imageViewPost);
        } else {
            imageViewPost.setVisibility(View.GONE);  // Nếu không có hình ảnh, ẩn ImageView
        }
        if (imageUrl != null && !imageUrl.isEmpty()) {
            // Chuyển đổi Base64 string thành Bitmap và hiển thị trong ImageView
            Bitmap bitmap = ImageManager.convertBase64ToBitmap(imageUrl);
            imageViewPost.setImageBitmap(bitmap);
        } else {
            imageViewPost.setVisibility(View.GONE);  // Nếu không có hình ảnh, ẩn ImageView
        }


        btnBack();
    }
    private void btnBack() {
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


}

