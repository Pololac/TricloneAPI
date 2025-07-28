package com.hb.cda.examapi.controller;

import com.hb.cda.examapi.business.ExpenseBusiness;
import com.hb.cda.examapi.controller.dto.PostExpenseDTO;
import com.hb.cda.examapi.controller.dto.ExpenseListDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/accounts/{accountId}/expenses")
public class ExpenseController {
    private ExpenseBusiness expenseBusiness;

    public ExpenseController(ExpenseBusiness expenseBusiness) {
        this.expenseBusiness = expenseBusiness;
    }

    @GetMapping
    public ExpenseListDTO listExpenses(
            @PathVariable String accountId,
            @RequestParam Optional<String> payerId,
            @RequestParam Optional<Double> minAmount,
            @RequestParam Optional<Double> maxAmount) {

        return expenseBusiness.getExpensesByAccount(accountId, payerId, minAmount, maxAmount);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostExpenseDTO addExpense(@PathVariable String accountId, @RequestBody @Valid PostExpenseDTO dto) {
        // On rattache l’Expense au bon compte (fourni via l'URL)
        return expenseBusiness.saveExpense(accountId, dto);
    }

}
