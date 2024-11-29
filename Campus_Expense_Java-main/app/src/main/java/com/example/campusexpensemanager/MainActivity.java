package com.example.campusexpensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusexpensemanager.DatabaseSQLite.BudgetDB;
import com.example.campusexpensemanager.DatabaseSQLite.ExpenseDB;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private BarChart barChart;
    private LineChart lineChart;
    private ExpenseDB expenseDB;
    private BudgetDB budgetDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        barChart = findViewById(R.id.barChart);
        lineChart = findViewById(R.id.lineChart);
        expenseDB = new ExpenseDB(this);
        budgetDB = new BudgetDB(this);

        Button btnExpenseTracking = findViewById(R.id.fuction_button1);
        Button logoutButton = findViewById(R.id.btnLogout);

        // Load data for the charts
        loadChartData();
        loadLineChartData();

        btnExpenseTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExpenseTracking.class);
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadChartData() {
        ArrayList<BarEntry> entries = new ArrayList<>();

        float totalExpenses = expenseDB.getTotalExpenses();
        float totalBudget = budgetDB.getTotalBudget();

        entries.add(new BarEntry(0, totalExpenses, "Total Expenses"));
        entries.add(new BarEntry(1, totalBudget, "Total Budget"));

        BarDataSet dataSet = new BarDataSet(entries, "Budget vs Expenses");
        BarData barData = new BarData(dataSet);

        barChart.setData(barData);
        barChart.invalidate(); // refresh
    }

    private void loadLineChartData() {
        Map<String, Float> dailyExpenses = expenseDB.getDailyExpenses();
        List<Entry> lineEntries = new ArrayList<>();
        int index = 0;

        for (Map.Entry<String, Float> entry : dailyExpenses.entrySet()) {
            lineEntries.add(new Entry(index++, entry.getValue()));
        }

        LineDataSet lineDataSet = new LineDataSet(lineEntries, "Daily Expenses");
        lineDataSet.setValueTextSize(10f);
        lineDataSet.setColor(getResources().getColor(R.color.Blue)); // Thay đổi màu sắc nếu cần

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineChart.invalidate(); // refresh
    }
}