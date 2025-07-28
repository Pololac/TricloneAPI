package com.hb.cda.examapi.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;

    // côté propriétaire
    @ManyToMany
    @JoinTable(
        name = "account_user",
        joinColumns = @JoinColumn(name = "account_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    Set<User> users = new HashSet<>();

    //côté inverse
    @OneToMany(mappedBy = "account", orphanRemoval = true)
    Set<Expense> expenses = new HashSet<>();

    @OneToMany(mappedBy = "account", orphanRemoval = true)
    @OrderBy("date DESC")
    Set<Payment> payments = new HashSet<>();

    public Account() {
    }

    public Account(String name) {
        this.name = name;
    }

    public Account(String id, String name, Set<User> users, Set<Expense> expenses, Set<Payment> payments) {
        this.id = id;
        this.name = name;
        this.users = users;
        this.expenses = expenses;
        this.payments = payments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    // helper pour ajouter un utilisateur
    public void addUser(User user) {
        users.add(user);
        user.getAccounts().add(this);
    }

    // helper pour retirer un utilisateur
    public void removeUser(User user) {
        users.remove(user);
        user.getAccounts().remove(this);
    }


    public Set<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(Set<Expense> expenses) {
        this.expenses = expenses;
    }

    public Set<Payment> getPayments() {
        return payments;
    }

    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }
}
