package com.example.final_project_admin_new.instance;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ClassInstanceAdapter classInstanceAdapter;
    private List<ClassInstance> classInstanceList;
    private DatabaseHelper dbHelper;
    private FloatingActionButton btnAdd;
    private LinearLayout backToYogaClassesLayout;
    private int classId;
    private TextView yogaClassTitle,yogaClassDetails, searchBtn;
    private TextInputEditText searchInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchInput = findViewById(R.id.searchInput);
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

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Không cần xử lý ở đây
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Khi có thay đổi văn bản, thực hiện tìm kiếm
                String teacherName = charSequence.toString().trim();
                performSearch(teacherName);  // Gọi hàm tìm kiếm
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Không cần xử lý ở đây
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
    private void performSearch(String teacherName) {
        Cursor cursor = dbHelper.searchClassesByTeacher(teacherName);
        List<ClassInstance> searchResults = new ArrayList<>(); // Tạo danh sách tạm để chứa kết quả tìm kiếm

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Tạo đối tượng ClassInstance từ dữ liệu trong Cursor
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INSTANCE_DATE));
                String teacher = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INSTANCE_TEACHER));
                String comment = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INSTANCE_COMMENTS));
                int yogaclassId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLASS_ID));

                // Thêm vào danh sách kết quả
                ClassInstance classInstance = new ClassInstance(date, teacher, comment, yogaclassId);
                searchResults.add(classInstance);
            } while (cursor.moveToNext());
            cursor.close();
        }

        // Cập nhật Adapter với dữ liệu mới
        classInstanceAdapter.setClassInstances(searchResults);
    }

}