package com.hb.cda.examapi.business.impl;

import com.hb.cda.examapi.business.ExpenseBusiness;
import com.hb.cda.examapi.controller.dto.PostExpenseDTO;
import com.hb.cda.examapi.controller.dto.ExpenseListDTO;
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
import java.util.stream.Collectors;

@Service
public class ExpenseBusinessImpl implements ExpenseBusiness {
    private ExpenseRepository expenseRepo;
    private AccountRepository accountRepo;
    private UserRepository userRepo;
    private ExpenseMapper mapper;

    public ExpenseBusinessImpl(ExpenseRepository expenseRepo, AccountRepository accountRepo, UserRepository userRepo, ExpenseMapper mapper) {
        this.expenseRepo = expenseRepo;
        this.accountRepo = accountRepo;
        this.userRepo = userRepo;
        this.mapper = mapper;
    }

    @Override
    public ExpenseListDTO getExpensesByAccount(String accountId,
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

        // Conversion en DTO
        List<PostExpenseDTO> dtos = expenses.stream()
                .map(mapper::convertToDTO)
                .collect(Collectors.toList());

        // Calcul de la somme des dépenses
        double total = expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();

        // Renvoi du DTO incluant le total
        return new ExpenseListDTO(dtos, total);
    }

    @Override
    public PostExpenseDTO saveExpense(String accountId, PostExpenseDTO dto) {
        Expense expense = mapper.convertFromPost(dto);

        // Récupération du groupe et du user concernés à partir du DTO
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        User payer = userRepo.findById(dto.getPayerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Ajout des propriétés liées aux relations
        expense.setAccount(account);
        expense.setPayer(payer);

        // Gestion cas où la date non envoyée par le front
        if (expense.getDate() == null) {
            expense.setDate(LocalDateTime.now(ZoneId.of("Europe/Paris")));
        }

        expenseRepo.save(expense);

        return mapper.convertToDTO(expense);
    }
}
