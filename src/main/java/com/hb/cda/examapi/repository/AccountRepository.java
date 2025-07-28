package com.hb.cda.examapi.repository;

import com.hb.cda.examapi.controller.dto.AccountDTO;
import com.hb.cda.examapi.entity.Account;
import com.hb.cda.examapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {
    List<Account> findByUsersContaining(User user);
    Optional<Account> findByName(String name);
}
