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
public class AddThongTinDiaDiem extends AppCompatActivity {

        private EditText editName, editType, editAddress, editSdt;
        private Button buttonAddImage, buttonAddPlace;
        private ImageView imageViewPreview;
        private Uri imageUri;
        private DatabaseReference placesRef;
        private StorageReference storageRef;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_thongtindiadiem);

            editName = findViewById(R.id.editName);
            editType = findViewById(R.id.editloaihang);
            editAddress = findViewById(R.id.editDiaChi);
            editSdt = findViewById(R.id.editSDT);
            buttonAddImage = findViewById(R.id.buttonAddImage);
            imageViewPreview = findViewById(R.id.imageViewPreview);
            buttonAddPlace = findViewById(R.id.buttonAdd);

            placesRef = FirebaseDatabase.getInstance().getReference().child("locations");
            storageRef = FirebaseStorage.getInstance().getReference("locations_images");

            buttonAddImage.setOnClickListener(v -> openGallery());
            buttonAddPlace.setOnClickListener(v -> addPlace());
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
            }
        }

        private void addPlace() {
            String name = editName.getText().toString().trim();
            String type = editType.getText().toString().trim();
            String address = editAddress.getText().toString().trim();
            String sdt = editSdt.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(type) || TextUtils.isEmpty(address) || TextUtils.isEmpty(sdt)) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            final String placeId = placesRef.push().getKey();

            if (imageUri != null) {
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String imageUrl = ImageManager.convertBitmapToBase64(bitmap);

                ThongTinDiaDiem place = new ThongTinDiaDiem(name, type, address, imageUrl, sdt);
                placesRef.child(placeId).setValue(place)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddThongTinDiaDiem.this, "Thêm địa điểm thành công", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(AddThongTinDiaDiem.this, "Thêm địa điểm thất bại", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                ThongTinDiaDiem place = new ThongTinDiaDiem(name, type, address, null, sdt);
                placesRef.child(placeId).setValue(place)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddThongTinDiaDiem.this, "Thêm địa điểm thành công", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(AddThongTinDiaDiem.this, "Thêm địa điểm thất bại", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
}