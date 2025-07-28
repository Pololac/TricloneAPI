package com.hb.cda.examapi.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Double amount;
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "from_user_id", nullable = false)
    private User from;

    @ManyToOne
    @JoinColumn(name = "to_user_id", nullable = false)
    private User to;

    @ManyToOne
    private Account account;

    public Payment() {}

    public Payment(Double amount, LocalDateTime date, User from, User to, Account account) {
        this.amount = amount;
        this.date = date;
        this.from = from;
        this.to = to;
        this.account = account;
    }

    public Payment(String id, Double amount, LocalDateTime date, User from, User to, Account account) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.from = from;
        this.to = to;
        this.account = account;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
