package com.example.g05;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.g05.Helpers.ImageManager;
public class ThongTinDiaDiem_Details extends AppCompatActivity {

    private TextView textViewName, textViewType, textViewAddress, textViewPhone;
    private ImageView imageViewPlace;
    private ImageButton imageBack;

    private Button buttonEdit, buttonDelete;
    private String id;
    private String name;
    private String type;
    private String address;
    private String imageUrl;
    private String sdt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_thongtindiadiem);

        // Khởi tạo các view
        textViewName = findViewById(R.id.text_Ten);
        textViewType = findViewById(R.id.text_loaihang);
        textViewAddress = findViewById(R.id.text_dia_chi);
        textViewPhone = findViewById(R.id.text_phone_number);
        imageViewPlace = findViewById(R.id.img_detail_info);
        imageBack = findViewById(R.id.imagebuttonBack);
        buttonDelete=findViewById(R.id.btn_delete);
        buttonEdit = findViewById(R.id.btn_edit);


        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String type = intent.getStringExtra("type");
        String address = intent.getStringExtra("address");
        String phone = intent.getStringExtra("sdt");
        String imageUrl = intent.getStringExtra("imageUrl");

        // Hiển thị dữ liệu lên các view
        if (name != null) {
            textViewName.setText(name);
        }

        if (type != null) {
            textViewType.setText(type);
        }

        if (address != null) {
            textViewAddress.setText(address);
        }

//        if (phone != null) {
//            textViewPhone.setText(phone);
//        }

        if (imageUrl != null && !imageUrl.isEmpty()) {
            // Chuyển đổi Base64 string thành Bitmap và hiển thị trong ImageView
            Bitmap bitmap = ImageManager.convertBase64ToBitmap(imageUrl);
            imageViewPlace.setImageBitmap(bitmap);
        } else {
            imageViewPlace.setVisibility(View.GONE);  // Nếu không có hình ảnh, ẩn ImageView
        }

        // Nút quay lại
        setupBackButton();
        Edit();
    }

    private void setupBackButton() {
        imageBack.setOnClickListener(v -> onBackPressed());
    }



    private void Edit()
    {
        buttonEdit.setOnClickListener(v -> {
            Intent editIntent = new Intent(ThongTinDiaDiem_Details.this, EditThongTinDiaDiem.class);
            editIntent.putExtra("id", id);
            editIntent.putExtra("name", name);
            editIntent.putExtra("type", type);
            editIntent.putExtra("address", address);
            editIntent.putExtra("sdt", sdt);
            editIntent.putExtra("imageUrl", imageUrl);
            startActivity(editIntent);
            startActivityForResult(editIntent, 100); // Chờ kết quả từ Edit Activity
             });
}
    // Nhận kết quả từ Edit Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            name = data.getStringExtra("name");
            type = data.getStringExtra("type");
            address = data.getStringExtra("address");
            sdt = data.getStringExtra("sdt");

            textViewName.setText(name);
            textViewType.setText(type);
            textViewAddress.setText(address);
            textViewPhone.setText(sdt);

            Toast.makeText(this, "Thông tin đã được cập nhật!", Toast.LENGTH_SHORT).show();
            displayData();
        }
    }
    // Hàm hiển thị dữ liệu lên giao diện
    private void displayData() {
        textViewName.setText(name);
        textViewType.setText(type);
        textViewAddress.setText(address);
        textViewPhone.setText(sdt);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Bitmap bitmap = ImageManager.convertBase64ToBitmap(imageUrl);
            imageViewPlace.setImageBitmap(bitmap);
        } else {
            imageViewPlace.setVisibility(View.GONE); // Nếu không có hình ảnh, ẩn ImageView
        }
    }



}