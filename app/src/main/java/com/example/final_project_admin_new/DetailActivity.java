package com.example.final_project_admin_new;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project_admin_new.db.DatabaseHelper;

import java.util.Calendar;

public class DetailActivity extends AppCompatActivity {

    private Spinner etClassType,etDayOfWeek ;
    private TextView etClassTime;
    private EditText etDuration, etCapacity, etPrice, etDescription;
    private Button btnUpdate, btnBack;
    private DatabaseHelper DB;
    private int classId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        DB = new DatabaseHelper(this);



        etClassType = findViewById(R.id.typeOfClass);
        etClassTime = findViewById(R.id.inputTime);
        etDuration = findViewById(R.id.inputDuration);
        etDayOfWeek = findViewById(R.id.inputDay);
        etCapacity = findViewById(R.id.inputCapacity);
        etPrice = findViewById(R.id.inputPrice);
        etDescription = findViewById(R.id.inputDescription);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnBack = findViewById(R.id.btnBack);
        showDropdownList(etDayOfWeek);
        setTypeClass(etClassType);
        Intent intent = getIntent();
        classId = intent.getIntExtra("CLASS_ID", -1);
        if (classId != -1) {
            loadClassDetails(classId);
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateClass();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        etClassTime.setOnClickListener(v -> showTimePickerDialog(etClassTime));
        // Initialize views
    }
    private void updateClass() {
        String classType = etClassType.getSelectedItem().toString();
        String classTime = etClassTime.getText().toString();
        int duration = Integer.parseInt(etDuration.getText().toString());
        String dayOfWeek = etDayOfWeek.getSelectedItem().toString();
        int capacity = Integer.parseInt(etCapacity.getText().toString());
        double price = Double.parseDouble(etPrice.getText().toString());
        String description = etDescription.getText().toString();

        boolean isUpdated = DB.updateClassDetails(classId, dayOfWeek, classTime, capacity, duration, price, classType, description);
        if (isUpdated) {
            Toast.makeText(this, "Class updated successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DetailActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Failed to update class", Toast.LENGTH_SHORT).show();
        }
    }
    private void loadClassDetails(int classId) {
        Cursor cursor = DB.getClassDetails(classId);

        if (cursor != null && cursor.moveToFirst()) {
            // Thiết lập giá trị cho các EditText và TextView
            int classTypeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_CLASS_TYPE);
            if (classTypeIndex != -1) {
                String classType = cursor.getString(classTypeIndex);
                setSpinnerSelection(etClassType, classType);
            }

            int classTimeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TIME);
            if (classTimeIndex != -1) {
                etClassTime.setText(cursor.getString(classTimeIndex));
            }

            int durationIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DURATION);
            if (durationIndex != -1) {
                etDuration.setText(cursor.getString(durationIndex));
            }

            int dayOfWeekIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DAY_OF_WEEK);
            if (dayOfWeekIndex != -1) {
                String dayOfWeek = cursor.getString(dayOfWeekIndex);
                setSpinnerSelection(etDayOfWeek, dayOfWeek);
            }

            int capacityIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_CAPACITY);
            if (capacityIndex != -1) {
                etCapacity.setText(cursor.getString(capacityIndex));
            }

            int priceIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PRICE);
            if (priceIndex != -1) {
                etPrice.setText(cursor.getString(priceIndex));
            }

            int descriptionIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION);
            if (descriptionIndex != -1) {
                etDescription.setText(cursor.getString(descriptionIndex));
            }

            cursor.close();
        } else {
            Toast.makeText(this, "Không thể tải thông tin lớp học", Toast.LENGTH_SHORT).show();
        }
    }
    private void setSpinnerSelection(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }
    private void showDropdownList(Spinner spinner){
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.days_of_week, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // Set a listener to handle selections
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item
                parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing if nothing is selected
            }
        });
    }
    private void setTypeClass(Spinner spinner){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.class_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
    private void showTimePickerDialog(TextView timeEditText) {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Initialize the TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (TimePicker view, int selectedHour, int selectedMinute) -> {
                    // Format the selected time and set it to EditText
                    String time = String.format("%02d:%02d", selectedHour, selectedMinute);

                    timeEditText.setText(time);
                    boolean isPickedTime = true;
                }, hour, minute, true); // Set true for 24-hour format, false for AM/PM format

        // Show the TimePickerDialog
        timePickerDialog.show();
    }
}
