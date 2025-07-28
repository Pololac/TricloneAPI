package com.hb.cda.examapi.controller.dto;

public class DebtDTO {
    private String fromUserId;
    private String fromUsername;
    private String toUserId;
    private String toUsername;
    private Double amount;

    public DebtDTO() {}

    public DebtDTO(String fromUserId, String fromUsername, String toUserId, String toUsername, Double amount) {
        this.fromUserId = fromUserId;
        this.fromUsername = fromUsername;
        this.toUserId = toUserId;
        this.toUsername = toUsername;
        this.amount = amount;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
