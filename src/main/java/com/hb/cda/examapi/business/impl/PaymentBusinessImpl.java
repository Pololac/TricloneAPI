package com.hb.cda.examapi.business.impl;

import com.hb.cda.examapi.business.PaymentBusiness;
import com.hb.cda.examapi.controller.dto.PaymentDTO;
import com.hb.cda.examapi.controller.dto.mapper.PaymentMapper;
import com.hb.cda.examapi.entity.Account;
import com.hb.cda.examapi.entity.Payment;
import com.hb.cda.examapi.entity.User;
import com.hb.cda.examapi.repository.AccountRepository;
import com.hb.cda.examapi.repository.PaymentRepository;
import com.hb.cda.examapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentBusinessImpl implements PaymentBusiness {
    private static final Logger logger = LoggerFactory.getLogger(PaymentBusinessImpl.class);

    private AccountRepository accountRepo;
    private UserRepository userRepo;
    private PaymentRepository paymentRepo;
    private PaymentMapper mapper;

    public PaymentBusinessImpl(AccountRepository accountRepo, UserRepository userRepo, PaymentRepository paymentRepo, PaymentMapper mapper) {
        this.accountRepo = accountRepo;
        this.userRepo = userRepo;
        this.paymentRepo = paymentRepo;
        this.mapper = mapper;
    }

    @Override
    public Payment savePayment(String accountId, Payment payment) {
        // Récupération du groupe et des users concernés à partir du DTO
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        User from = userRepo.findById(payment.getFrom().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payer not found"));
        User to = userRepo.findById(payment.getTo().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiver not found"));

        // Ajout des propriétés liées aux relations
        payment.setAccount(account);
        payment.setFrom(from);
        payment.setTo(to);

        // Gestion cas où la date non envoyée par le front
        if (payment.getDate() == null) {
            payment.setDate(LocalDateTime.now(ZoneId.of("Europe/Paris")));
        }

        paymentRepo.save(payment);
        return payment;
    }

    @Override
    public List<Payment> getPaymentsByAccount(String accountId) {
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        List<Payment> payments = paymentRepo.findByAccount(account);

        if (payments.isEmpty()) {
            logger.info("No payments found for account {}", accountId);
        }

        return payments;
    }
}
