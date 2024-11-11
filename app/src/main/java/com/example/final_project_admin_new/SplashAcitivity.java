package com.example.final_project_admin_new;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project_admin_new.yogaclass.MainActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SplashAcitivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        FirebaseApp.initializeApp(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
        setContentView(R.layout.activity_splash_acitivity);
        View loadingImage = findViewById(R.id.loadingImage);
        View loginForm = findViewById(R.id.loginForm);
        EditText usernameEditText = findViewById(R.id.username);
        EditText passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);

        // Sử dụng Handler để hiển thị form đăng nhập sau 1 giây
        new Handler().postDelayed(() -> {
            loadingImage.setVisibility(View.GONE);
            loginForm.setVisibility(View.VISIBLE);
        }, 1000);

        loginButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            Intent intent = new Intent(SplashAcitivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Kết thúc MainActivity để không quay lại được
            // Kiểm tra tên đăng nhập và mật khẩu
//            if (username.equals("admin") && password.equals("admin")) {
//                // Chuyển đến HomeActivity nếu đăng nhập thành công
//
//                Toast.makeText(MainActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
//            } else {
//                // Hiển thị thông báo lỗi nếu đăng nhập thất bại
//                Toast.makeText(MainActivity.this, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
//            }
        });
    }
}