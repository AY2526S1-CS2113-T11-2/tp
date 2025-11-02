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

    /**
     * Shows appropriate usage message based on error context.
     * For specific validation errors, only the error message is shown.
     * For structural/parsing errors, a usage hint is also displayed.
     *
     * @param errorMessage the error message from the exception
     * @param ui the UI to display contextual usage
     */
    private void showContextualUsage(String errorMessage, Ui ui) {
        System.out.println(errorMessage);

        // Check if this is a specific validation error (already informative on its own)
        if (isSpecificValidationError(errorMessage)) {
            return; // Don't show generic usage hint for specific validation errors
        }

        // For structural/parsing errors, show contextual usage hints
        if (errorMessage.contains("'edit'")) {
            ui.showEditUsage();
            return;
        }

        if (errorMessage.contains("find") || errorMessage.contains("search criteria") ||
                errorMessage.contains("search criterion")) {
            ui.showFindUsage();
            return;
        }

        if (errorMessage.contains("'add'") || errorMessage.contains("desc/") ||
                errorMessage.contains("Description") || errorMessage.contains("cat/") ||
                errorMessage.contains("Category")) {
            ui.showAddUsage();
            return;
        }

        if (errorMessage.contains("'delete'")) {
            ui.showDeleteUsage();
            return;
        }

        if (errorMessage.contains("budget") || errorMessage.contains("Budget")) {
            ui.showSetBudgetUsage();
            return;
        }

        if (errorMessage.contains("'mark'")) {
            ui.showMarkUsage();
            return;
        }

        if (errorMessage.contains("'unmark'")) {
            ui.showUnmarkUsage();
        }
    }

    /**
     * Checks if an error message is a specific validation error that is already
     * informative on its own and doesn't need a generic usage hint.
     *
     * @param errorMessage the error message to check
     * @return true if this is a specific validation error
     */
    private boolean isSpecificValidationError(String errorMessage) {
        return errorMessage.contains("must start with a letter") ||
                errorMessage.contains("must be at least") ||
                errorMessage.contains("not a valid decimal") ||
                errorMessage.contains("too large") ||
                errorMessage.contains("supports ASCII characters only");
    }
}
