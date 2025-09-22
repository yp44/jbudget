package org.github.ypiel.jbudget.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void testConstructorWithValidArguments() {
        assertDoesNotThrow(() -> new Account("1", "Bank", "Name", "Code", 100.0));
    }

    @Test
    void testConstructorWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> new Account(null, "Bank", "Name", "Code", 100.0));
    }

    @Test
    void testConstructorWithNullBank() {
        assertThrows(IllegalArgumentException.class, () -> new Account("1", null, "Name", "Code", 100.0));
    }

    @Test
    void testConstructorWithNullName() {
        assertThrows(IllegalArgumentException.class, () -> new Account("1", "Bank", null, "Code", 100.0));
    }

    @Test
    void testConstructorWithBlankName() {
        assertThrows(IllegalArgumentException.class, () -> new Account("1", "Bank", " ", "Code", 100.0));
    }

    @Test
    void testConstructorWithNullCode() {
        assertThrows(IllegalArgumentException.class, () -> new Account("1", "Bank", "Name", null, 100.0));
    }

    @Test
    void testConstructorWithNegativeInitialBalance() {
        assertThrows(IllegalArgumentException.class, () -> new Account("1", "Bank", "Name", "Code", -100.0));
    }

    @Test
    void testToLabelWithBank() {
        Account account = new Account("1", "MyBank", "MyAccount", "123", 0);
        assertEquals("MyAccount (MyBank)", account.toLabel());
    }

    @Test
    void testToLabelWithBlankBank() {
        Account account = new Account("1", " ", "MyAccount", "123", 0);
        assertEquals("MyAccount", account.toLabel());
    }

    @Test
    void testCompareTo() {
        Account account1 = new Account("1", "BankA", "AccountA", "1", 0);
        Account account2 = new Account("2", "BankB", "AccountB", "2", 0);
        Account account3 = new Account("3", "BankA", "AccountC", "3", 0);
        Account account4 = new Account("4", "BankA", "AccountA", "4", 0);

        assertTrue(account1.compareTo(account2) < 0); // BankA vs BankB
        assertTrue(account2.compareTo(account1) > 0); // BankB vs BankA
        assertTrue(account1.compareTo(account3) < 0); // AccountA vs AccountC
        assertTrue(account3.compareTo(account1) > 0); // AccountC vs AccountA
        assertTrue(account1.compareTo(account4) < 0); // Code 1 vs 4
        assertTrue(account4.compareTo(account1) > 0); // Code 4 vs 1
        assertEquals(0, account1.compareTo(new Account("5", "BankA", "AccountA", "1", 0)));
    }
}
