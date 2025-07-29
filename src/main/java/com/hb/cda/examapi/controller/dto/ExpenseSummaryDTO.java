package com.hb.cda.examapi.controller.dto;

import java.util.List;

public class ExpenseSummaryDTO {
    private List<PostExpenseDTO> expenses;
    private Double total;

    public ExpenseSummaryDTO() {
    }

    public ExpenseSummaryDTO(List<PostExpenseDTO> expenses, Double total) {
        this.expenses = expenses;
        this.total = total;
    }

    public List<PostExpenseDTO> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<PostExpenseDTO> expenses) {
        this.expenses = expenses;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
