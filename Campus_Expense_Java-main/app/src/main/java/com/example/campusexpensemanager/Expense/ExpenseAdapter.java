package com.example.campusexpensemanager.Expense;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanager.ExpenseTracking;
import com.example.campusexpensemanager.R;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {
    private List<Expense> expenses;
    private Context context;

    public ExpenseAdapter(ExpenseTracking expenseTracking, List<Expense> expenses) {
        this.expenses = expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenses.get(position);
        holder.textExpenseTitle.setText(expense.getDescription());
        holder.textExpenseAmount.setText("$" + expense.getAmount());
        holder.textExpenseDate.setText(expense.getDate());
        holder.textExpenseCategory.setText(expense.getCategory());

        holder.buttonDeleteExpense.setOnClickListener(v -> {
            if (context instanceof ExpenseTracking) {
                ((ExpenseTracking) context).deleteExpense(expense.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView textExpenseTitle, textExpenseAmount, textExpenseDate, textExpenseCategory;
        Button buttonEditExpense, buttonDeleteExpense;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            textExpenseTitle = itemView.findViewById(R.id.text_expense_title);
            textExpenseAmount = itemView.findViewById(R.id.text_expense_amount);
            textExpenseDate = itemView.findViewById(R.id.text_expense_date);
            textExpenseCategory = itemView.findViewById(R.id.text_expense_category);
            buttonEditExpense = itemView.findViewById(R.id.button_edit_expense);
            buttonDeleteExpense = itemView.findViewById(R.id.button_delete_expense);
        }
    }
}