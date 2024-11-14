package com.example.final_project_admin_new;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project_admin_new.db.DatabaseHelper;
import com.example.final_project_admin_new.yogaclass.MainActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SplashAcitivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_acitivity);
        FirebaseApp.initializeApp(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("them");

        myRef.setValue("thanh cong");

        View loadingImage = findViewById(R.id.loadingImage);
        View loginForm = findViewById(R.id.loginForm);
        EditText usernameEditText = findViewById(R.id.username);
        EditText passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);
        DatabaseHelper db = new DatabaseHelper(this);
        db.getDataFromFireBaseToSQL();
        new Handler().postDelayed(() -> {
            loadingImage.setVisibility(View.GONE);
            loginForm.setVisibility(View.VISIBLE);
        }, 2000);

        loginButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            Intent intent = new Intent(SplashAcitivity.this, MainActivity.class);
            startActivity(intent);
            finish();
//            if (username.isEmpty() || password.isEmpty()) {
//                Toast.makeText(SplashAcitivity.this, "Enter information", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            if (username.equals("admin") && password.equals("admin")) {
//
//                Toast.makeText(SplashAcitivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(SplashAcitivity.this, "Username or password is not correct", Toast.LENGTH_SHORT).show();
//            }
        });
    }
}