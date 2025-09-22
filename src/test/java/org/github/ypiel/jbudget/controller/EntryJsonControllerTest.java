package org.github.ypiel.jbudget.controller;

import org.github.ypiel.jbudget.model.Account;
import org.github.ypiel.jbudget.model.Entry;
import org.github.ypiel.jbudget.model.EntryCategory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EntryJsonControllerTest {

    @TempDir
    File tempDir;

    @Test
    void testSaveAndLoadEntries() throws IOException {
        // 1. Create a list of Entry objects
        Account account = new Account("1", "Test Bank", "Test Account", "123", 1000.0);
        LocalDate date = LocalDate.of(2023, 1, 1);
        List<Entry> originalEntries = Arrays.asList(
                new Entry(account, date, date, "Label1", "Desc1", BigDecimal.TEN, BigDecimal.ZERO, List.of(EntryCategory.GROCERIES_HOUSEHOLD)),
                new Entry(account, date.plusDays(1), date.plusDays(1), "Label2", "Desc2", BigDecimal.ZERO, BigDecimal.valueOf(20), List.of(EntryCategory.INCOME))
        );

        // 2. Save them to a temporary file
        File tempFile = new File(tempDir, "entries.json");
        EntryJsonController.saveEntriesToFile(originalEntries, tempFile.getAbsolutePath());

        // 3. Load them back from the file
        List<Entry> loadedEntries = EntryJsonController.loadEntriesFromFile(tempFile.getAbsolutePath());

        // 4. Assert that the loaded list is equal to the original list
        assertEquals(originalEntries.size(), loadedEntries.size());
        for (int i = 0; i < originalEntries.size(); i++) {
            assertEquals(originalEntries.get(i), loadedEntries.get(i));
        }
    }
}
