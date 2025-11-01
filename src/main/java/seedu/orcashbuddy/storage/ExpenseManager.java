package seedu.orcashbuddy.storage;

import seedu.orcashbuddy.exception.OrCashBuddyException;
import seedu.orcashbuddy.expense.Expense;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Central data model for orCASHbuddy.
 *
 * <p>This class stores:
 * <ul>
 *   <li>All recorded {@link Expense} objects</li>
 *   <li>The user's current budget</li>
 *   <li>The running total of money spent (based on marked expenses)</li>
 *   <li>The remaining balance</li>
 * </ul>
 *
 * <p>Key invariants:
 * <ul>
 *   <li>{@code totalExpenses} is the sum of amounts of all <b>marked</b> expenses.</li>
 *   <li>{@code remainingBalance} is always {@code budget - totalExpenses}.</li>
 *   <li>All user-facing indexes are 1-based (the first expense is index 1).</li>
 * </ul>
 *
 * <p>Responsibilities:
 * <ul>
 *   <li>Adding, deleting, and replacing expenses</li>
 *   <li>Marking / unmarking expenses as "paid"</li>
 *   <li>Maintaining budget totals when expenses are marked/unmarked/deleted</li>
 *   <li>Sorting and searching expenses</li>
 *   <li>Exposing budget status for UI alerts</li>
 * </ul>
 */
