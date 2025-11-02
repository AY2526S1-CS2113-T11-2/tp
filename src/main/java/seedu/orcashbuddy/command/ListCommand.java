//@@author gumingyoujia
package seedu.orcashbuddy.command;

import seedu.orcashbuddy.storage.ExpenseManager;
import seedu.orcashbuddy.ui.Ui;

import java.util.logging.Logger;

/**
 * Command to list all expenses along with summary statistics.
 */
public class ListCommand extends Command {
    private static final Logger LOGGER = Logger.getLogger(ListCommand.class.getName());

    /**
     * Displays the current financial summary:
     * <ul>
     *   <li>Budget</li>
     *   <li>Total spent</li>
     *   <li>Remaining balance</li>
     *   <li>All expenses in a numbered list</li>
     * </ul>
     *
     * @param expenseManager the central data model that stores all expenses and budget state
     * @param ui the UI used to show output to the user
     */
    @Override
    public void execute(ExpenseManager expenseManager, Ui ui) {
        LOGGER.fine("Executing list command");
        ui.showSeparator();
        ui.showFinancialSummary(expenseManager.getBudgetData(), expenseManager.getExpenses());
        ui.showSeparator();
    }
}
