package com.example.g05;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditThongTinDiaDiem extends AppCompatActivity {

    private EditText editName, editType, editAddress, editPhone;
    private Button buttonSave;
    private ImageButton buttonBack;
    private DatabaseReference placesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_thongtindiadiem);

        // Khởi tạo view
        editName = findViewById(R.id.editName);
        editType = findViewById(R.id.editType);
        editAddress = findViewById(R.id.editAddress);
        editPhone = findViewById(R.id.editPhone);
        buttonSave = findViewById(R.id.buttonSave);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        // Kiểm tra ID
        if (id == null || id.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy ID địa điểm!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Khởi tạo Firebase Reference
        placesRef = FirebaseDatabase.getInstance().getReference("locations");

        // Kiểm tra sự tồn tại của địa điểm
        placesRef.child(id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                DataSnapshot snapshot = task.getResult();
                String name = snapshot.child("name").getValue(String.class);
                String type = snapshot.child("type").getValue(String.class);
                String address = snapshot.child("address").getValue(String.class);
                String phone = snapshot.child("sdt").getValue(String.class);

                // Gán dữ liệu vào các trường
                editName.setText(name);
                editType.setText(type);
                editAddress.setText(address);
                editPhone.setText(phone);
            } else {
                Toast.makeText(this, "Không tìm thấy địa điểm trong Firebase!", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        // Xử lý nút Lưu
        buttonSave.setOnClickListener(v -> {
            String updatedName = editName.getText().toString().trim();
            String updatedType = editType.getText().toString().trim();
            String updatedAddress = editAddress.getText().toString().trim();
            String updatedPhone = editPhone.getText().toString().trim();

            if (TextUtils.isEmpty(updatedName) || TextUtils.isEmpty(updatedType) ||
                    TextUtils.isEmpty(updatedAddress) || TextUtils.isEmpty(updatedPhone)) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Cập nhật Firebase
            placesRef.child(id).child("name").setValue(updatedName);
            placesRef.child(id).child("type").setValue(updatedType);
            placesRef.child(id).child("address").setValue(updatedAddress);
            placesRef.child(id).child("sdt").setValue(updatedPhone).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(EditThongTinDiaDiem.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();

                    // Trả kết quả về
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("name", updatedName);
                    resultIntent.putExtra("type", updatedType);
                    resultIntent.putExtra("address", updatedAddress);
                    resultIntent.putExtra("sdt", updatedPhone);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(EditThongTinDiaDiem.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Xử lý nút Back
        back();
    }

    private void back() {
        buttonBack = findViewById(R.id.imagebuttonBack);
        buttonBack.setOnClickListener(v -> onBackPressed());
    }
}
