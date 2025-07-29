package com.hb.cda.examapi.controller;

import com.hb.cda.examapi.business.ExpenseBusiness;
import com.hb.cda.examapi.business.pojo.ExpenseSummary;
import com.hb.cda.examapi.controller.dto.ExpenseSummaryDTO;
import com.hb.cda.examapi.controller.dto.PostExpenseDTO;
import com.hb.cda.examapi.controller.dto.mapper.ExpenseMapper;
import com.hb.cda.examapi.entity.Expense;
import com.hb.cda.examapi.entity.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts/{accountId}/expenses")
public class ExpenseController {
    private ExpenseBusiness expenseBusiness;
    private ExpenseMapper mapper;

    public ExpenseController(ExpenseBusiness expenseBusiness, ExpenseMapper mapper) {
        this.expenseBusiness = expenseBusiness;
        this.mapper = mapper;
    }

    @GetMapping
    public ExpenseSummaryDTO listExpenses(
            @PathVariable String accountId,
            @RequestParam Optional<String> payerId,
            @RequestParam Optional<Double> minAmount,
            @RequestParam Optional<Double> maxAmount) {

        ExpenseSummary summary = expenseBusiness.getExpenseSummaryByAccount(accountId, payerId, minAmount, maxAmount);

        List<PostExpenseDTO> dtoList = summary.getExpenses().stream()
                .map(mapper::convertToDTO)
                .collect(Collectors.toList());

        ExpenseSummaryDTO dto = new ExpenseSummaryDTO(dtoList, summary.getTotal());

        return dto;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostExpenseDTO addExpense(@PathVariable String accountId, @AuthenticationPrincipal User user, @RequestBody @Valid PostExpenseDTO dto) {
        Expense expense = expenseBusiness.saveExpense(accountId, user, mapper.convertFromPost(dto));
        return mapper.convertToDTO(expense);
    }

}
