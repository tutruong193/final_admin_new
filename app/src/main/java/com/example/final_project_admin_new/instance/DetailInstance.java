package com.example.final_project_admin_new.instance;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project_admin_new.R;
import com.example.final_project_admin_new.adapter.ClassInstanceAdapter;
import com.example.final_project_admin_new.db.DatabaseHelper;
import com.example.final_project_admin_new.model.ClassInstance;
import com.example.final_project_admin_new.yogaclass.DetailActivity;
import com.example.final_project_admin_new.yogaclass.MainActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.List;

public class DetailInstance extends AppCompatActivity {
    private TextInputEditText dateInput;
    private TextInputEditText teacherInput;
    private TextInputEditText commentsInput;
    private MaterialButton backButton;
    private MaterialButton updateBtn;
    private DatabaseHelper dbHelper;
    private String date;
    private int classId, yogaclassId;
    private TextInputLayout dateInputLayout;
    private TextView yogaClassTitle,yogaClassDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_instance);
        classId = getIntent().getIntExtra("CLASS_ID", -1);
        yogaclassId = getIntent().getIntExtra("YOGA_CLASS_ID", -1);
        dbHelper = new DatabaseHelper(this);
        //lay du lieu lop hoc
        Cursor yogaClass = dbHelper.getClassDetails(yogaclassId);
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
        dateInput = findViewById(R.id.dateInput);
        teacherInput = findViewById(R.id.teacherInput);
        commentsInput = findViewById(R.id.commentsInput);

        backButton = findViewById(R.id.backButton);
        updateBtn = findViewById(R.id.updateBtn);

        loadInstanceDetail(classId);
        dateInput.setOnClickListener(v -> {
            if (yogaClass != null && yogaClass.moveToFirst()) {
                String dayOfWeek = yogaClass.getString(yogaClass.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DAY_OF_WEEK));
                setupDatePickerWithDayOfWeek(dayOfWeek);
            }
        });
        // Thiết lập hành động cho các nút
        backButton.setOnClickListener(v -> finish());  // Quay lại trang trước
        updateBtn.setOnClickListener(v -> updateClassInstance());  // Lưu lớp học
    }
    private void updateClassInstance() {
        // Lấy dữ liệu từ các EditText
        String date = dateInput.getText().toString().trim();
        String teacher = teacherInput.getText().toString().trim();
        String comments = commentsInput.getText().toString().trim();

        // Kiểm tra xem tất cả các trường nhập liệu có hợp lệ hay không
        if (date.isEmpty() || teacher.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật thông tin instance vào cơ sở dữ liệu
        boolean isUpdated = dbHelper.updateInstanceDetail(classId, date, teacher, comments, this);

        if (isUpdated) {
            Toast.makeText(this, "Updated successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity2.class);
            intent.putExtra("CLASS_ID", yogaclassId);
            startActivity(intent);
           // Quay lại trang trước sau khi cập nhật thành công
        } else {
            Toast.makeText(this, "Updated fail", Toast.LENGTH_SHORT).show();
        }
    }
    private void loadInstanceDetail(int instanceId) {
        // Lấy thông tin chi tiết của instance từ database
        Cursor cursor = dbHelper.getInstaceDetail(instanceId);

        if (cursor != null && cursor.moveToFirst()) {
            // Thiết lập giá trị cho các EditText và Spinner

            // Lấy và thiết lập giá trị cho StartTime (EditText)
            int date = cursor.getColumnIndex(DatabaseHelper.COLUMN_INSTANCE_DATE);
            if (date != -1) {
                dateInput.setText(cursor.getString(date));  // Cập nhật EditText cho startTime
            }

            // Lấy và thiết lập giá trị cho EndTime (EditText)
            int teacher = cursor.getColumnIndex(DatabaseHelper.COLUMN_INSTANCE_TEACHER);
            if (teacher != -1) {
                teacherInput.setText(cursor.getString(teacher));
            }

            // Lấy và thiết lập giá trị cho Capacity (EditText)
            int comment = cursor.getColumnIndex(DatabaseHelper.COLUMN_INSTANCE_COMMENTS);
            if (comment != -1) {
                commentsInput.setText(cursor.getString(comment));
            }

            cursor.close();  // Đóng cursor khi hoàn thành
        } else {
            // Nếu không thể lấy dữ liệu
            Toast.makeText(this, "Cannot load the data", Toast.LENGTH_SHORT).show();
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