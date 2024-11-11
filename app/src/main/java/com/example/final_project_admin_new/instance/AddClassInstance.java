package com.example.final_project_admin_new.instance;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project_admin_new.R;
import com.example.final_project_admin_new.db.DatabaseHelper;
import com.example.final_project_admin_new.model.ClassInstance;
import com.example.final_project_admin_new.model.YogaClass;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.List;

public class AddClassInstance extends AppCompatActivity {

    // Các biến liên kết với các thành phần UI
    private TextInputEditText dateInput;
    private TextInputEditText teacherInput;
    private TextInputEditText commentsInput;
    private MaterialButton backButton;
    private MaterialButton saveButton;
    private DatabaseHelper dbHelper;
    private String date;
    private int classId;
    private TextInputLayout dateInputLayout;
    private TextView yogaClassTitle,yogaClassDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_class_instance);
        dbHelper = new DatabaseHelper(this);
        //lay du lieu lop hoc
        classId = getIntent().getIntExtra("CLASS_ID", -1);
        Cursor yogaClass = dbHelper.getClassDetails(classId);
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

        // Khởi tạo các biến UI
        dateInput = findViewById(R.id.dateInput);
        teacherInput = findViewById(R.id.teacherInput);
        commentsInput = findViewById(R.id.commentsInput);
        dateInputLayout = findViewById(R.id.dateInputLayout);

        backButton = findViewById(R.id.backButton);
        saveButton = findViewById(R.id.saveButton);


        dateInput.setOnClickListener(v -> {
            if (yogaClass != null && yogaClass.moveToFirst()) {
                String dayOfWeek = yogaClass.getString(yogaClass.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DAY_OF_WEEK));
                setupDatePickerWithDayOfWeek(dayOfWeek);
            }
        });
        // Thiết lập hành động cho các nút
        backButton.setOnClickListener(v -> finish());  // Quay lại trang trước
        saveButton.setOnClickListener(v -> saveClassInstance());  // Lưu lớp học
    }

    // Hàm lưu lớp học (thực hiện khi nhấn nút Save)
    private void saveClassInstance() {
        // Lấy dữ liệu từ các trường đầu vào
        String date = dateInput.getText().toString();
        String teacher = teacherInput.getText().toString();
        String comments = commentsInput.getText().toString();

        // Kiểm tra các trường đầu vào
        if (date.isEmpty() || teacher.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra nếu classId không hợp lệ
        if (classId == -1) {
            Toast.makeText(this, "Yoga class not found.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo một đối tượng ClassInstance mới
        ClassInstance newInstance = new ClassInstance(date, teacher, comments, classId);

        // Thêm ClassInstance vào cơ sở dữ liệu
        boolean isAdd = dbHelper.addClassInstance(newInstance, this);
        if (isAdd) {
            Toast.makeText(this, "Class instance saved successfully.", Toast.LENGTH_SHORT).show();

            // Chuyển về màn hình chính (MainActivity2)
            Intent intent = new Intent(AddClassInstance.this, MainActivity2.class);
            intent.putExtra("CLASS_ID", classId);  // Truyền classId
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed to save class instance.", Toast.LENGTH_SHORT).show();
        }
    }



    private void setupDatePickerWithDayOfWeek(String dayOfWeek) {
        int targetDayOfWeek = getCalendarDayOfWeek(dayOfWeek);
        Calendar calendar = Calendar.getInstance();
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int daysToAdd = (targetDayOfWeek - currentDayOfWeek + 7) % 7;
        calendar.add(Calendar.DAY_OF_YEAR, daysToAdd);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(selectedYear, selectedMonth, selectedDay);

            // Kiểm tra nếu ngày đã chọn không khớp với ngày trong tuần mong muốn
            if (selectedDate.get(Calendar.DAY_OF_WEEK) != targetDayOfWeek) {
                // Hiển thị lại DatePickerDialog với ngày hợp lệ tiếp theo
                setupDatePickerWithDayOfWeek(dayOfWeek);
                Toast.makeText(this, "Just only choose " + dayOfWeek, Toast.LENGTH_SHORT).show();
            } else {
                // Định dạng và hiển thị ngày đã chọn
                String date = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                dateInput.setText(date);
            }
        }, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }


    private int getCalendarDayOfWeek(String dayOfWeek) {
        switch (dayOfWeek.toLowerCase()) {
            case "sunday":
                return Calendar.SUNDAY;
            case "monday":
                return Calendar.MONDAY;
            case "tuesday":
                return Calendar.TUESDAY;
            case "wednesday":
                return Calendar.WEDNESDAY;
            case "thursday":
                return Calendar.THURSDAY;
            case "friday":
                return Calendar.FRIDAY;
            case "saturday":
                return Calendar.SATURDAY;
            default:
                return Calendar.SUNDAY; // Mặc định là Chủ Nhật
        }
    }
}
