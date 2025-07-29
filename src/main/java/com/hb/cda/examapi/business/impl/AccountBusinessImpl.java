package com.hb.cda.examapi.business.impl;

import com.hb.cda.examapi.business.AccountBusiness;
import com.hb.cda.examapi.business.pojo.Balance;
import com.hb.cda.examapi.business.pojo.Debt;
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

    public AccountBusinessImpl(AccountRepository accountRepo, ExpenseRepository expenseRepo, PaymentRepository paymentRepo, UserRepository userRepo) {
        this.accountRepo = accountRepo;
        this.expenseRepo = expenseRepo;
        this.paymentRepo = paymentRepo;
        this.userRepo = userRepo;
    }

    @Override
    public List<Balance> calculateBalances(String accountId) {
        // Charger toutes les dépenses du groupe
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        //Récupérer toutes les dépenses
        List<Expense> expenses = expenseRepo.findByAccount(account);

        // Calculer ce que chaque utilisateur a payé
        Map<User, Double> totalExpenseByUser = new HashMap<>();

        for (Expense expense : expenses) {
            User payer = expense.getPayer();
            Double amount = expense.getAmount();

            Double totalPaidByUser = totalExpenseByUser.getOrDefault(payer, 0.0);
            totalPaidByUser += amount;
            totalExpenseByUser.put(payer, totalPaidByUser);
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
        Map<User, Double> totalGetByUser = new HashMap<>();

        for (Payment payment : payments) {
            User from = payment.getFrom();
            User to = payment.getTo();
            Double amount = payment.getAmount();

            Double totalRepaidByUser = totalRepaymentByUser.getOrDefault(from, 0.0);
            totalRepaidByUser += amount;
            totalRepaymentByUser.put(from, totalRepaidByUser);

            Double totalReceivedByUser = totalGetByUser.getOrDefault(to, 0.0);
            totalReceivedByUser += amount;
            totalGetByUser.put(to, totalReceivedByUser);
        }

        // Ajustement des soldes de chacun
        Map<User, Double> balanceByUser = new HashMap<>();

        for (User user : account.getUsers()) {
            Double paid = totalExpenseByUser.getOrDefault(user, 0.0);
            Double repayment = totalRepaymentByUser.getOrDefault(user, 0.0);
            Double received = totalGetByUser.getOrDefault(user, 0.0);
            Double balance = paid - shareByParticipant + repayment - received;
            balanceByUser.put(user, balance);
        }

        // Retourner une liste
        List<Balance> entries = balanceByUser.entrySet().stream()
                .map(entry -> {
                    return new Balance(entry.getKey(), entry.getValue());
                })
                .collect(Collectors.toList());

        return entries;
    }

    @Override
    public List<Debt> calculateDebts(String accountId) {
        // OBJECTIF : Balance à zéro (si solde +, on lui doit; si -, il doit)
        // Récupère les soldes nets par utilisateur
        List<Balance> balanceByUser = calculateBalances(accountId);

        // Séparer les Créditors et les Debtors dans des listes de type "Map" mutables
        List<Balance> debtors = balanceByUser.stream()
                .filter(n -> n.getBalance() < 0)
                .sorted(Comparator.comparing(Balance::getBalance))
                .collect(Collectors.toList());

        List<Balance> creditors = balanceByUser.stream()
                .filter(n -> n.getBalance() > 0)
                .sorted(Comparator.comparing(Balance::getBalance).reversed())
                .collect(Collectors.toList());

        // Appliquer l’algorithme de compensation
        List<Debt> debts = new ArrayList<>();

        // Boucler la méthode de compensation jusqu'à ce que les listes soient vides
        while (!debtors.isEmpty() && !creditors.isEmpty()) {
            // Récupérer les "top" débiteur et créditeur
            Balance debtor = debtors.get(0);
            Balance creditor = creditors.get(0);

            // Le max transférable est égale au min des deux balances
            Double amount = Math.min(-debtor.getBalance(), creditor.getBalance());
            debts.add(new Debt(debtor.getUser(), creditor.getUser(), amount));

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

        // Retourner la liste
        return debts;
    }

    @Override
    public List<Account> getAccountsByUser(User user) {
        return accountRepo.findByUsersContaining(user);
    }
}
