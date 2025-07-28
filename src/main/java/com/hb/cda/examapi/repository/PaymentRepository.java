package com.hb.cda.examapi.repository;

import com.hb.cda.examapi.entity.Account;
import com.hb.cda.examapi.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    List<Payment> findByAccount(Account account);
}
