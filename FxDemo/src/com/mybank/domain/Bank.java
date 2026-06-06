package com.mybank.domain;

import java.io.*;
import java.util.ArrayList;

public class Bank {

    private static ArrayList<Customer> customers = new ArrayList<>();

    public static void addCustomer(String first, String last) {
        customers.add(new Customer(first, last));
    }

    public static Customer getCustomer(int index) {
        return customers.get(index);
    }

    public static int getNumberOfCustomers() {
        return customers.size();
    }

 public static void load() {
    customers.clear();

    try {
        InputStream is = Bank.class.getResourceAsStream("/data/test.dat");

        if (is == null) {
            System.out.println("ERROR: /data/test.dat NOT FOUND IN RESOURCES");
            return;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line;
        Customer current = null;

        while ((line = br.readLine()) != null) {

            line = line.trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");

            // Новый клиент: Name Surname count(игнорим count)
            if (parts.length == 3 && !parts[0].equals("S") && !parts[0].equals("C")) {

                addCustomer(parts[0], parts[1]);
                current = getCustomer(getNumberOfCustomers() - 1);
            }

            // Savings account
            else if (parts[0].equals("S")) {

                current.addAccount(new SavingsAccount(
                        Double.parseDouble(parts[1]),
                        Double.parseDouble(parts[2])
                ));
            }

            // Checking account
            else if (parts[0].equals("C")) {

                double overdraft = parts.length > 2
                        ? Double.parseDouble(parts[2])
                        : 0.0;

                current.addAccount(new CheckingAccount(
                        Double.parseDouble(parts[1]),
                        overdraft
                ));
            }
        }

        br.close();

        System.out.println("LOAD OK. Customers = " + customers.size());

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}