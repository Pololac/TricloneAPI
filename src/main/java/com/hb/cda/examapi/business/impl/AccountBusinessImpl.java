package com.hb.cda.examapi.business.impl;

import com.hb.cda.examapi.business.AccountBusiness;
import com.hb.cda.examapi.controller.dto.AccountDTO;
import com.hb.cda.examapi.controller.dto.AccountEntryDTO;
import com.hb.cda.examapi.controller.dto.DebtDTO;
import com.hb.cda.examapi.controller.dto.mapper.AccountMapper;
import com.hb.cda.examapi.entity.Account;
import com.hb.cda.examapi.entity.Expense;
import com.hb.cda.examapi.entity.Payment;
import com.hb.cda.examapi.entity.User;
import com.hb.cda.examapi.repository.AccountRepository;
import com.hb.cda.examapi.repository.ExpenseRepository;
import com.hb.cda.examapi.repository.PaymentRepository;
import com.hb.cda.examapi.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccountBusinessImpl implements AccountBusiness {
    private AccountRepository accountRepo;
    private ExpenseRepository expenseRepo;
    private PaymentRepository paymentRepo;
    private UserRepository userRepo;
    private AccountMapper mapper;

    public AccountBusinessImpl(AccountRepository accountRepo, ExpenseRepository expenseRepo, PaymentRepository paymentRepo, UserRepository userRepo, AccountMapper mapper) {
        this.accountRepo = accountRepo;
        this.expenseRepo = expenseRepo;
        this.paymentRepo = paymentRepo;
        this.userRepo = userRepo;
        this.mapper = mapper;
    }

    @Override
    public List<AccountEntryDTO> calculateBalances(String accountId) {
        // Charger toutes les dépenses du groupe
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        List<Expense> expenses = expenseRepo.findByAccount(account);

        // Calculer ce que chaque utilisateur a payé
        Map<User, Double> totalExpenseByUser = new HashMap<>();

        for (Expense expense : expenses) {
            User payer = expense.getPayer();
            Double amount = expense.getAmount();

            Double totalPaid = totalExpenseByUser.getOrDefault(payer, 0.0);
            totalPaid += amount;
            totalExpenseByUser.put(payer, totalPaid);
        }

        // Calculer ce qu'il aurait dû payer
        double totalPaid = expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();
        Integer participants = account.getUsers().size();
        double shareByParticipant = totalPaid / participants;

        // Calculer les paiements effectués
        List<Payment> payments = paymentRepo.findByAccount(account);

        Map<User, Double> totalRepaymentByUser = new HashMap<>();
        Map<User, Double> totalReceivedByUser = new HashMap<>();

        for (Payment payment : payments) {
            User from = payment.getFrom();
            User to = payment.getTo();
            Double amount = payment.getAmount();

            Double totalRepaid = totalRepaymentByUser.getOrDefault(from, 0.0);
            totalRepaid += amount;
            totalRepaymentByUser.put(from, totalRepaid);

            Double totalReceived = totalReceivedByUser.getOrDefault(to, 0.0);
            totalReceived += amount;
            totalReceivedByUser.put(to, totalReceived);
        }

        // Ajustement des soldes de chacun
        Map<User, Double> balanceByUser = new HashMap<>();

        for (User user : account.getUsers()) {
            Double paid = totalExpenseByUser.getOrDefault(user, 0.0);
            Double repayment = totalRepaymentByUser.getOrDefault(user, 0.0);
            Double received = totalReceivedByUser.getOrDefault(user, 0.0);
            Double balance = paid - shareByParticipant + repayment - received;
            balanceByUser.put(user, balance);
        }

        // Retourner un List<AccountEntryDTO>
        List<AccountEntryDTO> entries = balanceByUser.entrySet().stream()
                .map(entry -> {
                    User user = entry.getKey();
                    Double balance = entry.getValue();
                    return new AccountEntryDTO(user.getId(), user.getUsername(), balance);
                        })
                .collect(Collectors.toList());

        return entries;
    }

    @Override
    public List<DebtDTO> calculateDebts(String accountId) {
        // OBJECTIF : Balance à zéro (si solde +, on lui doit; si -, il doit)
        // Utiliser calculateBalances pour récupérer les net balances
        List<AccountEntryDTO> balances = calculateBalances(accountId);

        // Séparer les Créditors et les Debtors
        List<AccountEntryDTO> debtors = balances.stream()
                .filter(n -> n.getBalance() < 0)
                .sorted(Comparator.comparing(AccountEntryDTO::getBalance))
                .collect(Collectors.toList());

        List<AccountEntryDTO> creditors = balances.stream()
                .filter(n -> n.getBalance() > 0)
                .sorted(Comparator.comparing(AccountEntryDTO::getBalance).reversed())
                .collect(Collectors.toList());

        // Appliquer l’algorithme de compensation
        List<DebtDTO> debts = new ArrayList<>();

        // Bouclé la méthode de compensation jusqu'à ce que les listes soient vides
        while (!debtors.isEmpty() && !creditors.isEmpty()) {
            // Récupérer les "top" débiteur et créditeur
            AccountEntryDTO debtor = debtors.get(0);
            AccountEntryDTO creditor = creditors.get(0);

            // Le max transférable est égale au min des deux balances
            Double amount = Math.min(-debtor.getBalance(), creditor.getBalance());
            debts.add(new DebtDTO(debtor.getId(), debtor.getUsername(), creditor.getId(), creditor.getUsername(), amount));

            // Appliquer ce transfert théorique aux balances des deux users
            debtor.setBalance(debtor.getBalance() + amount);
            creditor.setBalance(creditor.getBalance() - amount);

            if (Math.abs(debtor.getBalance()) < 0.01) {
                debtors.remove(0);
            }
            if (Math.abs(creditor.getBalance()) < 0.01) {
                creditors.remove(0);
            }
        }

        // Retourner la List<DebtDTO>
        return debts;
    }

    @Override
    public List<AccountDTO> getAccountsByUser(String userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return accountRepo.findByUsersContaining(user).stream()
                .map(mapper::convertToDto)
                .toList();
    }
}
