package com.example.campusexpensemanager.Account;

public class Account {
    private int id; // Đổi tên biến thành chữ thường theo quy ước Java
    private String fullname;
    private String studentId; // Chỉnh sửa tên biến cho nhất quán
    private String email;
    private String password;

    // Các phương thức setter
    public void setId(int id) {
        this.id = id;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Các phương thức getter
    public int getId() {
        return id;
    }

    public String getFullname() {
        return fullname;
    }

    public String getStudentId() { // Chỉnh sửa tên phương thức cho nhất quán
        return studentId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}