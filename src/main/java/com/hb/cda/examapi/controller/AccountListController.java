package com.hb.cda.examapi.controller;

import com.hb.cda.examapi.business.AccountBusiness;
import com.hb.cda.examapi.controller.dto.AccountDTO;
import com.hb.cda.examapi.controller.dto.mapper.AccountMapper;
import com.hb.cda.examapi.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
class AccountListController {
    private final AccountBusiness accountBusiness;
    private AccountMapper mapper;

    public AccountListController(AccountBusiness accountBusiness, AccountMapper mapper) {
        this.accountBusiness = accountBusiness;
        this.mapper = mapper;
    }

    @GetMapping
    public List<AccountDTO> getAccounts(@AuthenticationPrincipal User user) {
        return accountBusiness.getAccountsByUser(user).stream()
                .map(mapper::convertToDto)
                .toList();
    }
}
