package seedu.orcashbuddy.command;

import seedu.orcashbuddy.expense.Expense;
import seedu.orcashbuddy.storage.ExpenseManager;
import seedu.orcashbuddy.ui.Ui;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Command to find expenses by category or description.
 */
public class FindCommand extends Command {
    private static final Logger LOGGER = Logger.getLogger(FindCommand.class.getName());

    private final String searchType; // "category" or "description"
    private final String searchTerm;

    /**
     * Constructs a FindCommand with the specified search type and term.
     *
     * @param searchType the type of search ("category" or "description")
     * @param searchTerm the term to search for
     */
    public FindCommand(String searchType, String searchTerm) {
        this.searchType = searchType;
        this.searchTerm = searchTerm;
    }

    @Override
    public void execute(ExpenseManager expenseManager, Ui ui) {
        assert searchType != null && !searchType.isBlank() : "Search type must not be blank";
        assert searchTerm != null && !searchTerm.isBlank() : "Search term must not be blank";

        LOGGER.log(Level.INFO, "Executing find command: type={0}, term={1}",
                new Object[]{searchType, searchTerm});

        ArrayList<Expense> foundExpenses;

        if (searchType.equals("category")) {
            foundExpenses = expenseManager.findExpensesByCategory(searchTerm);
        } else {
            foundExpenses = expenseManager.findExpensesByDescription(searchTerm);
        }

        LOGGER.log(Level.INFO, "Found {0} matching expenses", foundExpenses.size());

        ui.showFoundExpenses(foundExpenses, searchTerm, searchType);
    }
}
