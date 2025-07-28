package com.hb.cda.examapi;

import com.hb.cda.examapi.data.DataLoader;
import com.hb.cda.examapi.entity.Account;
import com.hb.cda.examapi.entity.User;
import com.hb.cda.examapi.repository.AccountRepository;
import com.hb.cda.examapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.closeTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@Transactional
class ApiAccountTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private DataLoader dataLoader;

    Account account;
    String accountId;
    String userId;
    int userCount;

    @BeforeEach
    void setUp() throws Exception {
        // Récupère les IDs dynamiquement depuis la BDD peuplée par DataLoader
        Account account = accountRepo.findByName("Ski Fev 2025")
                .orElseThrow();
        accountId = account.getId();

        User user = userRepo.findByUsernameIgnoreCase("pierre")
                .orElseThrow();
        userId = user.getId();

        userCount = accountRepo.findById(accountId)
                .get()
                .getUsers()
                .size();
    }

    @Test
    void countUser(){
        assertEquals(3, userRepo.count());
    }

    @Test
    void getBalancesWithAccountIdShouldReturnAllBalances() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/accounts/{accountId}/balances", accountId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(userCount)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].balance").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("sum($[*].balance)", closeTo(0.0, 0.01))
                );
    }



}
