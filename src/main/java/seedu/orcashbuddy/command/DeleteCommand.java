//@@author saheer17
package seedu.orcashbuddy.command;

import seedu.orcashbuddy.exception.OrCashBuddyException;
import seedu.orcashbuddy.expense.Expense;
import seedu.orcashbuddy.storage.ExpenseManager;
import seedu.orcashbuddy.ui.Ui;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Command to delete an expense from the expense manager.
 */
public class DeleteCommand extends Command {
    private static final Logger LOGGER = Logger.getLogger(DeleteCommand.class.getName());
    private final int index;

    /**
     * Constructs a {@code DeleteCommand} with the specified expense index.
     *
     * @param index the 1-based index of the expense to delete
     */
    public DeleteCommand(int index) {
        this.index = index;
    }

    /**
     * Deletes the chosen expense and displays confirmation,
     * along with an updated budget status if relevant.
     *
     * @param expenseManager the central data model that stores all expenses and budget state
     * @param ui the UI used to show output to the user
     * @throws OrCashBuddyException if the provided index is invalid
     */
    @Override
    public void execute(ExpenseManager expenseManager, Ui ui) throws OrCashBuddyException {
        assert index >= 1 : "Index must be at least 1";

        Expense expenseToDelete = expenseManager.getExpense(index);
        boolean wasMarked = expenseToDelete.isMarked();

        Expense removedExpense = expenseManager.deleteExpense(index);

        LOGGER.log(Level.INFO, "Deleted expense at index {0}: {1}",
                new Object[]{index, removedExpense.getDescription()});

        ui.showSeparator();
        ui.showDeletedExpense(removedExpense);
        if (wasMarked) {
            ui.showSeparator();
            ui.showProgressBar(expenseManager.getBudgetData());
        }
        ui.showSeparator();
    }
}
