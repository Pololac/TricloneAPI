package com.hb.cda.examapi.business.pojo;

import com.hb.cda.examapi.entity.User;

public class Balance {
    private User user;
    private Double balance; // + = on lui doit, - = il doit qqc

    public Balance() {
    }

    public Balance(User user, Double balance) {
        this.user = user;
        this.balance = balance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
