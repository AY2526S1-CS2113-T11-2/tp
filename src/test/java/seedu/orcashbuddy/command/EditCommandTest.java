package seedu.orcashbuddy.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.orcashbuddy.exception.OrCashBuddyException;
import seedu.orcashbuddy.expense.Expense;
import seedu.orcashbuddy.storage.ExpenseManager;
import seedu.orcashbuddy.ui.Ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Extended test suite for EditCommand.
 * Covers full-field, partial-field, invalid, and boundary scenarios.
 */
class EditCommandTest {

    private ExpenseManager manager;
    private StubUi ui;

    static class StubUi extends Ui {
        Expense lastEditedExpense = null;
        Expense lastEmptyEdit = null;
        boolean separatorShown = false;
        boolean progressBarShown = false;

        @Override
        public void showEditedExpense(Expense expense) {
            this.lastEditedExpense = expense;
        }

        @Override
        public void showEmptyEdit(Expense expense) {
            this.lastEmptyEdit = expense;
        }

        @Override
        public void showSeparator() {
            separatorShown = true;
        }

        @Override
        public void showProgressBar(seedu.orcashbuddy.storage.BudgetData budgetData) {
            progressBarShown = true;
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        manager = new ExpenseManager();
        ui = new StubUi();

        new AddCommand(12.50, "Lunch", "Food").execute(manager, ui);
        new AddCommand(30.00, "Book", "Education").execute(manager, ui);
        new SetBudgetCommand(100.0).execute(manager, ui);
    }

    // === Basic update scenarios ===

    @Test
    void execute_editAllFields_updatesExpenseSuccessfully() throws Exception {
        EditCommand cmd = new EditCommand(1, 20.00, "Dinner", "Meals");
        cmd.execute(manager, ui);

        Expense edited = manager.getExpense(1);
        assertEquals(20.00, edited.getAmount(), 1e-6);
        assertEquals("Dinner", edited.getDescription());
        assertEquals("Meals", edited.getCategory());
        assertEquals(ui.lastEditedExpense, edited);
        assertTrue(ui.separatorShown);
    }

    @Test
    void execute_editPartialFields_preservesUnchangedFields() throws Exception {
        EditCommand cmd = new EditCommand(2, null, "Notebook", null);
        cmd.execute(manager, ui);

        Expense edited = manager.getExpense(2);
        assertEquals(30.00, edited.getAmount());
        assertEquals("Notebook", edited.getDescription());
        assertEquals("Education", edited.getCategory());
        assertEquals(ui.lastEditedExpense, edited);
    }

    @Test
    void execute_editDoesNotAffectOtherExpenses() throws Exception {
        EditCommand cmd = new EditCommand(1, 25.0, "Dinner", null);
        cmd.execute(manager, ui);

        Expense edited = manager.getExpense(1);
        Expense untouched = manager.getExpense(2);

        assertEquals("Dinner", edited.getDescription());
        assertEquals("Book", untouched.getDescription());
    }

    // === Mark / unmark interactions ===

    @Test
    void execute_editMarkedExpense_editPreservesMarkStatus() throws Exception {
        new MarkCommand(1).execute(manager, ui);

        EditCommand cmd = new EditCommand(1, 15.0, "Lunch with friends", null);
        cmd.execute(manager, ui);

        Expense edited = manager.getExpense(1);
        assertTrue(edited.isMarked());
    }

    @Test
    void execute_editUnmarkedExpense_preservesMarkStatus() throws Exception {
        EditCommand cmd = new EditCommand(1, 20.0, "Updated Lunch", "Dining");
        cmd.execute(manager, ui);

        Expense edited = manager.getExpense(1);
        assertFalse(edited.isMarked());
    }

    @Test
    void execute_editMarkedExpense_triggersProgressBarUpdate() throws Exception {
        new MarkCommand(1).execute(manager, ui);

        EditCommand cmd = new EditCommand(1, 15.0, "Updated Lunch", "Dining");
        cmd.execute(manager, ui);

        assertTrue(ui.progressBarShown, "Progress bar should be shown when editing a marked expense's amount");
    }


    // === No change / edge case scenarios ===

    @Test
    void execute_noChanges_invokesShowEmptyEdit() throws Exception {
        EditCommand cmd = new EditCommand(1, null, null, null);
        cmd.execute(manager, ui);

        assertNull(ui.lastEditedExpense);
        assertNotNull(ui.lastEmptyEdit);
    }

    @Test
    void execute_smallChangeInAmount_detectedAsChange() throws Exception {
        EditCommand cmd = new EditCommand(1, 12.501, null, null);
        cmd.execute(manager, ui);

        assertNull(ui.lastEditedExpense, "Digits after second decimal is not counted as changes");
    }

    // === Invalid input and boundary cases ===

    @Test
    void execute_invalidIndex_throwsException() {
        EditCommand cmd = new EditCommand(10, 10.0, "Fake", "Misc");
        assertThrows(OrCashBuddyException.class, () -> cmd.execute(manager, ui));
    }

    @Test
    void execute_editFirstExpenseBoundary_succeeds() throws Exception {
        EditCommand cmd = new EditCommand(1, 50.0, "Boundary case", "Misc");
        cmd.execute(manager, ui);
        Expense edited = manager.getExpense(1);

        assertEquals(50.0, edited.getAmount());
        assertEquals("Boundary case", edited.getDescription());
    }

    @Test
    void execute_editLastExpenseBoundary_succeeds() throws Exception {
        int lastIndex = manager.getSize();
        EditCommand cmd = new EditCommand(lastIndex, 99.9, "Last", "Category");
        cmd.execute(manager, ui);
        Expense edited = manager.getExpense(lastIndex);

        assertEquals(99.9, edited.getAmount());
        assertEquals("Last", edited.getDescription());
    }

    @Test
    void execute_editWithNullUi_throwsAssertionError() {
        EditCommand cmd = new EditCommand(1, 20.0, "Desc", "Cat");
        assertThrows(AssertionError.class, () -> cmd.execute(manager, null));
    }

    @Test
    void execute_editWithNullManager_throwsAssertionError() {
        EditCommand cmd = new EditCommand(1, 20.0, "Desc", "Cat");
        assertThrows(AssertionError.class, () -> cmd.execute(null, ui));
    }

    @Test
    void execute_editWithInvalidAmount_doesNotCrash() {
        assertThrows(IllegalArgumentException.class, () -> {
            new EditCommand(1, -10.0, "Invalid", "Test").execute(manager, ui);
        });
    }

    // === Combination field edits ===

    @Test
    void execute_editCategoryAndAmountOnly_updatesCorrectly() throws Exception {
        EditCommand cmd = new EditCommand(1, 99.0, null, "Travel");
        cmd.execute(manager, ui);

        Expense edited = manager.getExpense(1);
        assertEquals(99.0, edited.getAmount());
        assertEquals("Lunch", edited.getDescription());
        assertEquals("Travel", edited.getCategory());
    }

    @Test
    void execute_editDescriptionOnly_reflectsInManager() throws Exception {
        EditCommand cmd = new EditCommand(1, null, "Updated Description", null);
        cmd.execute(manager, ui);

        assertEquals("Updated Description", manager.getExpense(1).getDescription());
    }
}
