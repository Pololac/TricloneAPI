package com.hb.cda.examapi.controller;

import com.hb.cda.examapi.business.AccountBusiness;
import com.hb.cda.examapi.business.PaymentBusiness;
import com.hb.cda.examapi.controller.dto.AccountEntryDTO;
import com.hb.cda.examapi.controller.dto.DebtDTO;
import com.hb.cda.examapi.controller.dto.PaymentDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts/{accountId}")
public class AccountController {
    private final AccountBusiness accountBusiness;
    private final PaymentBusiness paymentBusiness;

    public AccountController(AccountBusiness accountBusiness, PaymentBusiness paymentBusiness) {
        this.accountBusiness = accountBusiness;
        this.paymentBusiness = paymentBusiness;
    }

    @GetMapping("/balances")
    public List<AccountEntryDTO> getBalances(@PathVariable String accountId) {
        return accountBusiness.calculateBalances(accountId);
    }

    @GetMapping("/debts")
    public List<DebtDTO> getDebts(@PathVariable String accountId) {
        return accountBusiness.calculateDebts(accountId);
    }

    @PostMapping("payments")
    public PaymentDTO makePayment(@PathVariable String accountId, @RequestBody PaymentDTO dto) {
        return paymentBusiness.savePayment(accountId, dto);
    }

    @GetMapping("/payments")
    public List<PaymentDTO> getAll(@PathVariable String accountId){
        return paymentBusiness.getPaymentsByAccount(accountId);
    }
}
