package seedu.orcashbuddy.exception;

/**
 * Custom exception class for orCASHbuddy application-specific errors.
 * Provides factory methods for creating common exception types with appropriate messages.
 */
public class OrCashBuddyException extends Exception {

    /**
     * Creates a new OrCashBuddyException with the specified message.
     *
     * @param message the exception message
     */
    public OrCashBuddyException(String message) {
        super(message);
    }

    /**
     * Creates a new OrCashBuddyException with the specified message and cause.
     *
     * @param message the exception message
     * @param cause the underlying cause
     */
    public OrCashBuddyException(String message, Throwable cause) {
        super(message, cause);
    }

    //@@author limzerui
    // ========== Amount-Related Exceptions ==========
    /**
     * Creates an exception for missing amount prefix 'a/'.
     *
     * @return OrCashBuddyException for missing amount prefix
     */
    public static OrCashBuddyException missingAmountPrefix() {
        return new OrCashBuddyException("Missing amount prefix 'a/'");
    }

    /**
     * Creates an exception for empty amount field.
     *
     * @return OrCashBuddyException for empty amount
     */
    public static OrCashBuddyException emptyAmount() {
        return new OrCashBuddyException("Amount is missing after 'a/'");
    }

    /**
     * Creates an exception for invalid amount format.
     *
     * @param amountStr the invalid amount string
     * @return OrCashBuddyException for invalid amount
     */
    public static OrCashBuddyException invalidAmount(String amountStr) {
        return new OrCashBuddyException("Amount is not a valid decimal: " + amountStr);
    }

    /**
     * Creates an exception for invalid amount format with cause.
     *
     * @param amountStr the invalid amount string
     * @param cause the underlying parsing exception
     * @return OrCashBuddyException for invalid amount
     */
    public static OrCashBuddyException invalidAmount(String amountStr, Throwable cause) {
        return new OrCashBuddyException("Amount is not a valid decimal: " + amountStr, cause);
    }

    /**
     * Creates an exception for non-positive amounts.
     *
     * @param amountStr the invalid amount string
     * @return OrCashBuddyException for non-positive amount
     */
    public static OrCashBuddyException amountNotPositive(String amountStr) {
        return new OrCashBuddyException("Amount must be greater than 0: " + amountStr);
    }

    // ========== Description-Related Exceptions ==========

    /**
     * Creates an exception for missing description prefix 'desc/'.
     *
     * @return OrCashBuddyException for missing description prefix
     */
    public static OrCashBuddyException missingDescriptionPrefix() {
        return new OrCashBuddyException("Missing description prefix 'desc/'");
    }

    /**
     * Creates an exception for empty description field.
     *
     * @return OrCashBuddyException for empty description
     */
    public static OrCashBuddyException emptyDescription() {
        return new OrCashBuddyException("Description is missing after 'desc/'");
    }

    //@@author saheer17
    // ========== Index-Related Exceptions ==========

    /**
     * Creates an exception for missing expense index.
     *
     * @param commandName the command that requires an index
     * @return OrCashBuddyException for missing index
     */
    public static OrCashBuddyException missingExpenseIndex(String commandName) {
        return new OrCashBuddyException("Missing expense index after '" + commandName + "' command");
    }

    /**
     * Creates an exception for invalid expense index format.
     *
     * @return OrCashBuddyException for invalid index format
     */
    public static OrCashBuddyException invalidExpenseIndex() {
        return new OrCashBuddyException("Expense index must be an integer");
    }

    /**
     * Creates an exception for invalid expense index format with cause.
     *
     * @param cause the underlying parsing exception
     * @return OrCashBuddyException for invalid index format
     */
    public static OrCashBuddyException invalidExpenseIndex(Throwable cause) {
        return new OrCashBuddyException("Expense index must be an integer", cause);
    }

    /**
     * Creates an exception for expense index less than 1.
     *
     * @return OrCashBuddyException for index too small
     */
    public static OrCashBuddyException expenseIndexTooSmall() {
        return new OrCashBuddyException("Expense index must be at least 1");
    }

    /**
     * Creates an exception for expense index out of valid range.
     *
     * @param index the invalid index
     * @param maxIndex the maximum valid index (size of expense list)
     * @return OrCashBuddyException for invalid expense range
     */
    public static OrCashBuddyException expenseIndexOutOfRange(int index, int maxIndex) {
        if (maxIndex == 0) {
            return emptyExpenseList();
        }
        return new OrCashBuddyException(
                "Expense index must be between 1 and " + maxIndex + ", but got " + index);
    }

    //@@author aydrienlaw
    // ========== Budget-Related Exceptions ==========

    /**
     * Creates an exception for missing budget amount.
     *
     * @return OrCashBuddyException for missing budget
     */
    public static OrCashBuddyException missingBudgetAmount() {
        return new OrCashBuddyException("Missing budget amount");
    }

    // ========== Expense List Exceptions ==========

    /**
     * Creates an exception for operations on empty expense lists.
     *
     * @return OrCashBuddyException for empty expense list
     */
    public static OrCashBuddyException emptyExpenseList() {
        return new OrCashBuddyException("No expenses available. Add some expenses first.");
    }
}