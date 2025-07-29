package com.hb.cda.examapi.repository;

import com.hb.cda.examapi.entity.Account;
import com.hb.cda.examapi.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    List<Payment> findByAccount(Account account);
}
