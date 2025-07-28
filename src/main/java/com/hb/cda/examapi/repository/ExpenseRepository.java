package com.hb.cda.examapi.repository;

import com.hb.cda.examapi.entity.Expense;
import com.hb.cda.examapi.entity.Account;
import com.hb.cda.examapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ExpenseRepository extends JpaRepository<Expense, String> {
    List<Expense> findByAccount(Account account);

    @Query("""
    SELECT e FROM Expense e
    WHERE e.account = :account
        AND (:payerId   IS NULL OR e.payer.id   = :payerId)
        AND (:minAmount IS NULL OR e.amount     >= :minAmount)
        AND (:maxAmount IS NULL OR e.amount     <= :maxAmount)
    """)
    List<Expense> findByAccountAndFilters(
            @Param("account") Account account,
            @Param("payerId")   String payerId,
            @Param("minAmount") Double minAmount,
            @Param("maxAmount") Double maxAmount
    );

    List<Expense> findByPayer(User user);
}
