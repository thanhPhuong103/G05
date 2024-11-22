package com.example.g05;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.g05.Helpers.ImageManager;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> postList;
    private Context context;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);


        holder.titleText.setText(post.getTitle());

        // Ẩn hoặc xóa nội dung bài viết (có thể làm TextView cho nội dung nhưng không hiển thị)
        holder.contentText.setVisibility(View.GONE); // Ẩn nội dung

        // Tải hình ảnh (nếu có)
        if (post.getImageUrl() != null) {
            if (isUrl(post.getImageUrl())) {
                Picasso.get().load(post.getImageUrl()).into(holder.imageView);
            } else {
                holder.imageView.setImageBitmap(ImageManager.convertBase64ToBitmap(post.getImageUrl()));
            }
        } else {
            holder.imageView.setVisibility(View.GONE); // Nếu không có hình ảnh, ẩn ImageView
        }

            // Sự kiện nhấn vào item
            holder.itemView.setOnClickListener(v -> {
                // Mở PostDetailActivity và truyền dữ liệu qua Intent
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra("title", post.getTitle());
                intent.putExtra("content", post.getContent());
                intent.putExtra("imageUrl", post.getImageUrl());
                context.startActivity(intent);
            });
        }



    @Override
    public int getItemCount() {
        return postList.size();
    }

//    //  lưu địa điểm yêu thích
//    private void saveFavoritePlace(ThongTinDiaDiem place) {
//        DatabaseReference favoriteRef = FirebaseDatabase.getInstance().getReference("favorites");
//        String placeId = favoriteRef.push().getKey(); // Tạo ID duy nhất cho địa điểm
//
//        if (placeId != null) {
//            favoriteRef.child(placeId).setValue(place).addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    Toast.makeText(context, "Lưu địa điểm thành công!", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(context, "Lưu địa điểm thất bại!", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }



    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, contentText;
        ImageView imageView;

        public PostViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.textViewTitle);
            contentText = itemView.findViewById(R.id.textViewContent);
            imageView = itemView.findViewById(R.id.imageViewPost);
        }
    }

    public boolean isUrl(String imageUrl) {
        return  (imageUrl.startsWith("http://") || imageUrl.startsWith("https://"));
    }

}
