package com.example.campusexpensemanager.DatabaseSQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

public class AccountDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "account.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_FULLNAME = "fullname";
    public static final String COLUMN_STUDENTID = "studentid";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_FULLNAME + " TEXT, " +
                    COLUMN_STUDENTID + " TEXT, " +
                    COLUMN_EMAIL + " TEXT UNIQUE, " +  // Đảm bảo email là duy nhất
                    COLUMN_PASSWORD + " TEXT " +
                    "); ";

    public AccountDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Phương thức cập nhật mật khẩu
    public boolean updatePassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPassword);

        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = { email };

        int count = db.update(TABLE_USERS, values, selection, selectionArgs);
        return count > 0; // Trả về true nếu có bản ghi được cập nhật
    }

    // Phương thức kiểm tra email có tồn tại không
    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = { COLUMN_EMAIL };
        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query(TABLE_USERS, projection, selection, selectionArgs, null, null, null);
        boolean exists = cursor.getCount() > 0; // Kiểm tra xem có dòng nào không
        cursor.close();
        return exists;
    }

    // Phương thức kiểm tra studentId và email có tồn tại không
    public boolean isStudentIdAndEmailExists(String studentId, String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = { COLUMN_STUDENTID, COLUMN_EMAIL };
        String selection = COLUMN_STUDENTID + " = ? OR " + COLUMN_EMAIL + " = ?";
        String[] selectionArgs = { studentId, email };

        Cursor cursor = db.query(TABLE_USERS, projection, selection, selectionArgs, null, null, null);
        boolean exists = cursor.getCount() > 0; // Kiểm tra xem có dòng nào không
        cursor.close();
        return exists;
    }

    // Phương thức lấy ID tài khoản dựa trên email
    public int getAccountIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = { COLUMN_ID };
        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query(TABLE_USERS, projection, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            cursor.close();
            return id;
        }
        return -1; // Không tìm thấy
    }
}