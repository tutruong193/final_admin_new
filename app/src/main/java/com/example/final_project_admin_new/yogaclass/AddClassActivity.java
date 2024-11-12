package com.example.final_project_admin_new.yogaclass;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.final_project_admin_new.R;
import com.example.final_project_admin_new.db.DatabaseHelper;
import com.example.final_project_admin_new.model.YogaClass;

import java.util.Calendar;

public class AddClassActivity extends AppCompatActivity {

    EditText description, price, duration, capacity;
    TextView timeCourse;
    Button addClassBtn, btnBack;
    DatabaseHelper DB;
    private boolean isPickedTime;
    Spinner classType, dayOfWeek;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_class_acitivity);

        addClassBtn = findViewById(R.id.btnAdd);
        classType = findViewById(R.id.typeOfClass);
        price = findViewById(R.id.inputPrice);
        duration = findViewById(R.id.inputDuration);
        capacity = findViewById(R.id.inputCapacity);
        timeCourse = findViewById(R.id.inputTime);
        dayOfWeek = findViewById(R.id.inputDay);
        description = findViewById(R.id.inputDescription);
        isPickedTime = false;
        btnBack = findViewById(R.id.btnBack);
        DB = new DatabaseHelper(AddClassActivity.this);

        setupDropdown(dayOfWeek, R.array.days_of_week);
        setupDropdown(classType, R.array.class_types);

        addClassBtn.setOnClickListener(v -> {

            String missingFields = checkRequiredFields();
            if (!missingFields.isEmpty()) {
                Toast.makeText(this, "Missing fields: " + missingFields, Toast.LENGTH_LONG).show();
                return;
            }
            String selectedDay = dayOfWeek.getSelectedItem().toString();
            String timeOfCourse = timeCourse.getText().toString();
            int _capacity = Integer.parseInt(capacity.getText().toString());
            int _duration = Integer.parseInt(duration.getText().toString());
            double _price = Double.parseDouble(price.getText().toString());
            String _classType = classType.getSelectedItem().toString();
            String _description = description.getText().toString();

            String confirmationMessage = "Day: " + selectedDay + "\n" +
                    "Time: " + timeOfCourse + "\n" +
                    "Capacity: " + _capacity + "\n" +
                    "Duration: " + _duration + " mins\n" +
                    "Price: $" + _price + "\n" +
                    "Class Type: " + _classType + "\n" +
                    "Description: " + _description;
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Add Class")
                    .setMessage("Are you sure you want to add this yoga class?\n\n" + confirmationMessage)
                    .setPositiveButton("Yes", (dialog, which) -> {
                        YogaClass yogaClass = new YogaClass(
                                selectedDay, timeOfCourse, _capacity, _duration, _price, _classType, _description
                        );

                        DB.addYogaClass(yogaClass, this);
                        Toast.makeText(this, "Added class successfully!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AddClassActivity.this, MainActivity.class);
                        startActivity(intent);
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .create()
                    .show();
        });

        btnBack.setOnClickListener(v -> finish());

        timeCourse.setOnClickListener(v -> showTimePickerDialog());
    }

    private void setupDropdown(Spinner dropdown, int arrayResourceId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                arrayResourceId, android.R.layout.simple_dropdown_item_1line);
        dropdown.setAdapter(adapter);
    }


    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, selectedHour, selectedMinute) -> {
                    String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                    timeCourse.setText(time);
                    isPickedTime = true;
                }, hour, minute, true);

        timePickerDialog.show();
    }


    private String checkRequiredFields() {
        StringBuilder missingFields = new StringBuilder();

        if (TextUtils.isEmpty(capacity.getText().toString())) {
            missingFields.append("Capacity, ");
        }
        if (TextUtils.isEmpty(duration.getText().toString())) {
            missingFields.append("Duration, ");
        }
        if (TextUtils.isEmpty(price.getText().toString())) {
            missingFields.append("Price, ");
        }
        if (TextUtils.isEmpty(dayOfWeek.getSelectedItem().toString())) {
            missingFields.append("Day of Week, ");
        }
        if (TextUtils.isEmpty(classType.getSelectedItem().toString())) {
            missingFields.append("Class Type, ");
        }
        if (!isPickedTime) {
            missingFields.append("Time, ");
        }
        if (missingFields.length() > 0) {
            missingFields.delete(missingFields.length() - 2, missingFields.length());
        }
        return missingFields.toString();
    }
}
