package com.hb.cda.examapi.business.impl;

import com.hb.cda.examapi.business.ExpenseBusiness;
import com.hb.cda.examapi.business.pojo.ExpenseSummary;
import com.hb.cda.examapi.controller.dto.PostExpenseDTO;
import com.hb.cda.examapi.controller.dto.mapper.ExpenseMapper;
import com.hb.cda.examapi.entity.Account;
import com.hb.cda.examapi.entity.Expense;
import com.hb.cda.examapi.entity.User;
import com.hb.cda.examapi.repository.AccountRepository;
import com.hb.cda.examapi.repository.ExpenseRepository;
import com.hb.cda.examapi.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseBusinessImpl implements ExpenseBusiness {
    private ExpenseRepository expenseRepo;
    private AccountRepository accountRepo;
    private UserRepository userRepo;

    public ExpenseBusinessImpl(ExpenseRepository expenseRepo, AccountRepository accountRepo, UserRepository userRepo, ExpenseMapper mapper) {
        this.expenseRepo = expenseRepo;
        this.accountRepo = accountRepo;
        this.userRepo = userRepo;
    }

    @Override
    public ExpenseSummary getExpenseSummaryByAccount(String accountId,
                                               Optional<String> payerId,
                                               Optional<Double> minAmount,
                                               Optional<Double> maxAmount) {
        // Récupération des dépenses du groupe
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        List<Expense> expenses = expenseRepo.findByAccountAndFilters(
                account,
                payerId.orElse(null),
                minAmount.orElse(null),
                maxAmount.orElse(null));

        // Renvoi du DTO incluant le total
        return new ExpenseSummary(expenses);
    }

    @Override
    public Expense saveExpense(String accountId, User user, Expense expense) {
        // Récupération du groupe et du user concernés à partir du DTO
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        // Ajout des propriétés liées aux relations
        expense.setAccount(account);
        expense.setPayer(user);

        // Gestion cas où la date non envoyée par le front
        if (expense.getDate() == null) {
            expense.setDate(LocalDateTime.now(ZoneId.of("Europe/Paris")));
        }

        expenseRepo.save(expense);

        return expense;
    }
}
