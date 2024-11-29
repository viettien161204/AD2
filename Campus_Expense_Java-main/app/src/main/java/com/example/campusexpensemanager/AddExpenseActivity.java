package com.example.campusexpensemanager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusexpensemanager.DatabaseSQLite.ExpenseDB;
import com.example.campusexpensemanager.DatabaseSQLite.BudgetDB;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity {
    private EditText addDescription;
    private EditText addDate;
    private EditText addAmount;
    private Button btnAdd;
    private Button btnBack;
    private Spinner categorySpinner;
    private EditText editOtherCategory;

    private ExpenseDB dbHelper;
    private BudgetDB budgetDB;
    private double currentBudget;

    private int expenseId = -1;
    private Calendar calendar;
    private String selectedCategory;
    private int accountId; // Thêm accountId

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_expense_activity);

        addDescription = findViewById(R.id.add_description);
        addDate = findViewById(R.id.add_date);
        addAmount = findViewById(R.id.add_amount);
        btnAdd = findViewById(R.id.button_add);
        btnBack = findViewById(R.id.add_back);
        categorySpinner = findViewById(R.id.spinner_category);
        editOtherCategory = findViewById(R.id.edit_other_category);

        dbHelper = new ExpenseDB(this);
        budgetDB = new BudgetDB(this);
        calendar = Calendar.getInstance();

        accountId = getIntent().getIntExtra("account_id", -1); // Lấy accountId

        if (getIntent().hasExtra("expense_id")) {
            expenseId = getIntent().getIntExtra("expense_id", -1);
        }

        setupSpinner();

        btnAdd.setOnClickListener(v -> saveExpense());
        addDate.setOnClickListener(v -> showDateTimePicker());
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(AddExpenseActivity.this, ExpenseTracking.class);
            intent.putExtra("account_id", accountId); // Truyền accountId
            startActivity(intent);
        });
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.expense_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
                if (selectedCategory.equals("Other")) {
                    editOtherCategory.setVisibility(View.VISIBLE);
                } else {
                    editOtherCategory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = "";
            }
        });
    }

    private void showDateTimePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                            AddExpenseActivity.this,
                            (view1, hourOfDay, minute) -> {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);

                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                                addDate.setText(dateFormat.format(calendar.getTime()));
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                    );
                    timePickerDialog.show();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void saveExpense() {
        String description = addDescription.getText().toString().trim();
        String date = addDate.getText().toString().trim();
        String amountStr = addAmount.getText().toString().trim();

        if (description.isEmpty() || date.isEmpty() || amountStr.isEmpty() || selectedCategory == null || selectedCategory.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedCategory.equals("Other")) {
            selectedCategory = editOtherCategory.getText().toString().trim();
            if (selectedCategory.isEmpty()) {
                Toast.makeText(this, "Please enter a category for 'Other'", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ExpenseDB.COLUMM_DESCRIPTION, description);
        values.put(ExpenseDB.COLUMM_DATE, date);
        values.put(ExpenseDB.COLUMM_AMOUNT, amount);
        values.put(ExpenseDB.COLUMN_CATEGORY, selectedCategory);
         // Lưu accountId

        if (expenseId == -1) {
            db.insert(ExpenseDB.TABLE_EXPENSES, null, values);
            Toast.makeText(this, "Expense added", Toast.LENGTH_SHORT).show();
        } else {
            db.update(ExpenseDB.TABLE_EXPENSES, values, ExpenseDB.COLUMM_ID + " = ?", new String[]{String.valueOf(expenseId)});
            Toast.makeText(this, "Expense updated", Toast.LENGTH_SHORT).show();
        }

        db.close();
        Intent intent = new Intent(AddExpenseActivity.this, ExpenseTracking.class);
        intent.putExtra("account_id", accountId); // Truyền accountId
        startActivity(intent);
    }
}