package org.github.ypiel.jbudget.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EntryTest {

    private Account account;
    private LocalDate date;

    @BeforeEach
    void setUp() {
        account = new Account("1", "Test Bank", "Test Account", "123", 1000.0);
        date = LocalDate.now();
    }

    @Test
    void testMainConstructorWithValidArguments() {
        assertDoesNotThrow(() -> new Entry(account, date, date, "Label", "Desc", BigDecimal.TEN, BigDecimal.ZERO, Collections.emptyList()));
    }

    @Test
    void testCompactConstructorWithNullCategory() {
        Entry entry = new Entry(account, date, date, "Label", "Desc", BigDecimal.TEN, BigDecimal.ZERO, null, false, false);
        assertNotNull(entry.category());
        assertTrue(entry.category().isEmpty());
    }

    @Test
    void testConstructorWithNullAccount() {
        assertThrows(IllegalArgumentException.class, () -> new Entry(null, date, date, "Label", "Desc", BigDecimal.TEN, BigDecimal.ZERO, null));
    }

    @Test
    void testWithAccount() {
        Entry entry = new Entry(account, date, date, "Label", "Desc", BigDecimal.TEN, BigDecimal.ZERO, null);
        Account newAccount = new Account("2", "New Bank", "New Account", "456", 2000.0);
        Entry newEntry = entry.withAccount(newAccount);
        assertEquals(newAccount, newEntry.account());
        assertEquals(entry.label(), newEntry.label());
    }

    @Test
    void testWithDescription() {
        Entry entry = new Entry(account, date, date, "Label", "Desc", BigDecimal.TEN, BigDecimal.ZERO, null);
        String newDescription = "New Description";
        Entry newEntry = entry.withDescription(newDescription);
        assertEquals(newDescription, newEntry.description());
        assertEquals(entry.label(), newEntry.label());
    }

    @Test
    void testAddCategory() {
        Entry entry = new Entry(account, date, date, "Label", "Desc", BigDecimal.TEN, BigDecimal.ZERO, null);
        EntryCategory category = EntryCategory.GROCERIES_HOUSEHOLD;
        Entry newEntry = entry.addCategory(category);
        assertTrue(newEntry.category().contains(category));
        assertEquals(1, newEntry.category().size());
    }

    @Test
    void testRemoveCategory() {
        EntryCategory category1 = EntryCategory.GROCERIES_HOUSEHOLD;
        EntryCategory category2 = EntryCategory.TRANSPORT;
        List<EntryCategory> categories = new ArrayList<>();
        categories.add(category1);
        categories.add(category2);
        Entry entry = new Entry(account, date, date, "Label", "Desc", BigDecimal.TEN, BigDecimal.ZERO, categories);

        Entry newEntry = entry.removeCategory(category1);
        assertFalse(newEntry.category().contains(category1));
        assertTrue(newEntry.category().contains(category2));
        assertEquals(1, newEntry.category().size());
    }

    @Test
    void testRemoveCategoryALL() {
        EntryCategory category1 = EntryCategory.GROCERIES_HOUSEHOLD;
        EntryCategory category2 = EntryCategory.TRANSPORT;
        List<EntryCategory> categories = new ArrayList<>();
        categories.add(category1);
        categories.add(category2);
        Entry entry = new Entry(account, date, date, "Label", "Desc", BigDecimal.TEN, BigDecimal.ZERO, categories);

        Entry newEntry = entry.removeCategory(EntryCategory.ALL);
        assertTrue(newEntry.category().contains(category1));
        assertTrue(newEntry.category().contains(category2));
        assertEquals(2, newEntry.category().size());
    }

    @Test
    void testIsDuplicateAndIsNotDuplicate() {
        Entry entry = new Entry(account, date, date, "Label", "Desc", BigDecimal.TEN, BigDecimal.ZERO, null);
        assertFalse(entry.duplicate());
        Entry duplicateEntry = entry.isDuplicate();
        assertTrue(duplicateEntry.duplicate());
        Entry notDuplicateEntry = duplicateEntry.isNotDuplicate();
        assertFalse(notDuplicateEntry.duplicate());
    }

    @Test
    void testIsNotNew() {
        Entry entry = new Entry(account, date, date, "Label", "Desc", BigDecimal.TEN, BigDecimal.ZERO, null, true, false);
        assertTrue(entry.newEntry());
        Entry notNewEntry = entry.isNotNew();
        assertFalse(notNewEntry.newEntry());
    }

    @Test
    void testValue() {
        Entry debitEntry = new Entry(account, date, date, "Debit", "Desc", BigDecimal.TEN, BigDecimal.ZERO, null);
        assertEquals(BigDecimal.TEN.negate(), debitEntry.value());

        Entry creditEntry = new Entry(account, date, date, "Credit", "Desc", BigDecimal.ZERO, BigDecimal.TEN, null);
        assertEquals(BigDecimal.TEN, creditEntry.value());
    }

    @Test
    void testCompareTo() {
        Entry entry1 = new Entry(account, date, date, "A", "Desc", BigDecimal.ONE, BigDecimal.ZERO, null);
        Entry entry2 = new Entry(account, date.plusDays(1), date.plusDays(1), "B", "Desc", BigDecimal.ONE, BigDecimal.ZERO, null);
        Entry entry3 = new Entry(account, date, date.plusDays(1), "C", "Desc", BigDecimal.ONE, BigDecimal.ZERO, null);
        Entry entry4 = new Entry(account, date, date, "D", "Desc", BigDecimal.TEN, BigDecimal.ZERO, null);
        Entry entry5 = new Entry(account, date, date, "D", "Desc", BigDecimal.ONE, BigDecimal.ONE, null);

        assertTrue(entry1.compareTo(entry2) < 0); // dateOperation
        assertTrue(entry1.compareTo(entry3) < 0); // dateValue
        assertTrue(entry1.compareTo(entry4) < 0); // label
        assertTrue(entry4.compareTo(entry5) > 0); // debit
        assertTrue(entry1.compareTo(entry5) < 0); // credit
    }

    @Test
    void testContains() {
        List<Entry> entries = new ArrayList<>();
        Entry entry1 = new Entry(account, date, date, "A", "Desc", BigDecimal.ONE, BigDecimal.ZERO, null);
        Entry entry2 = new Entry(account, date.plusDays(1), date.plusDays(1), "B", "Desc", BigDecimal.ONE, BigDecimal.ZERO, null);
        entries.add(entry1);
        entries.add(entry2);

        Entry searchEntry = new Entry(account, date, date, "A", "Desc", BigDecimal.ONE, BigDecimal.ZERO, null);
        assertTrue(Entry.contains(entries, searchEntry));

        Entry notFoundEntry = new Entry(account, date, date, "C", "Desc", BigDecimal.ONE, BigDecimal.ZERO, null);
        assertFalse(Entry.contains(entries, notFoundEntry));
    }
}
