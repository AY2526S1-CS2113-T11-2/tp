package seedu.orcashbuddy.storage;

/**
 * A data transfer object that encapsulates budget-related information.
 * This class provides a convenient way to pass budget, total expenses,
 * and remaining balance together without multiple method calls.
 */
public record BudgetData(double budget, double totalExpenses, double remainingBalance) {
}
