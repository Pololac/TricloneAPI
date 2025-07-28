package com.hb.cda.examapi;

import com.hb.cda.examapi.data.DataLoader;
import com.hb.cda.examapi.entity.Account;
import com.hb.cda.examapi.entity.User;
import com.hb.cda.examapi.repository.AccountRepository;
import com.hb.cda.examapi.repository.ExpenseRepository;
import com.hb.cda.examapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@Transactional
public class ApiExpenseTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private ExpenseRepository expenseRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private DataLoader dataLoader;

    private Account account;
    String accountId;
    String userId;

    @BeforeEach
    void setUp() throws Exception {
        // Récupère les IDs dynamiquement depuis la BDD peuplée par DataLoader
        Account account = accountRepo.findByName("Ski Fev 2025")
                .orElseThrow();
        accountId = account.getId();

        User user = userRepo.findByUsernameIgnoreCase("pierre")
                .orElseThrow();
        userId = user.getId();
    }

    @Test
    void countUser(){
        assertEquals(3, userRepo.count());
    }

    @Test
    void getExpensesWithAccountIdShouldReturnAllExpenses() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/accounts/{accountId}/expenses", accountId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.expenses", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(150.0));
    }

    @Test
    void getExpensesWithFakeAccountIdShouldThrow404() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/accounts/fakeid/expenses"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getExpensesWithUserAndAmountsShouldReturnFilteredExpenses() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/accounts/{accountId}/expenses", accountId)
                .param("payerId",    userId)
                .param("minAmount",  "29.0")
                .param("maxAmount",  "31.0"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.expenses", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.expenses[0].amount").value(30.0));
    }

    @Test
    void postShouldPersistExpense() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/accounts/{accountId}/expenses", accountId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "description" : "Repas chez Michel",
                            "amount" : 60.00,
                            "payerId": "%s",
                            "date" : "15-02-2025T20:45:45"
                        }
                        """.formatted(userId)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Repas chez Michel"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(60.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date").value("15-02-2025T20:45:45"));
    }

    @Test
    void postShouldFailOnValidationError() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/accounts/{accountId}/expenses", accountId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "description" : "Repas chez Michel",
                            "amount" : -100.00,
                            "payerId": "%s",
                            "date" : "15-02-2025T20:45:45"
                        }
                        """.formatted(userId)))
                .andExpect(status().isBadRequest()
                );
    }
}
