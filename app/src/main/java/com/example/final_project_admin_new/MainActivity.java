package com.example.final_project_admin_new;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project_admin_new.adapter.YogaClassAdapter;
import com.example.final_project_admin_new.db.DatabaseHelper;
import com.example.final_project_admin_new.model.YogaClass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private YogaClassAdapter yogaClassAdapter;
    private List<YogaClass> yogaClassList;
    private DatabaseHelper dbHelper;
    private FloatingActionButton btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo RecyclerView
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnAdd = findViewById(R.id.btnAdd);  // Correct casting


        // Khởi tạo DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Lấy tất cả các lớp yoga từ cơ sở dữ liệu
        yogaClassList = dbHelper.getAllYogaClasses();

        // Khởi tạo Adapter với dữ liệu
        yogaClassAdapter = new YogaClassAdapter(this, yogaClassList, dbHelper);
        recyclerView.setAdapter(yogaClassAdapter);

        // Thêm một lớp yoga mới
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddClassActivity.class);
                startActivity(intent);
            }
        });
    }
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    // Phương thức làm mới dữ liệu trong RecyclerView
    public void refreshData() {
        // Lấy lại danh sách lớp yoga sau khi quay lại từ AddClassActivity
        yogaClassList = dbHelper.getAllYogaClasses();
        yogaClassAdapter.notifyDataSetChanged();  // Cập nhật lại RecyclerView
    }
}