public class ExpenseManager implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ExpenseManager.class.getName());
    private static final double BUDGET_ALERT_THRESHOLD = 10.0;
    private static final double FLOAT_NOISE_THRESHOLD =0.001;

    // ========== State ==========
    private final List<Expense> expenses;
    private double budget = 0.0;
    private double totalExpenses = 0.0;
    private double remainingBalance = 0.0;

    /**
     * Constructs a new, empty ExpenseManager with no expenses
     * and a budget of 0.
     */
    public ExpenseManager() {
        this.expenses = new ArrayList<>();
    }

    // ========== Getters ==========
    //@@author gumingyoujia
    /**
     * Returns the current budget amount.
     *
     * @return the user's budget
     */
    public double getBudget() {
        return budget;
    }

    /**
     * Returns the total money currently counted as "spent".
     * This is the sum of all expenses that are marked.
     *
     * @return total of all marked expenses
     */
    public double getTotalExpenses() {
        return totalExpenses;
    }

    /**
     * Returns the remaining balance in the budget.
     * This is {@code budget - totalExpenses}.
     *
     * @return remaining balance (may be negative if overspent)
     */
    public double getRemainingBalance() {
        return remainingBalance;
    }

    //@@author
    /**
     * Returns the number of expenses currently tracked.
     *
     * @return number of expenses
     */
    public int getSize() {
        return expenses.size();
    }

    //@@author gumingyoujia
    /**
     * Returns the full list of expenses.
     * The returned list is the live internal list, so callers
     * should not modify it directly unless they intend to mutate
     * model state.
     *
     * @return the internal {@link List} of expenses
     */
    public List<Expense> getExpenses() {
        return expenses;
    }

    // ========== Expense Operations ==========

    //@@author limzerui
    /**
     * Adds a new expense to the list.
     *
     * @param expense the expense to add
     * @throws IllegalArgumentException if {@code expense} is null
     */
    public void addExpense(Expense expense) {
        validateExpense(expense);

        expenses.add(expense);
        LOGGER.log(Level.INFO, "Added expense amount={0}, desc={1}, category={2}",
                new Object[]{expense.getAmount(), expense.getDescription(), expense.getCategory()});
        LOGGER.fine(() -> "Expense list size is now " + expenses.size());
    }

    //@@author saheer17
    /**
     * Deletes the expense at the given position.
     * <p>If the deleted expense was marked, the budget totals are
     * adjusted (the amount is subtracted from {@code totalExpenses}
     * and {@code remainingBalance} is recalculated).</p>
     *
     * @param index the 1-based index of the expense to delete
     * @return the deleted expense
     * @throws OrCashBuddyException if the index is out of range
     */
    public Expense deleteExpense(int index) throws OrCashBuddyException {
        validateIndex(index);

        Expense removedExpense = expenses.remove(index - 1);
        assert removedExpense != null : "Removed expense should not be null";

        // Rebalance if a marked expense was deleted
        if (removedExpense.isMarked()) {
            updateBudgetAfterUnmark(removedExpense);
            assert totalExpenses >= 0 : "Total expenses became negative after deletion";
        }

        LOGGER.log(Level.INFO, "Deleted expense at index {0}: {1}",
                    new Object[]{index, removedExpense.getDescription()});
        return removedExpense;
    }

    //@@author gumingyoujia
    /**
     * Returns the expense at the given position.
     *
     * @param index the 1-based index of the target expense (as shown in 'list')
     * @return the expense at that position
     * @throws OrCashBuddyException if the index is out of range
     */
    public Expense getExpense(int index) throws OrCashBuddyException{
        validateIndex(index);

        LOGGER.log(Level.FINE, "Getting expense at index {0}", index);
        return expenses.get(index - 1);
    }

    /**
     * Replaces the expense at the given position with a new expense.
     * <ul>
     *   <li>Preserves the marked/unmarked state of the original expense.</li>
     *   <li>Updates budget totals if needed.</li>
     * </ul>
     *
     * @param index      the 1-based index of the expense to replace
     * @param newExpense the new expense to insert
     * @throws OrCashBuddyException if the index is out of range
     * @throws IllegalArgumentException if {@code newExpense} is null
     */
    public void replaceExpense(int index, Expense newExpense) throws OrCashBuddyException {
        validateIndex(index);
        validateExpense(newExpense);

        LOGGER.log(Level.INFO, "Replacing expense at index {0}", index);

        Expense removedExpense = deleteExpense(index);
        expenses.add(index - 1, newExpense);
    }

    //@@author muadzyamani
    /**
     * Marks the specified expense as paid.
     * <ul>
     *   <li>If it was previously unmarked, it is marked.</li>
     *   <li>The expense amount is added to {@code totalExpenses}.</li>
     *   <li>{@code remainingBalance} is recalculated.</li>
     * </ul>
     *
     * @param index the 1-based index of the expense to mark
     * @return the marked expense
     * @throws OrCashBuddyException if the index is out of range
     */
    public Expense markExpense(int index) throws OrCashBuddyException {
        validateIndex(index);

        Expense expense = expenses.get(index - 1);
        if (!expense.isMarked()) {
            expense.mark();
            updateBudgetAfterMark(expense);
        }

        return expense;
    }

    /**
     * Unmarks the specified expense.
     * <ul>
     *   <li>If it was previously marked, it becomes unmarked.</li>
     *   <li>The expense amount is subtracted from {@code totalExpenses}.</li>
     *   <li>{@code remainingBalance} is recalculated.</li>
     * </ul>
     *
     * @param index the 1-based index of the expense to unmark
     * @return the unmarked expense
     * @throws OrCashBuddyException if the index is out of range
     */
    public Expense unmarkExpense(int index) throws OrCashBuddyException {
        validateIndex(index);

        Expense expense = expenses.get(index - 1);
        if (expense.isMarked()) {
            expense.unmark();
            updateBudgetAfterUnmark(expense);
        }

        return expense;
    }

    // ========== Budget Operations ==========

    //@@author aydrienlaw
    /**
     * Sets the user's budget and recalculates {@code remainingBalance}.
     *
     * @param budget the new budget amount (must be > 0)
     * @throws AssertionError if {@code budget <= 0}
     */
    public void setBudget(double budget) {
        assert budget > 0.0 : "Budget must be positive";

        this.budget = budget;
        recalculateRemainingBalance();

        LOGGER.log(Level.INFO, "Budget set to {0}", budget);
    }

    //@@author gumingyoujia
    /**
     * Returns a summary of how the user's current spending compares
     * to their budget. Used by the UI to decide which alert to show.
     *
     * @return a {@link BudgetStatus} value such as OK, NEAR, EQUAL, or EXCEEDED
     */
    public BudgetStatus determineBudgetStatus() {
        if (remainingBalance < -FLOAT_NOISE_THRESHOLD) {
            return BudgetStatus.EXCEEDED;
        } else if (remainingBalance >= -FLOAT_NOISE_THRESHOLD && remainingBalance <= FLOAT_NOISE_THRESHOLD) {
            return BudgetStatus.EQUAL;
        } else if (remainingBalance < BUDGET_ALERT_THRESHOLD + BUDGET_ALERT_THRESHOLD) {
            return BudgetStatus.NEAR;
        }
        return BudgetStatus.OK;
    }

    // ========== Display Operations ==========

    //@@author saheer17
    /**
     * Returns a new list of all expenses sorted by amount in descending order.
     * The original list is not mutated.
     *
     * @return a new {@link List} of expenses sorted from highest to lowest amount
     */
    public List<Expense> sortExpenses() {
        if (expenses.isEmpty()) {
            LOGGER.info("Cannot sort expenses - list is empty");
            return expenses;
        }

        LOGGER.info("Sorting expenses by amount in descending order");
        List<Expense> sortedExpenses = new ArrayList<>(expenses);
        sortedExpenses.sort((e1, e2) -> Double.compare(e2.getAmount(), e1.getAmount()));
        assert sortedExpenses.size() == expenses.size() : "Sorted expenses size should match original expenses size";
        return sortedExpenses;
    }

    // ========== Search Operations ==========

    //@@author muadzyamani
    /**
     * Finds all expenses whose category contains the given text (case-insensitive).
     *
     * @param category the category substring to match
     * @return all matching expenses
     * @throws IllegalArgumentException if {@code category} is null or blank
     */
    public List<Expense> findExpensesByCategory(String category) {
        validateSearchTerm(category, "Category");

        String searchTerm = category.toLowerCase().trim();
        List<Expense> foundExpenses = new ArrayList<>();

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
     * Finds all expenses whose description contains the given text (case-insensitive).
     *
     * @param keyword the search substring
     * @return all matching expenses
     * @throws IllegalArgumentException if {@code keyword} is null or blank
     */
    public List<Expense> findExpensesByDescription(String keyword) {
        validateSearchTerm(keyword, "Keyword");

        String searchTerm = keyword.toLowerCase().trim();
        List<Expense> foundExpenses = new ArrayList<>();

        for (Expense expense : expenses) {
            if (expense.getDescription().toLowerCase().contains(searchTerm)) {
                foundExpenses.add(expense);
            }
        }

        LOGGER.log(Level.INFO, "Found {0} expenses matching description: {1}",
                new Object[]{foundExpenses.size(), keyword});

        return foundExpenses;
    }

    // ========== Private Helper Methods ==========

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
        assert totalExpenses >= 0 : "Total expenses must not be negative after unmarking";
        recalculateRemainingBalance();

        LOGGER.info(() -> "Updated budget after unmark: total=" + totalExpenses +
                ", remaining=" + remainingBalance);
    }

    //@@author gumingyoujia
    /**
     * Recalculates the remaining balance based on budget and total expenses.
     */
    private void recalculateRemainingBalance() {
        remainingBalance = budget - totalExpenses;
        if  (remainingBalance < FLOAT_NOISE_THRESHOLD && remainingBalance >= -FLOAT_NOISE_THRESHOLD) {
            remainingBalance=0;
        }
        assert Math.abs(remainingBalance - (budget - totalExpenses)) < 0.001
                : "Remaining balance calculation error";
    }

    // ========== Validation Methods ==========

    //@@author aydrienlaw
    /**
     * Validates that an expense object is valid.
     *
     * @param expense the expense to validate
     * @throws IllegalArgumentException if expense is invalid
     */
    private void validateExpense(Expense expense) {
        if (expense == null) {
            throw new IllegalArgumentException("Expense must not be null");
        }
        if (expense.getAmount() <= 0.0) {
            throw new IllegalArgumentException("Expense amount must be positive");
        }
        if (expense.getDescription().isBlank()) {
            throw new IllegalArgumentException("Expense description must not be blank");
        }
        if (expense.getCategory().isBlank()) {
            throw new IllegalArgumentException("Expense category must not be blank");
        }
    }

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

    /**
     * Validates that a search term is not null or blank.
     *
     * @param searchTerm the search term to validate
     * @param fieldName the name of the field for error messages
     * @throws IllegalArgumentException if search term is invalid
     */
    private void validateSearchTerm(String searchTerm, String fieldName) {
        if (searchTerm == null || searchTerm.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
    }
}
