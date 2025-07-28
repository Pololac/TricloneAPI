package com.hb.cda.examapi.business;

import com.hb.cda.examapi.controller.dto.PaymentDTO;

import java.util.List;

public interface PaymentBusiness {
    PaymentDTO savePayment(String accountId, PaymentDTO dto);
    List<PaymentDTO> getPaymentsByAccount(String accountId);
}
