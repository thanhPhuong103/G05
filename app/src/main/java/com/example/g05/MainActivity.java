package com.example.g05;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fabAddPost;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private DatabaseReference postsRef;
    private ImageButton btnAddress, btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Khởi tạo RecyclerView
        recyclerView = findViewById(R.id.recyclerViewPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(MainActivity.this, postList);
        recyclerView.setAdapter(postAdapter);

        // Khởi tạo Firebase Database
        postsRef = FirebaseDatabase.getInstance().getReference("posts");

        // Lắng nghe dữ liệu thay đổi từ Firebase
        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear(); // Xóa danh sách cũ trước khi cập nhật
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    if (post != null) {
                        postList.add(post); // Thêm bài đăng vào danh sách
                    }
                }
                postAdapter.notifyDataSetChanged(); // Cập nhật UI
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Thông báo khi có lỗi xảy ra trong quá trình đọc dữ liệu
                Toast.makeText(MainActivity.this, "Failed to load posts.", Toast.LENGTH_SHORT).show();
            }
        });

        // Sự kiện khi nhấn FloatingActionButton để thêm bài đăng
        fabAddPost = findViewById(R.id.fabAddPost);
        fabAddPost.setOnClickListener(v -> {
            // Mở AddPostActivity khi nhấn FAB
            Intent intent = new Intent(MainActivity.this, AddPostActivity.class);
            startActivity(intent);
        });

        btnAddress = findViewById(R.id.buttonAddress);
        btnAddress.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Main_ThongTinDiaDiem.class);
            startActivity(intent);
        });


        //

    }
}