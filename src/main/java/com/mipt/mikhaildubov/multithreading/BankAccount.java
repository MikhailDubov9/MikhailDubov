package com.mipt.mikhaildubov.multithreading;

public class BankAccount {

  private final long id;
  private double balance;

  public BankAccount(long id, double balance) {
    this.id = id;
    this.balance = balance;
  }

  public long getId() {
    return id;
  }

  public double getBalance() {
    return balance;
  }

  public void setBalance(double balance) {
    this.balance = balance;
  }

  @Override
  public String toString() {
    return "BankAccount{" + "id = " + id + ", balance = " + balance + '}';
  }
}
