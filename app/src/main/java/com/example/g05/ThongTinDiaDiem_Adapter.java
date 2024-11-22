package com.example.g05;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g05.Helpers.ImageManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ThongTinDiaDiem_Adapter extends RecyclerView.Adapter<ThongTinDiaDiem_Adapter.PlaceViewHolder> {

    private Context context;
    private List<ThongTinDiaDiem> placeList;
    private DatabaseReference favoritesRef;

    public ThongTinDiaDiem_Adapter(Context context, List<ThongTinDiaDiem> placeList) {
        this.context = context;
        this.placeList = placeList;
        this.favoritesRef = FirebaseDatabase.getInstance().getReference("favorites"); // Tham chiếu Firebase
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_thongtindiadiem, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        ThongTinDiaDiem place = placeList.get(position);

        holder.textViewName.setText(place.getName());
        holder.textViewType.setText(place.getType());
        holder.textViewAddress.setText(place.getAddress());

        // Tải hình ảnh (nếu có)
        if (place.getImageUrl() != null) {
            if (isUrl(place.getImageUrl())) {
                Picasso.get().load(place.getImageUrl()).into(holder.imageViewPlace);
            } else {
                holder.imageViewPlace.setImageBitmap(ImageManager.convertBase64ToBitmap(place.getImageUrl()));
            }
        } else {
            holder.imageViewPlace.setVisibility(View.GONE);
        }

//
//        // Kiểm tra trạng thái "Save/Đã Save" từ Firebase
//        favoritesRef.child(place.getId()).get().addOnCompleteListener(task -> {
//            if (task.isSuccessful() && task.getResult().exists()) {
//                holder.saveIcon.setImageResource(R.drawable.save_favourite); // Icon "Đã Save"
//            } else {
//                holder.saveIcon.setImageResource(R.drawable.save); // Icon "Save"
//            }
//        });
//
//        // Xử lý khi nhấn nút Save
//        holder.saveIcon.setOnClickListener(v -> {
//            favoritesRef.child(place.getId()).get().addOnCompleteListener(task -> {
//                if (task.isSuccessful() && task.getResult().exists()) {
//                    // Đã lưu -> Xóa khỏi danh sách yêu thích
//                    favoritesRef.child(place.getId()).removeValue().addOnCompleteListener(removeTask -> {
//                        if (removeTask.isSuccessful()) {
//                            holder.saveIcon.setImageResource(R.drawable.save); // Cập nhật lại icon
//                            Toast.makeText(context, "Đã xóa khỏi danh sách yêu thích!", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(context, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                } else {
//                    // Chưa lưu -> Thêm vào danh sách yêu thích
//                    favoritesRef.child(place.getId()).setValue(place).addOnCompleteListener(saveTask -> {
//                        if (saveTask.isSuccessful()) {
//                            holder.saveIcon.setImageResource(R.drawable.save_favourite); // Cập nhật lại icon
//                            Toast.makeText(context, "Đã lưu địa điểm!", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(context, "Lưu thất bại!", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            });
//        });


        // Sự kiện nhấn vào item để mở giao diện chi tiết
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ThongTinDiaDiem_Details.class);
            intent.putExtra("id", place.getId());
            intent.putExtra("name", place.getName());
            intent.putExtra("type", place.getType());
            intent.putExtra("address", place.getAddress());
            intent.putExtra("sdt", place.getSdt());
            intent.putExtra("imageUrl", place.getImageUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public static class PlaceViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewType, textViewAddress;
        ImageView imageViewPlace, saveIcon; // Biểu tượng Save

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textten);
            textViewType = itemView.findViewById(R.id.textloaihang);
            textViewAddress = itemView.findViewById(R.id.textdiachi);
            imageViewPlace = itemView.findViewById(R.id.image);
            saveIcon = itemView.findViewById(R.id.btnSave); // Icon Save
        }
    }

    // Kiểm tra xem URL có hợp lệ hay không
    public boolean isUrl(String imageUrl) {
        return imageUrl.startsWith("http://") || imageUrl.startsWith("https://");
    }
}
