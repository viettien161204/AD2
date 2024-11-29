package com.example.campusexpensemanager.Expense;

public class Expense {
    private int id;
    private String description;
    private String date;
    private double amount;
    private String category; // Thêm trường loại chi phí

    public Expense(int id, String description, String date, double amount, String category) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.amount = amount;
        this.category = category; // Khởi tạo loại chi phí
    }

    // Getter cho các trường
    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category; // Getter cho loại chi phí
    }

    // Setter cho loại chi phí
    public void setCategory(String category) {
        this.category = category;
    }
}