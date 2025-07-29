package com.hb.cda.examapi.business.pojo;

import com.hb.cda.examapi.entity.Expense;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExpenseSummary {
    private List<Expense> expenses;
    private Double total;

    public ExpenseSummary() {}
    public ExpenseSummary(List<Expense> expenses) {
        this.expenses = expenses;
        // Calcul de la somme des dépenses
        this.total = expenses.stream().mapToDouble(Expense::getAmount).sum();
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
