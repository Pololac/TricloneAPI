package com.hb.cda.examapi.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String description;
    private Double amount;
    private LocalDateTime date;

    // côté propriétaire
    @ManyToOne
    private User payer;

    // côté propriétaire
    @ManyToOne
    private Account account;

    public Expense() {
    }

    public Expense(String description, Double amount, LocalDateTime date, User payer, Account account) {
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.payer = payer;
        this.account = account;
    }

    public Expense(String id, String description, Double amount, LocalDateTime date, User payer, Account account) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.payer = payer;
        this.account = account;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public User getPayer() {
        return payer;
    }

    public void setPayer(User payer) {
        this.payer = payer;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }


}
