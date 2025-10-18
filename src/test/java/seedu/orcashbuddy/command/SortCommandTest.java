//@@author saheer17
package seedu.orcashbuddy.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.orcashbuddy.expense.Expense;
import seedu.orcashbuddy.storage.ExpenseManager;
import seedu.orcashbuddy.ui.Ui;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Command-level tests for the SortCommand.
 * Verifies that expenses are sorted in descending order by amount
 * and correctly displayed in the UI.
 */
class SortCommandTest {

    private ExpenseManager manager;
    private StubUi ui;

    static class StubUi extends Ui {
        ArrayList<Expense> lastSortedExpenses = null;
        Boolean isListEmpty = false;

        @Override
        public void showSortedList(ArrayList<Expense> expenses) {
            this.lastSortedExpenses = expenses;
        }

        @Override
        public void showListUsage() {
            this.isListEmpty = true;
        }
    }

    @BeforeEach
    void setUp() {
        manager = new ExpenseManager();
        ui = new StubUi();
    }

    /**
     * Tests that SortCommand sorts multiple expenses in descending order of amount.
     */
    @Test
    void execute_withMultipleExpenses_sortsDescendingByAmount() throws Exception {
        // Add expenses in unsorted order
        new AddCommand(12.50, "Lunch").execute(manager, ui);
        new AddCommand(7.25, "Coffee").execute(manager, ui);
        new AddCommand(25.00, "Groceries").execute(manager, ui);

        // Execute the sort command
        new SortCommand().execute(manager, ui);

        // Verify sorted order (highest to lowest)
        assertEquals(3, ui.lastSortedExpenses.size());
        assertEquals("[ ] [" + Expense.DEFAULT_CATEGORY + "] Groceries - $25.00",
                ui.lastSortedExpenses.get(0).formatForDisplay());
        assertEquals("[ ] [" + Expense.DEFAULT_CATEGORY + "] Lunch - $12.50",
                ui.lastSortedExpenses.get(1).formatForDisplay());
        assertEquals("[ ] [" + Expense.DEFAULT_CATEGORY + "] Coffee - $7.25",
                ui.lastSortedExpenses.get(2).formatForDisplay());
    }

    /**
     * Tests that executing the SortCommand when there are no expenses
     * throws an OrCashBuddyException indicating the expense list is empty.
     */
    @Test
    void execute_withNoExpenses_displaysNoExpenseAddedMessage() throws Exception{
        // Execute SortCommand when no expenses exist
        new SortCommand().execute(manager, ui);

        // Verify printed message
        assertTrue(ui.isListEmpty);
    }
}
