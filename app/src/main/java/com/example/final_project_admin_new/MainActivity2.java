package com.example.final_project_admin_new;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project_admin_new.adapter.ClassInstanceAdapter;
import com.example.final_project_admin_new.adapter.YogaClassAdapter;
import com.example.final_project_admin_new.db.DatabaseHelper;
import com.example.final_project_admin_new.model.ClassInstance;
import com.example.final_project_admin_new.model.YogaClass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity2 extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ClassInstanceAdapter classInstanceAdapter;
    private List<ClassInstance> classInstanceList;
    private DatabaseHelper dbHelper;
    private FloatingActionButton btnAdd, btnYogaClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnAdd = findViewById(R.id.btnAdd);  // Correct casting
        btnYogaClass = findViewById(R.id.btnYogaClass);

        // Khởi tạo DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Lấy tất cả các lớp yoga từ cơ sở dữ liệu
        classInstanceList = dbHelper.getClassInstances();

        // Khởi tạo Adapter với dữ liệu
        classInstanceAdapter = new ClassInstanceAdapter(this, classInstanceList, dbHelper);
        recyclerView.setAdapter(classInstanceAdapter);

        // Thêm một lớp yoga mới
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, AddClassInstance.class);
                startActivity(intent);
            }
        });
        btnYogaClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
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
        classInstanceList = dbHelper.getClassInstances();
        classInstanceAdapter.notifyDataSetChanged();  // Cập nhật lại RecyclerView
    }
}