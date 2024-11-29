package com.example.campusexpensemanager.DatabaseSQLite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BudgetDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "budget.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_BUDGET = "budget";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_AMOUNT = "amount";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_BUDGET + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_AMOUNT + " REAL " +
                    ");";

    public BudgetDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGET);
        onCreate(db);
    }

    // Phương thức lấy tổng ngân sách
    public float getTotalBudget() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(amount) FROM " + TABLE_BUDGET, null);
        float total = 0;

        if (cursor.moveToFirst()) {
            total = cursor.getFloat(0);
        }
        cursor.close();
        return total;
    }

    // Phương thức thêm ngân sách
    public void addBudget(float amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO " + TABLE_BUDGET + " (amount) VALUES (?)", new Object[]{amount});
    }

    // Phương thức cập nhật ngân sách
    public void updateBudget(float amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_BUDGET + " SET amount = ?", new Object[]{amount});
    }

    // Phương thức xóa ngân sách
    public void deleteBudget() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_BUDGET);
    }
}