package com.mybank.domain;

public class CheckingAccount extends Account {

    private double overdraft;

    public CheckingAccount(double balance) {
        super(balance);
    }

    public CheckingAccount(double balance, double overdraft) {
        super(balance);
        this.overdraft = overdraft;
    }

    @Override
    public String getType() {
        return "Checking";
    }
}