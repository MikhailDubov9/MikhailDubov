package com.mipt.mikhaildubov.multithreading;

public class Bank {

  public boolean sendToAccount(BankAccount from, BankAccount to, int amount) {
    if (from == null || to == null) {
      throw new IllegalArgumentException("Accounts cannot be null");
    }

    if (amount <= 0) {
      throw new IllegalArgumentException("Amount must be positive");
    }

    if (from == to) {
      throw new IllegalArgumentException("Cannot transfer to the same account");
    }

    BankAccount first;
    BankAccount second;

    if (from.getId() < to.getId()) {
      first = from;
      second = to;
    } else {
      first = to;
      second = from;
    }

    synchronized (first) {
      synchronized (second) {
        if (from.getBalance() < amount) {
          System.out.println("Insufficient funds");
          return false;
        } else {
          from.setBalance(from.getBalance() - amount);
          to.setBalance(to.getBalance() + amount);
          return true;
        }
      }
    }
  }

  public boolean sendToAccountDeadlock(BankAccount from, BankAccount to, int amount) {
    if (from == null || to == null) {
      throw new IllegalArgumentException("Accounts cannot be null");
    }

    if (amount <= 0) {
      throw new IllegalArgumentException("Amount must be positive");
    }

    if (from == to) {
      throw new IllegalArgumentException("Cannot transfer to the same account");
    }

    synchronized (from) {
      synchronized (to) {
        if (from.getBalance() < amount) {
          System.out.println("Insufficient funds");
          return false;
        } else {
          from.setBalance(from.getBalance() - amount);
          to.setBalance(to.getBalance() + amount);
          return true;
        }
      }
    }
  }
}
