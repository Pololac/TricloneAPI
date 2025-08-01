package com.hb.cda.examapi;

import com.hb.cda.examapi.data.DataLoader;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@Transactional
class ApiAccountListTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    private DataLoader dataLoader;

    @Test
    @WithUserDetails(value="paul", userDetailsServiceBeanName="userService")
    void getAccountsShouldReturnAllUserAccounts() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/accounts"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Ski Fev 2025")
                );
    }

    @Test
    void getAccountsWhenUnauthenticatedShouldReturn401() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/accounts"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized()
                );
    }

}
