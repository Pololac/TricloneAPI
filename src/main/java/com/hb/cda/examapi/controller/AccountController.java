package com.hb.cda.examapi.controller;

import com.hb.cda.examapi.business.AccountBusiness;
import com.hb.cda.examapi.business.PaymentBusiness;
import com.hb.cda.examapi.controller.dto.AccountEntryDTO;
import com.hb.cda.examapi.controller.dto.DebtDTO;
import com.hb.cda.examapi.controller.dto.PaymentDTO;
import com.hb.cda.examapi.controller.dto.mapper.AccountMapper;
import com.hb.cda.examapi.controller.dto.mapper.PaymentMapper;
import com.hb.cda.examapi.entity.Payment;
import com.hb.cda.examapi.entity.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts/{accountId}")
public class AccountController {
    private final AccountBusiness accountBusiness;
    private final PaymentBusiness paymentBusiness;
    private final PaymentMapper paymentMapper;

    public AccountController(AccountBusiness accountBusiness, PaymentBusiness paymentBusiness, PaymentMapper paymentMapper) {
        this.accountBusiness = accountBusiness;
        this.paymentBusiness = paymentBusiness;
        this.paymentMapper = paymentMapper;
    }

    @GetMapping("/balances")
    public List<AccountEntryDTO> getBalances(@PathVariable String accountId) {
        return accountBusiness.calculateBalances(accountId).stream()
                .map(e -> new AccountEntryDTO(
                        e.getUser().getId(),
                        e.getUser().getUsername(),
                        e.getBalance()
                ))
                .toList();
    }

    @GetMapping("/debts")
    public List<DebtDTO> getDebts(@PathVariable String accountId) {
        return accountBusiness.calculateDebts(accountId).stream()
                .map(e -> new DebtDTO(
                        e.getFrom().getId(),
                        e.getFrom().getUsername(),
                        e.getTo().getId(),
                        e.getTo().getUsername(),
                        e.getAmount()
                ))
                .toList();
    }

    @PostMapping("payments")
    public PaymentDTO makePayment(@PathVariable String accountId, @RequestBody PaymentDTO dto) {
        Payment payment = paymentBusiness.savePayment(accountId, paymentMapper.convertFromPost(dto));
        return paymentMapper.convertToDTO(payment);
    }

    @GetMapping("/payments")
    public List<PaymentDTO> getAll(@PathVariable String accountId){
        return paymentBusiness.getPaymentsByAccount(accountId).stream()
                .map(paymentMapper::convertToDTO)
                .collect(Collectors.toList());
    }
}
