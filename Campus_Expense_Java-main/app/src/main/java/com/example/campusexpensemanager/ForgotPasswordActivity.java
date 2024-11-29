package com.example.campusexpensemanager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusexpensemanager.DatabaseSQLite.AccountDB;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText emailET;
    private EditText newPasswordET;
    private AccountDB dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailET = findViewById(R.id.forgot_email);
        newPasswordET = findViewById(R.id.new_password);
        dbHelper = new AccountDB(this);

        Button resetPasswordButton = findViewById(R.id.btnResetPassword);
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString().trim();
                String newPassword = newPasswordET.getText().toString().trim();
                if (updatePassword(email, newPassword)) {
                    Toast.makeText(ForgotPasswordActivity.this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Quay lại activity trước đó
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Invalid email or update failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean updatePassword(String email, String newPassword) {
        // Gọi phương thức cập nhật mật khẩu từ dbHelper
        return dbHelper.updatePassword(email, newPassword);
    }
}