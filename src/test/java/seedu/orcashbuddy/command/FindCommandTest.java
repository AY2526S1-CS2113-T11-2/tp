package seedu.orcashbuddy.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.orcashbuddy.expense.Expense;
import seedu.orcashbuddy.storage.ExpenseManager;
import seedu.orcashbuddy.ui.Ui;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;


/**
 * Command-level tests for the FindCommand.
 * Verifies that expenses are correctly found by category or description
 * and correctly displayed in the UI.
 */
class FindCommandTest {

    private ExpenseManager manager;
    private StubUi ui;

    static class StubUi extends Ui {
        ArrayList<Expense> lastFoundExpenses = null;
        String lastSearchTerm = null;
        String lastSearchType = null;

        @Override
        public void showFoundExpenses(ArrayList<Expense> foundExpenses, String searchTerm, String searchType) {
            this.lastFoundExpenses = foundExpenses;
            this.lastSearchTerm = searchTerm;
            this.lastSearchType = searchType;
        }
    }

    @BeforeEach
    void setUp() {
        manager = new ExpenseManager();
        ui = new StubUi();
    }

    /**
     * Tests that FindCommand finds expenses by category (case-insensitive).
     */
    @Test
    void execute_findByCategory_findsMatchingExpenses() throws Exception {
        // Add expenses with different categories
        new AddCommand(50.00, "Lunch", "Food").execute(manager, ui);
        new AddCommand(100.00, "Court booking", "Venue").execute(manager, ui);
        new AddCommand(25.00, "Dinner", "Food").execute(manager, ui);

        // Execute find command for "Food" category
        new FindCommand("category", "Food").execute(manager, ui);

        // Verify results
        assertNotNull(ui.lastFoundExpenses);
        assertEquals(2, ui.lastFoundExpenses.size());
        assertEquals("Food", ui.lastSearchTerm);
        assertEquals("category", ui.lastSearchType);

        // Verify both Food expenses are found
        assertEquals("[ ] [Food] Lunch - $50.00",
                ui.lastFoundExpenses.get(0).formatForDisplay());
        assertEquals("[ ] [Food] Dinner - $25.00",
                ui.lastFoundExpenses.get(1).formatForDisplay());
    }

    /**
     * Tests that FindCommand finds expenses by category in a case-insensitive manner.
     */
    @Test
    void execute_findByCategoryLowercase_findsMatchingExpenses() throws Exception {
        // Add expenses with mixed case categories
        new AddCommand(50.00, "Lunch", "Food").execute(manager, ui);
        new AddCommand(100.00, "Court booking", "Venue").execute(manager, ui);

        // Execute find command with lowercase search term
        new FindCommand("category", "food").execute(manager, ui);

        // Verify results
        assertNotNull(ui.lastFoundExpenses);
        assertEquals(1, ui.lastFoundExpenses.size());
        assertEquals("[ ] [Food] Lunch - $50.00",
                ui.lastFoundExpenses.get(0).formatForDisplay());
    }

    /**
     * Tests that FindCommand finds expenses by description (case-insensitive).
     */
    @Test
    void execute_findByDescription_findsMatchingExpenses() throws Exception {
        // Add expenses with different descriptions
        new AddCommand(50.00, "Lunch at cafe", "Food").execute(manager, ui);
        new AddCommand(100.00, "Court booking", "Venue").execute(manager, ui);
        new AddCommand(25.00, "Dinner at restaurant", "Food").execute(manager, ui);

        // Execute find command for "at" in description
        new FindCommand("description", "at").execute(manager, ui);

        // Verify results
        assertNotNull(ui.lastFoundExpenses);
        assertEquals(2, ui.lastFoundExpenses.size());
        assertEquals("at", ui.lastSearchTerm);
        assertEquals("description", ui.lastSearchType);
    }

    /**
     * Tests that FindCommand finds expenses by description in a case-insensitive manner.
     */
    @Test
    void execute_findByDescriptionUppercase_findsMatchingExpenses() throws Exception {
        // Add expenses
        new AddCommand(50.00, "lunch", "Food").execute(manager, ui);
        new AddCommand(25.00, "dinner", "Food").execute(manager, ui);

        // Execute find command with uppercase search term
        new FindCommand("description", "LUNCH").execute(manager, ui);

        // Verify results
        assertNotNull(ui.lastFoundExpenses);
        assertEquals(1, ui.lastFoundExpenses.size());
        assertEquals("[ ] [Food] lunch - $50.00",
                ui.lastFoundExpenses.get(0).formatForDisplay());
    }

    /**
     * Tests that FindCommand returns empty list when no expenses match the category.
     */
    @Test
    void execute_findByCategoryNoMatch_returnsEmptyList() throws Exception {
        // Add expenses
        new AddCommand(50.00, "Lunch", "Food").execute(manager, ui);
        new AddCommand(100.00, "Court booking", "Venue").execute(manager, ui);

        // Execute find command for non-existent category
        new FindCommand("category", "Transport").execute(manager, ui);

        // Verify results
        assertNotNull(ui.lastFoundExpenses);
        assertEquals(0, ui.lastFoundExpenses.size());
        assertEquals("Transport", ui.lastSearchTerm);
        assertEquals("category", ui.lastSearchType);
    }

    /**
     * Tests that FindCommand returns empty list when no expenses match the description.
     */
    @Test
    void execute_findByDescriptionNoMatch_returnsEmptyList() throws Exception {
        // Add expenses
        new AddCommand(50.00, "Lunch", "Food").execute(manager, ui);
        new AddCommand(100.00, "Court booking", "Venue").execute(manager, ui);

        // Execute find command for non-existent description keyword
        new FindCommand("description", "breakfast").execute(manager, ui);

        // Verify results
        assertNotNull(ui.lastFoundExpenses);
        assertEquals(0, ui.lastFoundExpenses.size());
        assertEquals("breakfast", ui.lastSearchTerm);
        assertEquals("description", ui.lastSearchType);
    }

    /**
     * Tests that FindCommand finds partial matches in category names.
     */
    @Test
    void execute_findByCategoryPartialMatch_findsMatchingExpenses() throws Exception {
        // Add expenses
        new AddCommand(50.00, "Lunch", "Food-Groceries").execute(manager, ui);
        new AddCommand(100.00, "Court booking", "Venue").execute(manager, ui);

        // Execute find command with partial category name
        new FindCommand("category", "Food").execute(manager, ui);

        // Verify results
        assertNotNull(ui.lastFoundExpenses);
        assertEquals(1, ui.lastFoundExpenses.size());
        assertTrue(ui.lastFoundExpenses.get(0).getCategory().contains("Food"));
    }

    /**
     * Tests that FindCommand works correctly with marked expenses.
     */
    @Test
    void execute_findWithMarkedExpenses_findsCorrectly() throws Exception {
        // Add and mark expenses
        new AddCommand(50.00, "Lunch", "Food").execute(manager, ui);
        new AddCommand(100.00, "Dinner", "Food").execute(manager, ui);
        manager.markExpense(1); // Mark first expense

        // Execute find command
        new FindCommand("category", "Food").execute(manager, ui);

        // Verify both marked and unmarked expenses are found
        assertNotNull(ui.lastFoundExpenses);
        assertEquals(2, ui.lastFoundExpenses.size());
        assertTrue(ui.lastFoundExpenses.get(0).isMarked());
        assertFalse(ui.lastFoundExpenses.get(1).isMarked());
    }
}
