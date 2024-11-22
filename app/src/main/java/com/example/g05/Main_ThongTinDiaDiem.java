package com.example.g05;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
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

public class Main_ThongTinDiaDiem extends AppCompatActivity {

    private FloatingActionButton fabAddthongtindiadiem;
    private RecyclerView recycler;
    private ThongTinDiaDiem_Adapter locationAdapter;
    private List<ThongTinDiaDiem> locationList; // Danh sách gốc
    private List<ThongTinDiaDiem> filteredList; // Danh sách đã lọc
    private DatabaseReference placesRef;
    private ImageButton btnhome;
    private EditText editSearch; // Thanh tìm kiếm

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thongtindiadiem);

        recycler = findViewById(R.id.recyclerView);
        recycler.setLayoutManager(new LinearLayoutManager(this));


        locationList = new ArrayList<>();
        filteredList = new ArrayList<>();
        locationAdapter = new ThongTinDiaDiem_Adapter(Main_ThongTinDiaDiem.this, filteredList);
        recycler.setAdapter(locationAdapter);


        placesRef = FirebaseDatabase.getInstance().getReference("locations");

        // 4. Thiết lập thanh tìm kiếm
        editSearch = findViewById(R.id.editSearch);
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterLocations(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        placesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                locationList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ThongTinDiaDiem place = snapshot.getValue(ThongTinDiaDiem.class);
                    if (place != null) {
                        locationList.add(place);
                    }
                }
                // Lọc lại danh sách theo từ khóa tìm kiếm hiện tại
                filterLocations(editSearch.getText().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Main_ThongTinDiaDiem.this, "Lỗi khi tải dữ liệu từ Firebase", Toast.LENGTH_SHORT).show();
            }
        });

        // 6. Xử lý khi nhấn nút thêm địa điểm
        fabAddthongtindiadiem = findViewById(R.id.fabAdd);
        fabAddthongtindiadiem.setOnClickListener(v -> {
            Intent intent = new Intent(Main_ThongTinDiaDiem.this, AddThongTinDiaDiem.class);
            startActivity(intent);
        });

        btnhome = findViewById(R.id.buttonHome);
        btnhome.setOnClickListener(v -> {
            Intent intent = new Intent(Main_ThongTinDiaDiem.this, Main_ThongTinDiaDiem.class);
            startActivity(intent);
        });
    }

    private void filterLocations(String keyword) {
        filteredList.clear();

        if (keyword.isEmpty()) {
            // Nếu không có từ khóa, hiển thị toàn bộ danh sách
            filteredList.addAll(locationList);
        } else {
            for (ThongTinDiaDiem place : locationList) {
                // Kiểm tra null trước khi so sánh
                String name = place.getName() != null ? place.getName().toLowerCase() : "";
                String address = place.getAddress() != null ? place.getAddress().toLowerCase() : "";

                if (name.contains(keyword.toLowerCase()) || address.contains(keyword.toLowerCase())) {
                    filteredList.add(place);
                }
            }
        }

        // Cập nhật RecyclerView sau khi lọc
        locationAdapter.notifyDataSetChanged();
    }
}













//package com.example.g05;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//public class Main_ThongTinDiaDiem extends AppCompatActivity {
//
//    private FloatingActionButton fabAddthongtindiadiem;
//    private RecyclerView recycler;
//    private ThongTinDiaDiem_Adapter locationAdapter;
//    private List<ThongTinDiaDiem> locationList;
//    private DatabaseReference placesRef;
//
//    private List<ThongTinDiaDiem> filteredList; // Danh sách được lọc
//
//    private EditText editSearch; // EditText để nhập từ khóa tìm kiếm
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.thongtindiadiem);
//
//        // Khởi tạo RecyclerView
//        recycler = findViewById(R.id.recyclerView);
//        recycler.setLayoutManager(new LinearLayoutManager(this));
//        locationList = new ArrayList<>();
//        locationAdapter = new ThongTinDiaDiem_Adapter(Main_ThongTinDiaDiem.this, locationList);
//        recycler.setAdapter(locationAdapter);
//
//
//        // Khởi tạo Firebase Database
//        placesRef = FirebaseDatabase.getInstance().getReference("locations");
//
//
//        // Khởi tạo EditText tìm kiếm
//        editSearch = findViewById(R.id.editSearch);
//        editSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                filterLocations(s.toString()); // Lọc danh sách khi có thay đổi trong EditText
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
//
//
//        // Lắng nghe dữ liệu thay đổi từ Firebase
//        placesRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                locationList.clear(); // Xóa danh sách cũ trước khi cập nhật
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    ThongTinDiaDiem place = snapshot.getValue(ThongTinDiaDiem.class);
//                    if (place != null) {
//                        locationList.add(place); // Thêm địa điểm vào danh sách
//                    }
//                }
//                locationAdapter.notifyDataSetChanged(); // Cập nhật UI
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Thông báo khi có lỗi xảy ra trong quá trình đọc dữ liệu
//                Toast.makeText(Main_ThongTinDiaDiem.this, "Failed to load locations.", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        // Sự kiện khi nhấn FloatingActionButton để thêm địa điểm
//        fabAddthongtindiadiem = findViewById(R.id.fabAdd);
//        fabAddthongtindiadiem.setOnClickListener(v -> {
//            // Mở AddPlaceActivity khi nhấn FAB
//            Intent intent = new Intent(Main_ThongTinDiaDiem.this, AddThongTinDiaDiem.class);
//            startActivity(intent);
//        });
//
//        filteredList = new ArrayList<>(); // Danh sách được lọc
//        locationAdapter = new ThongTinDiaDiem_Adapter(Main_ThongTinDiaDiem.this, filteredList);
//    }
//    private void filterLocations(String keyword) {
//        filteredList.clear();
//        if (keyword.isEmpty()) {
//            filteredList.addAll(locationList); // Hiển thị toàn bộ danh sách nếu không có từ khóa
//        } else {
//            for (ThongTinDiaDiem place : locationList) {
//                if (place.getName().toLowerCase().contains(keyword.toLowerCase()) ||
//                        place.getAddress().toLowerCase().contains(keyword.toLowerCase())) {
//                    filteredList.add(place); // Thêm địa điểm phù hợp vào danh sách lọc
//                }
//            }
//        }
//        locationAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
//    }
//}