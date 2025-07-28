package com.hb.cda.examapi.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class PaymentDTO {
    private Double amount;
    @JsonFormat(pattern = "dd-MM-yyyy'T'HH:mm:ss", timezone = "Europe/Paris")
    private LocalDateTime date;

    private String fromUserId;
    private String toUserId;
    private String accountId;

    public PaymentDTO() {}

    public PaymentDTO(String fromUserId, String toUserId, Double amount, LocalDateTime date, String accountId) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.amount = amount;
        this.date = date;
        this.accountId = accountId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
