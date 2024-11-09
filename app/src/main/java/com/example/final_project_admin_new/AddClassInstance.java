package com.example.final_project_admin_new;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsCompat;

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
    private Spinner yogaClassSpinner;  // Spinner cho danh sách lớp yoga
    private MaterialButton backButton;
    private MaterialButton saveButton;
    private DatabaseHelper dbHelper;
    private String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_class_instance);
        dbHelper = new DatabaseHelper(this);
        // Khởi tạo các biến UI
        dateInput = findViewById(R.id.dateInput);
        teacherInput = findViewById(R.id.teacherInput);
        commentsInput = findViewById(R.id.commentsInput);
        yogaClassSpinner = findViewById(R.id.yogaClassSpinner);

        backButton = findViewById(R.id.backButton);
        saveButton = findViewById(R.id.saveButton);

        setupYogaClassSpinner();

        dateInput.setOnClickListener(v -> setupDatePickerWithDayOfWeek(date));
        // Thiết lập hành động cho các nút
        backButton.setOnClickListener(v -> finish());  // Quay lại trang trước
        saveButton.setOnClickListener(v -> saveClassInstance());  // Lưu lớp học
    }

    // Hàm lưu lớp học (thực hiện khi nhấn nút Save)
    private void saveClassInstance() {
        String date = dateInput.getText().toString();
        String teacher = teacherInput.getText().toString();
        String comments = commentsInput.getText().toString();

        if (date.isEmpty() || teacher.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        YogaClass selectedYogaClass = (YogaClass) yogaClassSpinner.getSelectedItem();
        if (selectedYogaClass == null) {
            Toast.makeText(this, "Please select a yoga class.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new ClassInstance and save it
        ClassInstance newInstance = new ClassInstance(date, teacher, comments, selectedYogaClass.getId());
        dbHelper.addClassInstance(newInstance);
        Toast.makeText(this, "Class instance saved successfully.", Toast.LENGTH_SHORT).show();
        finish(); // Go back after saving
    }
    private void setupYogaClassSpinner() {
        // Lấy danh sách lớp học từ database
        List<YogaClass> yogaClassList = dbHelper.getAllYogaClasses();

        // Sử dụng Adapter để hiển thị dữ liệu lên Spinner
        ArrayAdapter<YogaClass> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, yogaClassList);
        adapter.setDropDownViewResource(R.layout.spinner_item_layout);
        yogaClassSpinner.setAdapter(adapter);
        yogaClassSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                YogaClass selectedYogaClass = yogaClassList.get(position);
                date = selectedYogaClass.getDayOfWeek(); // Lấy ngày trong tuần từ lớp học

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

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
            case "sunday": return Calendar.SUNDAY;
            case "monday": return Calendar.MONDAY;
            case "tuesday": return Calendar.TUESDAY;
            case "wednesday": return Calendar.WEDNESDAY;
            case "thursday": return Calendar.THURSDAY;
            case "friday": return Calendar.FRIDAY;
            case "saturday": return Calendar.SATURDAY;
            default: return Calendar.SUNDAY; // Mặc định là Chủ Nhật
        }
    }
}
