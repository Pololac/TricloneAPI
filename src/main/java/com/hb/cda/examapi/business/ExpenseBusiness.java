package com.hb.cda.examapi.business;

import com.hb.cda.examapi.controller.dto.PostExpenseDTO;
import com.hb.cda.examapi.controller.dto.ExpenseListDTO;

import java.util.Optional;

public interface ExpenseBusiness {
    ExpenseListDTO getExpensesByAccount(String accountId,
                                        Optional<String> payerId,
                                        Optional<Double> minAmount,
                                        Optional<Double> maxAmount);

    PostExpenseDTO saveExpense(String accountId, PostExpenseDTO dto);
}
