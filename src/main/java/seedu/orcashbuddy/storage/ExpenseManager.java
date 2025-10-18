package seedu.orcashbuddy.storage;

import seedu.orcashbuddy.exception.OrCashBuddyException;
import seedu.orcashbuddy.expense.Expense;
import seedu.orcashbuddy.ui.Ui;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages a list of expenses.
 */
public class ExpenseManager {
    private static final Logger LOGGER = Logger.getLogger(ExpenseManager.class.getName());
    private final ArrayList<Expense> expenses;
    private double budget = 0.0;
    private double totalExpenses = 0.0;
    private double remainingBalance = 0.0;

    /**
     * Constructs an ExpenseManager.
     */
    public ExpenseManager() {
        this.expenses = new ArrayList<>();
    }

    //@@author limzerui
    /**
     * Adds an expense to the list.
     *
     * @param expense the expense to add
     */
    public void addExpense(Expense expense) {
        assert expense != null : "Expense must not be null";
        assert expense.getAmount() > 0.0 : "Parsed expense must be positive";
        assert !expense.getDescription().isBlank() : "Parsed expense description must not be blank";
        assert !expense.getCategory().isBlank() : "Parsed expense category must not be blank";

        expenses.add(expense);
        LOGGER.log(Level.INFO, "Added expense amount={0}, desc={1}, category={2}",
                new Object[]{expense.getAmount(), expense.getDescription(), expense.getCategory()});
        LOGGER.fine(() -> "Expense list size is now " + expenses.size());
    }

    //@@author aydrienlaw
    /**
     * Sets the budget amount.
     *
     * @param budget the new budget amount
     */
    public void setBudget(double budget) {
        assert budget > 0.0 : "Budget must be positive";

        this.budget = budget;
        recalculateRemainingBalance();
        LOGGER.log(Level.INFO, "Budget set to {0}", budget);
    }

    //@@author saheer17
    /**
     * Deletes an expense at the specified index.
     *
     * @param index the 1-based index of the expense to delete
     * @return the deleted expense
     * @throws OrCashBuddyException if the index is out of range
     */
    public Expense deleteExpense(int index) throws OrCashBuddyException {
        assert index >= 1 && index <= expenses.size() : "Parsed index out of valid range";
        validateIndex(index);

        Expense removedExpense = expenses.remove(index - 1);
        assert removedExpense != null : "Removed expense should not be null";

        // Rebalance if a marked expense was deleted
        if (removedExpense.isMarked()) {
            totalExpenses -= removedExpense.getAmount();
            assert totalExpenses >= 0.0 : "Total expenses should not go negative after delete";
            recalculateRemainingBalance();
            assert remainingBalance == budget - totalExpenses
                    : "Remaining balance must equal budget minus total expenses after delete";
        }

        LOGGER.log(Level.INFO, "Deleted expense at index {0}: {1}",
                    new Object[]{index, removedExpense.getDescription()});
        return removedExpense;
    }

    //@@author muadzyamani
    /**
     * Marks an expense at the specified index as paid.
     *
     * @param index the 1-based index of the expense to mark
     * @return the marked expense
     * @throws OrCashBuddyException if the index is out of range
     */
    public Expense markExpense(int index) throws OrCashBuddyException {
        assert index >= 1: "Index must be at least 1";
        validateIndex(index);

        Expense expense = expenses.get(index - 1);
        expense.mark();
        updateBudgetAfterMark(expense);

        return expense;
    }

    /**
     * Unmarks an expense at the specified index.
     *
     * @param index the 1-based index of the expense to unmark
     * @return the unmarked expense
     * @throws OrCashBuddyException if the index is out of range
     */
    public Expense unmarkExpense(int index) throws OrCashBuddyException {
        assert index >= 1 : "Index must be at least 1";
        validateIndex(index);

        Expense expense = expenses.get(index - 1);
        expense.unmark();
        updateBudgetAfterUnmark(expense);

        return expense;
    }

    //@@author gumingyoujia
    /**
     * Displays the list of expenses with budget information.
     *
     * @param ui the UI to display the information
     */
    public void displayList(Ui ui){
        LOGGER.fine("displayList invoked.");
        assert ui != null : "Ui must not be null";
        assert budget >= 0.0 : "Budget should never be negative";
        assert totalExpenses >= 0.0 : "Total expenses should never be negative";
        assert remainingBalance == budget - totalExpenses
                : "Remaining balance must equal budget minus total expenses";
        ui.showList(totalExpenses, budget, remainingBalance, expenses);
        LOGGER.fine(() -> "Expenses listed.");
    }

