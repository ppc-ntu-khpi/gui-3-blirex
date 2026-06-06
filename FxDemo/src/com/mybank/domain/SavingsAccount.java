package com.mybank.domain;

public class SavingsAccount extends Account {

    private double interest;

    public SavingsAccount(double balance, double interest) {
        super(balance);
        this.interest = interest;
    }

    @Override
    public String getType() {
        return "Savings";
    }
}