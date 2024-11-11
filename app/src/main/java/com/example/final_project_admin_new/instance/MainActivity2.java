package com.example.final_project_admin_new.instance;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project_admin_new.R;
import com.example.final_project_admin_new.adapter.ClassInstanceAdapter;
import com.example.final_project_admin_new.db.DatabaseHelper;
import com.example.final_project_admin_new.model.ClassInstance;
import com.example.final_project_admin_new.model.YogaClass;
import com.example.final_project_admin_new.yogaclass.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity2 extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ClassInstanceAdapter classInstanceAdapter;
    private List<ClassInstance> classInstanceList;
    private DatabaseHelper dbHelper;
    private FloatingActionButton btnAdd;
    private LinearLayout backToYogaClassesLayout;
    private int classId;
    private TextView yogaClassTitle,yogaClassDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnAdd = findViewById(R.id.btnAdd);  // Correct casting
        backToYogaClassesLayout = findViewById(R.id.backToYogaClassesLayout);

        classId = getIntent().getIntExtra("CLASS_ID", -1);
        // Khởi tạo DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Lấy tất cả các lớp yoga từ cơ sở dữ liệu
        classInstanceList = dbHelper.getClassInstancesByClassId(classId);

        // Khởi tạo Adapter với dữ liệu
        classInstanceAdapter = new ClassInstanceAdapter(this, classInstanceList, dbHelper);
        recyclerView.setAdapter(classInstanceAdapter);
        Cursor yogaClass = dbHelper.getClassDetails(classId);

        // Hiển thị thông tin yoga class lên giao diện
        yogaClassTitle = findViewById(R.id.yogaClassTitle);
        yogaClassDetails = findViewById(R.id.yogaClassDetails);
        //yogaclass information
        if (yogaClass != null && yogaClass.moveToFirst()) {  // Kiểm tra nếu có dữ liệu trong Cursor
            String classType = yogaClass.getString(yogaClass.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLASS_TYPE));
            String time = yogaClass.getString(yogaClass.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TIME));
            String dayOfWeek = yogaClass.getString(yogaClass.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DAY_OF_WEEK));

            yogaClassTitle.setText(classType);  // Đặt tên của yoga class
            yogaClassDetails.setText(time + " on " + dayOfWeek);  // Đặt thông tin chi tiết của yoga class
        } else {
            yogaClassTitle.setText("Yoga Class Not Found");
            yogaClassDetails.setText("No details available.");
        }

        // Thêm một lớp yoga mới
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, AddClassInstance.class);
                intent.putExtra("CLASS_ID", classId);  // Truyền classId qua Intent
                startActivity(intent);
            }
        });
        backToYogaClassesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        refreshData();  // Gọi lại phương thức làm mới dữ liệu
    }
    public void refreshData() {
        // Lấy lại danh sách lớp yoga sau khi quay lại từ AddClassInstance
        classInstanceList = dbHelper.getClassInstancesByClassId(classId);
        classInstanceAdapter.notifyDataSetChanged();  // Cập nhật lại RecyclerView
    }
}