package com.hb.cda.examapi.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "user_table")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String username;
    private String password;

    // côté inverse
    @ManyToMany(mappedBy = "users")
    Set<Account> accounts = new HashSet<>();

    // côté inverse
    @OneToMany(mappedBy = "payer")
    @OrderBy("date DESC")
    Set<Expense> expenses = new HashSet<>();

    @OneToMany(mappedBy = "from")
    @OrderBy("date DESC")
    Set<Payment> paymentsFrom = new HashSet<>();

    @OneToMany(mappedBy = "to")
    @OrderBy("date DESC")
    Set<Payment> paymentsTo = new HashSet<>();

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String id, String username, String password, Set<Account> accounts, Set<Expense> expenses, Set<Payment> paymentsFrom, Set<Payment> paymentsTo) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.accounts = accounts;
        this.expenses = expenses;
        this.paymentsFrom = paymentsFrom;
        this.paymentsTo = paymentsTo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public Set<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(Set<Expense> expenses) {
        this.expenses = expenses;
    }

    public Set<Payment> getPaymentsFrom() {
        return paymentsFrom;
    }

    public void setPaymentsFrom(Set<Payment> paymentsFrom) {
        this.paymentsFrom = paymentsFrom;
    }

    public Set<Payment> getPaymentsTo() {
        return paymentsTo;
    }

    public void setPaymentsTo(Set<Payment> paymentsTo) {
        this.paymentsTo = paymentsTo;
    }
}
