package com.hb.cda.examapi.business;

import com.hb.cda.examapi.business.pojo.ExpenseSummary;
import com.hb.cda.examapi.controller.dto.PostExpenseDTO;
import com.hb.cda.examapi.entity.Expense;
import com.hb.cda.examapi.entity.User;

import java.util.Optional;

public interface ExpenseBusiness {
    ExpenseSummary getExpenseSummaryByAccount(String accountId,
                                        Optional<String> payerId,
                                        Optional<Double> minAmount,
                                        Optional<Double> maxAmount);

    Expense saveExpense(String accountId, User user, Expense expense);
}
