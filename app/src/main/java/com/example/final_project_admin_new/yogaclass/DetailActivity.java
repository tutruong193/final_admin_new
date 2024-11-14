package com.example.final_project_admin_new.yogaclass;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project_admin_new.R;
import com.example.final_project_admin_new.db.DatabaseHelper;
import com.example.final_project_admin_new.model.YogaClass;

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
        EdgeToEdge.enable(this);
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
    }
    private void updateClass() {
        String missingFields = checkRequiredFields();
        if (!missingFields.isEmpty()) {
            Toast.makeText(this, "Missing fields: " + missingFields, Toast.LENGTH_LONG).show();
            return;
        }
        String classType = etClassType.getSelectedItem().toString();
        String classTime = etClassTime.getText().toString();
        int duration = Integer.parseInt(etDuration.getText().toString());
        String dayOfWeek = etDayOfWeek.getSelectedItem().toString();
        int capacity = Integer.parseInt(etCapacity.getText().toString());
        double price = Double.parseDouble(etPrice.getText().toString());
        String description = etDescription.getText().toString();

        String confirmationMessage = "Day: " + dayOfWeek + "\n" +
                "Time: " + classTime + "\n" +
                "Capacity: " + capacity + "\n" +
                "Duration: " + duration + " mins\n" +
                "Price: $" + price + "\n" +
                "Class Type: " + classType + "\n" +
                "Description: " + description;
        new AlertDialog.Builder(this)
                .setTitle("Confirm Add Class")
                .setMessage("Are you sure you want to add this yoga class?\n\n" + confirmationMessage)
                .setPositiveButton("Yes", (dialog, which) -> {
                    boolean isUpdated = DB.updateClassDetails(classId, dayOfWeek, classTime, capacity, duration, price, classType, description, this);
                    if (isUpdated) {
                        Toast.makeText(this, "Class updated successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Failed to update class", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                })
                .create()
                .show();
    }
    private void loadClassDetails(int classId) {
        Cursor cursor = DB.getClassDetails(classId);

        if (cursor != null && cursor.moveToFirst()) {
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
            Toast.makeText(this, "Can not load the instance data", Toast.LENGTH_SHORT).show();
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
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.days_of_week, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (TimePicker view, int selectedHour, int selectedMinute) -> {
                    String time = String.format("%02d:%02d", selectedHour, selectedMinute);

                    timeEditText.setText(time);
                    boolean isPickedTime = true;
                }, hour, minute, true);

        timePickerDialog.show();
    }
    private String checkRequiredFields() {
        StringBuilder missingFields = new StringBuilder();

        if (TextUtils.isEmpty(etCapacity.getText().toString())) {
            missingFields.append("Capacity, ");
        }
        if (TextUtils.isEmpty(etDuration.getText().toString())) {
            missingFields.append("Duration, ");
        }
        if (TextUtils.isEmpty(etPrice.getText().toString())) {
            missingFields.append("Price, ");
        }
        if (TextUtils.isEmpty(etDayOfWeek.getSelectedItem().toString())) {
            missingFields.append("Day of Week, ");
        }
        if (TextUtils.isEmpty(etClassType.getSelectedItem().toString())) {
            missingFields.append("Class Type, ");
        }
        if (TextUtils.isEmpty(etClassTime.getText().toString())) {
            missingFields.append("Time, ");
        }
        if (missingFields.length() > 0) {
            missingFields.delete(missingFields.length() - 2, missingFields.length());
        }
        return missingFields.toString();
    }
}
