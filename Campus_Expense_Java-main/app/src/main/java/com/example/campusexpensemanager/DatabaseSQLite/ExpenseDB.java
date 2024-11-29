package com.example.campusexpensemanager.DatabaseSQLite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.campusexpensemanager.Expense.Expense;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExpenseDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "expenses.db";
    private static final int DATABASE_VERSION = 2; // Tăng phiên bản cơ sở dữ liệu

    public static final String TABLE_EXPENSES = "expenses";
    public static final String COLUMM_ID = "_id";
    public static final String COLUMM_DESCRIPTION = "description";
    public static final String COLUMM_DATE = "date";
    public static final String COLUMM_AMOUNT = "amount";
    public static final String COLUMN_CATEGORY = "category"; // Thêm cột loại chi phí

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_EXPENSES + " (" +
                    COLUMM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMM_DESCRIPTION + " TEXT, " +
                    COLUMM_DATE + " TEXT, " +
                    COLUMM_AMOUNT + " REAL, " +
                    COLUMN_CATEGORY + " TEXT" + // Thêm cột loại chi phí
                    ");";

    public ExpenseDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Nếu phiên bản tăng lên, cần thêm cột mới
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_EXPENSES + " ADD COLUMN " + COLUMN_CATEGORY + " TEXT");
        }
    }

    public float getTotalExpenses() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(amount) FROM " + TABLE_EXPENSES, null);
        float total = 0;

        if (cursor.moveToFirst()) {
            total = cursor.getFloat(0);
        }
        cursor.close();
        return total;
    }

    public Map<String, Float> getDailyExpenses() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT date, SUM(amount) FROM " + TABLE_EXPENSES + " GROUP BY date", null);
        Map<String, Float> dailyExpenses = new HashMap<>();

        while (cursor.moveToNext()) {
            String date = cursor.getString(0); // Lấy ngày
            float amount = cursor.getFloat(1); // Lấy tổng chi tiêu cho ngày
            dailyExpenses.put(date, amount);
        }
        cursor.close();
        return dailyExpenses;
    }
    @SuppressLint("Range")
    // Phương thức lấy danh sách chi tiêu theo accountId
    public ArrayList<Expense> getExpensesByAccountId(int accountId) {
        ArrayList<Expense> expenses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EXPENSES + " WHERE account_id = ?", new String[]{String.valueOf(accountId)});

        if (cursor.moveToFirst()) {
            do {
                 int id = cursor.getInt(cursor.getColumnIndex(COLUMM_ID));
                String description = cursor.getString(cursor.getColumnIndex(COLUMM_DESCRIPTION));
                String date = cursor.getString(cursor.getColumnIndex(COLUMM_DATE));
                float amount = cursor.getFloat(cursor.getColumnIndex(COLUMM_AMOUNT));
                String category = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY));

                Expense expense = new Expense(id, description, date, amount, category);
                expenses.add(expense);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return expenses;
    }

    // Phương thức xóa một chi tiêu theo ID
    public void deleteExpense(int expenseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXPENSES, COLUMM_ID + " = ?", new String[]{String.valueOf(expenseId)});
    }
}