    /**
     * Updates budget tracking when an expense is marked as paid.
     * Adds the expense amount to total expenses and recalculates remaining balance.
     *
     * @param expense the expense that was marked
     */
    private void updateBudgetAfterMark(Expense expense) {
        assert expense != null : "Expense must not be null";

        totalExpenses += expense.getAmount();
        recalculateRemainingBalance();

        LOGGER.info(() -> "Updated budget after mark: total=" + totalExpenses +
                ", remaining=" + remainingBalance);
    }

    /**
     * Updates budget tracking when an expense is unmarked.
     * Subtracts the expense amount from total expenses and recalculates remaining balance.
     *
     * @param expense the expense that was unmarked
     */
    private void updateBudgetAfterUnmark(Expense expense) {
        assert expense != null : "Expense must not be null";

        totalExpenses -= expense.getAmount();
        recalculateRemainingBalance();

        LOGGER.info(() -> "Updated budget after unmark: total=" + totalExpenses +
                ", remaining=" + remainingBalance);
    }

    /**
     * Recalculates the remaining balance.
     */
    private void recalculateRemainingBalance() {
        remainingBalance = budget - totalExpenses;
    }

    //@@author aydrienlaw
    /**
     * Validates that an index is within the valid range.
     *
     * @param index the 1-based index to validate
     * @throws OrCashBuddyException if the index is out of range
     */
    private void validateIndex(int index) throws OrCashBuddyException {
        if (expenses.isEmpty()) {
            throw OrCashBuddyException.emptyExpenseList();
        }
        if (index < 1 || index > expenses.size()) {
            throw OrCashBuddyException.expenseIndexOutOfRange(index, expenses.size());
        }
    }

    //@@author saheer17
    /**
     * Sorts the expenses in descending order by amount and displays them using the UI.
     *
     * @param ui the UI to display the sorted list of expenses or error message if list is empty.
     */
    public void sortExpenses(Ui ui) {
        assert ui != null : "Ui must not be null";
        if (expenses.isEmpty()) {
            LOGGER.info("Unable to sort expenses as list is empty");
            ui.showListUsage();
            return;
        }
        LOGGER.info("Sorting expenses in descending order by amount");
        ArrayList<Expense> sortedExpenses = new ArrayList<>(expenses);
        sortedExpenses.sort((e1, e2) -> Double.compare(e2.getAmount(), e1.getAmount()));
        assert sortedExpenses.size() == expenses.size() : "Sorted expenses size should match original expenses size";
        ui.showSortedList(sortedExpenses);
    }

    //@@author muadzyamani
    /**
     * Finds all expenses that match the specified category (case-insensitive).
     *
     * @param category the category to search for
     * @return list of expenses matching the category
     */
    public ArrayList<Expense> findExpensesByCategory(String category) {
        assert category != null && !category.isBlank() : "Category must not be blank";

        ArrayList<Expense> foundExpenses = new ArrayList<>();
        String searchTerm = category.toLowerCase().trim();

        for (Expense expense : expenses) {
            if (expense.getCategory().toLowerCase().contains(searchTerm)) {
                foundExpenses.add(expense);
            }
        }

        LOGGER.log(Level.INFO, "Found {0} expenses matching category: {1}",
                new Object[]{foundExpenses.size(), category});

        return foundExpenses;
    }

    /**
     * Finds all expenses that match the specified description keyword (case-insensitive).
     *
     * @param keyword the keyword to search for in descriptions
     * @return list of expenses matching the keyword
     */
    public ArrayList<Expense> findExpensesByDescription(String keyword) {
        assert keyword != null && !keyword.isBlank() : "Keyword must not be blank";

        ArrayList<Expense> foundExpenses = new ArrayList<>();
        String searchTerm = keyword.toLowerCase().trim();

        for (Expense expense : expenses) {
            if (expense.getDescription().toLowerCase().contains(searchTerm)) {
                foundExpenses.add(expense);
            }
        }

        LOGGER.log(Level.INFO, "Found {0} expenses matching description: {1}",
                new Object[]{foundExpenses.size(), keyword});

        return foundExpenses;
    }
}
