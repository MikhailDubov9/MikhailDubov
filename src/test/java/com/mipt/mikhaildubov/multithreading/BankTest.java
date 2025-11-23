package com.mipt.mikhaildubov.multithreading;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BankTest {

  private final Bank bank = new Bank();
  private final BankAccount account1 = new BankAccount(1, 1000);
  private final BankAccount account2 = new BankAccount(2, 1000);

  @Test
  void testSuccessfulTransfer() {
    boolean result = bank.sendToAccount(account1, account2, 200);
    assertTrue(result);
    assertEquals(800, account1.getBalance());
    assertEquals(1200, account2.getBalance());
  }

  @Test
  void testInsufficientFunds() {
    boolean result = bank.sendToAccount(account1, account2, 1500);
    assertFalse(result);
    assertEquals(1000, account1.getBalance());
    assertEquals(1000, account2.getBalance());
  }

  @Test
  void testSuccessfulTransferDeadlock() {
    boolean result = bank.sendToAccountDeadlock(account1, account2, 200);
    assertTrue(result);
    assertEquals(800, account1.getBalance());
    assertEquals(1200, account2.getBalance());
  }

  @Test
  void testInsufficientFundsDeadlock() {
    boolean result = bank.sendToAccountDeadlock(account1, account2, 1500);
    assertFalse(result);
    assertEquals(1000, account1.getBalance());
    assertEquals(1000, account2.getBalance());
  }

  @Test
  void testNullFromAccount() {
    assertThrows(IllegalArgumentException.class, () ->
        bank.sendToAccount(null, account2, 100));

    assertThrows(IllegalArgumentException.class, () ->
        bank.sendToAccountDeadlock(null, account2, 100));
  }

  @Test
  void testNullToAccount() {
    assertThrows(IllegalArgumentException.class, () ->
        bank.sendToAccount(account1, null, 100));

    assertThrows(IllegalArgumentException.class, () ->
        bank.sendToAccountDeadlock(account1, null, 100));
  }

  @Test
  void testNegativeAmount() {
    assertThrows(IllegalArgumentException.class, () ->
        bank.sendToAccount(account1, account2, -100));

    assertThrows(IllegalArgumentException.class, () ->
        bank.sendToAccountDeadlock(account1, account2, -100));
  }

  @Test
  void testZeroAmount() {
    assertThrows(IllegalArgumentException.class, () ->
        bank.sendToAccount(account1, account2, 0));

    assertThrows(IllegalArgumentException.class, () ->
        bank.sendToAccountDeadlock(account1, account2, 0));
  }

  @Test
  void testTransferToSameAccount() {
    assertThrows(IllegalArgumentException.class, () ->
        bank.sendToAccount(account1, account1, 100));

    assertThrows(IllegalArgumentException.class, () ->
        bank.sendToAccountDeadlock(account1, account1, 100));
  }

  @Test
  @Timeout(5)
  void testSimpleConcurrentTransfer() throws InterruptedException {
    Thread thread1 = new Thread(() -> {
      for (int i = 0; i < 100; i++) {
        bank.sendToAccount(account1, account2, 1);
      }
    });

    Thread thread2 = new Thread(() -> {
      for (int i = 0; i < 100; i++) {
        bank.sendToAccount(account2, account1, 1);
      }
    });

    thread1.start();
    thread2.start();

    thread1.join(3000);
    thread2.join(3000);

    double totalBalance = account1.getBalance() + account2.getBalance();
    assertEquals(2000, totalBalance);
  }

  @Test
  @Timeout(3)
  void testDeadlockSituation() throws InterruptedException {
    Thread thread1 = new Thread(() -> {
      for (int i = 0; i < 1000; i++) {
        bank.sendToAccountDeadlock(account1, account2, 1);
      }
    });

    Thread thread2 = new Thread(() -> {
      for (int i = 0; i < 1000; i++) {
        bank.sendToAccountDeadlock(account2, account1, 1);
      }
    });

    thread1.start();
    thread2.start();

    thread1.join(2500);
    thread2.join(500);

    System.out.println("Deadlock тест завершен - deadlock не произошел в этот раз");
  }

  @Test
  void testAccountOrder() {
    BankAccount smallAccount = new BankAccount(1, 100);
    BankAccount bigAccount = new BankAccount(2, 200);

    boolean result = bank.sendToAccount(smallAccount, bigAccount, 50);
    assertTrue(result);
    assertEquals(50, smallAccount.getBalance());
    assertEquals(250, bigAccount.getBalance());
  }
}
