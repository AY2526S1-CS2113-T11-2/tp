//@@author gumingyoujia
package seedu.orcashbuddy.command;

import seedu.orcashbuddy.exception.OrCashBuddyException;
import seedu.orcashbuddy.expense.Expense;
import seedu.orcashbuddy.storage.ExpenseManager;
import seedu.orcashbuddy.ui.Ui;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Command to edit expense in the expense manager.
 */
public class EditCommand extends Command {
    private static final Logger LOGGER = Logger.getLogger(EditCommand.class.getName());
    private final int index;
    private final Double newAmount; // can be null if unchanged
    private final String newDescription;
    private final String newCategory;

    /**
     * Constructs an {@code EditCommand}.
     *
     * @param index index of the expense to edit (1-based)
     * @param newAmount new amount (nullable to keep existing)
     * @param newDescription new description (nullable to keep existing)
     * @param newCategory new category (nullable to keep existing)
     */
    public EditCommand(int index, Double newAmount, String newDescription,
            String newCategory) {
        this.index = index;
        this.newAmount = newAmount;
        this.newDescription = newDescription;
        this.newCategory = newCategory;
    }

    /**
     * Applies the modifications (if any) to the specified expense.
     * Replaces the stored expense with an updated {@link Expense} object,
     * preserves mark/unmark status, and shows the result through {@link Ui}.
     *
     * @param expenseManager the central data model that stores all expenses and budget state
     * @param ui the UI used to show output to the user
     * @throws OrCashBuddyException if the index is invalid or the expense cannot be found
     */
    @Override
    public void execute(ExpenseManager expenseManager, Ui ui) throws OrCashBuddyException {
        assert expenseManager != null : "ExpenseManager cannot be null";
        assert ui != null : "Ui cannot be null";
        assert index >= 1 : "Expense index must be >= 1";

        LOGGER.log(Level.INFO, "Attempting to edit expense at index {0}", index);

        // Get original expense particulars
        Expense original = expenseManager.getExpense(index);
        if (original == null) {
            LOGGER.log(Level.WARNING, "No expense found at index {0}", index);
            throw new OrCashBuddyException("No expense found at index " + index);
        }
        double originalAmount = original.getAmount();
        String originalDescription = original.getDescription();
        String originalCategory = original.getCategory();

        // Update expense particulars
        double updatedAmount = (newAmount != null) ? newAmount : originalAmount;
        String updatedDescription = (newDescription != null) ? newDescription : originalDescription;
        String updatedCategory = (newCategory != null) ? newCategory : originalCategory;
        boolean wasMarked = original.isMarked();
        LOGGER.log(Level.FINE, "Original expense: {0}", original.formatForDisplay());
        LOGGER.log(Level.FINE, "Updated fields: amount={0}, desc={1}, category={2}",
                new Object[]{updatedAmount, updatedDescription, updatedCategory});

        // Create a new expense with updated particulars and replace the old one
        Expense edited = new Expense(updatedAmount, updatedDescription, updatedCategory);
        expenseManager.replaceExpense(index, edited);

        // Preserve the mark status
        if  (wasMarked) {
            expenseManager.markExpense(index);
        }

        // UI output
        ui.showSeparator();
        if (Math.abs(updatedAmount - originalAmount) < 0.001 && updatedDescription.equals(originalDescription)
                && updatedCategory.equals(originalCategory)) {
            ui.showEmptyEdit(edited);
            LOGGER.log(Level.INFO, "No changes were made to the expense.");
        } else {
            ui.showEditedExpense(edited);
        }
        if (edited.isMarked() && Math.abs(updatedAmount - originalAmount) >= 0.001) {
            ui.showSeparator();
            ui.showProgressBar(expenseManager.getBudgetData());
        }
        LOGGER.log(Level.INFO, "Expense at index {0} successfully edited.", index);
        ui.showSeparator();
    }
}
