package com.hb.cda.examapi.controller.dto;

public class AccountEntryDTO {
    private String id;
    private String username;
    private Double balance; // + = on lui doit, - = il doit qqc

    public AccountEntryDTO() {}
    public AccountEntryDTO(String id, String username, Double balance) {
        this.id = id;
        this.username = username;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
