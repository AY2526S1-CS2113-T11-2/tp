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
     * Constructs an {@code InvalidCommand} for unknown commands
     * with no specific parsing error.
     */
    public InvalidCommand() {
        this.exception = null;
    }

    /**
     * Constructs an {@code InvalidCommand} storing parsing/validation error details.
     *
     * @param exception the exception containing the cause of failure
     */
    public InvalidCommand(OrCashBuddyException exception) {
        this.exception = exception;
    }

    /**
     * Displays a usage hint based on the error context.
     *
     * @param expenseManager the central data model that stores all expenses and budget state (unused)
     * @param ui the UI used to show output to the user
     */
    @Override
    public void execute(ExpenseManager expenseManager, Ui ui) {
        ui.showSeparator();
        if (exception == null) {
            ui.showUnknownCommand();
            ui.showSeparator();
            return;
        }

        String errorMessage = exception.getMessage();
        showContextualUsage(errorMessage, ui);
        ui.showSeparator();
    }

    //@@author limzerui
    /**
     * Shows appropriate usage message based on error message content.
     * All errors display both the error message and a usage hint based on the command type.
     *
     * @param errorMessage the error message from the exception
     * @param ui the UI to display contextual usage
     */
    private void showContextualUsage(String errorMessage, Ui ui) {
        ui.showError(errorMessage);

        // Show usage hints based on string matching
        if (errorMessage.contains("'edit'") || errorMessage.contains("id/")) {
            ui.showEditUsage();
        } else if (errorMessage.contains("find") || errorMessage.contains("search criteria") ||
                errorMessage.contains("search criterion")) {
            ui.showFindUsage();
        } else if (errorMessage.contains("budget") || errorMessage.contains("Budget")) {
            ui.showSetBudgetUsage();
        } else if (errorMessage.contains("'add'") || errorMessage.contains("a/") ||
                errorMessage.contains("desc/") || errorMessage.contains("Description") ||
                errorMessage.contains("cat/") || errorMessage.contains("Category")) {
            ui.showAddUsage();
        } else if (errorMessage.contains("'delete'")) {
            ui.showDeleteUsage();
        } else if (errorMessage.contains("'mark'")) {
            ui.showMarkUsage();
        } else if (errorMessage.contains("'unmark'")) {
            ui.showUnmarkUsage();
        }
    }
}
