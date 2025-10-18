//@@author aydrienlaw
package seedu.orcashbuddy.command;

import seedu.orcashbuddy.exception.OrCashBuddyException;
import seedu.orcashbuddy.storage.ExpenseManager;
import seedu.orcashbuddy.ui.Ui;

/**
 * Command representing an invalid or unknown command.
 * Handles both unrecognized commands and parsing errors.
 */
public class InvalidCommand extends Command {
    private final OrCashBuddyException exception;

    /**
     * Constructs an InvalidCommand for unknown commands.
     */
    public InvalidCommand() {
        this.exception = null;
    }

    /**
     * Constructs an InvalidCommand with specific parsing error details.
     *
     * @param exception the exception containing error details
     */
    public InvalidCommand(OrCashBuddyException exception) {
        this.exception = exception;
    }

    @Override
    public void execute(ExpenseManager expenseManager, Ui ui) {
        if (exception == null) {
            ui.showUnknownCommand();
            return;
        }

        String errorMessage = exception.getMessage();
        showContextualUsage(errorMessage, ui);
    }

    /**
     * Shows appropriate usage message based on error context.
     *
     * @param errorMessage the error message from the exception
     * @param ui the UI to display messages
     */
    private void showContextualUsage(String errorMessage, Ui ui) {
        if (errorMessage.contains("'add'") || errorMessage.contains("desc/") ||
                errorMessage.contains("Description") || errorMessage.contains("cat/") ||
                errorMessage.contains("Category")) {
            ui.showAddUsage();
        } else if (errorMessage.contains("'delete'")) {
            ui.showDeleteUsage(errorMessage);
        } else if (errorMessage.contains("budget") || errorMessage.contains("Budget")) {
            ui.showSetBudgetUsage();
        } else if (errorMessage.contains("find") || errorMessage.contains("search criteria")) {
            ui.showFindUsage();
        } else if (errorMessage.contains("'mark'")) {
            ui.showMarkUsage(errorMessage);
        } else if (errorMessage.contains("'unmark'")) {
            ui.showUnmarkUsage(errorMessage);
        } else {
            System.out.println(errorMessage);
        }
    }
}
