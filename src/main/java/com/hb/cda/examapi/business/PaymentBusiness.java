package com.hb.cda.examapi.business;

import com.hb.cda.examapi.controller.dto.PaymentDTO;
import com.hb.cda.examapi.entity.Payment;

import java.util.List;

public interface PaymentBusiness {
    List<Payment> getPaymentsByAccount(String accountId);
    Payment savePayment(String accountId, Payment payment);
}
