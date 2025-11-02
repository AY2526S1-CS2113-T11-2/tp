//@@author muadzyamani
package seedu.orcashbuddy.command;

import seedu.orcashbuddy.exception.OrCashBuddyException;
import seedu.orcashbuddy.expense.Expense;
import seedu.orcashbuddy.storage.ExpenseManager;
import seedu.orcashbuddy.ui.Ui;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Command to unmark (set as unpaid) a previously marked expense.
 */
public class UnmarkCommand extends Command {
    private static final Logger LOGGER = Logger.getLogger(UnmarkCommand.class.getName());
    private final int index;

    /**
     * Constructs an {@code UnmarkCommand} with the specified expense index.
     *
     * @param index the 1-based index of the expense to unmark
     */
    public UnmarkCommand(int index) {
        this.index = index;
    }

    /**
     * Unmarks the specified expense, updates budget tracking,
     * and shows the unmarked expense plus any budget alerts.
     *
     * @param expenseManager the model to update
     * @param ui the UI to display results
     * @throws OrCashBuddyException if the index is invalid
     */
    @Override
    public void execute(ExpenseManager expenseManager, Ui ui) throws OrCashBuddyException {
        assert index >= 1 : "Index must be at least 1";

        Expense expense = expenseManager.unmarkExpense(index);

        LOGGER.log(Level.INFO, "Unmarked expense at index {0}: {1}",
                new Object[]{index, expense.getDescription()});

        ui.showSeparator();
        ui.showUnmarkedExpense(expense);
        ui.showSeparator();
        ui.showProgressBar(expenseManager.getBudgetData());
        ui.showSeparator();
    }
}
