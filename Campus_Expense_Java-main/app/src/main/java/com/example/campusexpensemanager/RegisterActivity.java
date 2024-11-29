package com.example.campusexpensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.campusexpensemanager.Account.Account;
import com.example.campusexpensemanager.DatabaseSQLite.AccountDB;

public class RegisterActivity extends AppCompatActivity {
    private EditText fullnameET;
    private EditText studentidET;
    private EditText emailET;
    private EditText passwordET;
    private AccountDB dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullnameET = findViewById(R.id.register_fullname);
        studentidET = findViewById(R.id.register_studentid);
        emailET = findViewById(R.id.register_email);
        passwordET = findViewById(R.id.register_password);
        dbHelper = new AccountDB(this);

        Button registerButton = findViewById(R.id.btnRegisterSubmit);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Account account = new Account();
                account.setFullname(fullnameET.getText().toString().trim());
                account.setStudentId(studentidET.getText().toString().trim());
                account.setEmail(emailET.getText().toString().trim());
                account.setPassword(passwordET.getText().toString().trim());

                if (isValidInput(account)) {
                    saveToDatabase(account);
                }
            }
        });
    }

    private boolean isValidInput(Account account) {
        if (account.getFullname().isEmpty() || account.getStudentId().isEmpty() ||
                account.getEmail().isEmpty() || account.getPassword().isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveToDatabase(Account account) {
        if (dbHelper.isStudentIdAndEmailExists(account.getStudentId(), account.getEmail())) {
            Toast.makeText(this, "Student ID or Email already exists", Toast.LENGTH_SHORT).show();
            return; // Không lưu nếu đã tồn tại
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AccountDB.COLUMN_FULLNAME, account.getFullname());
        values.put(AccountDB.COLUMN_STUDENTID, account.getStudentId());
        values.put(AccountDB.COLUMN_EMAIL, account.getEmail());
        values.put(AccountDB.COLUMN_PASSWORD, account.getPassword());

        long newRowId = db.insert(AccountDB.TABLE_USERS, null, values);
        if (newRowId != -1) {
            Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed to Register", Toast.LENGTH_SHORT).show();
        }
    }
}