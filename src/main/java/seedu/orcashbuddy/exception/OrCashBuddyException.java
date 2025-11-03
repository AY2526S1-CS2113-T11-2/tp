package seedu.orcashbuddy.exception;

/**
 * Custom exception class for orCASHbuddy application-specific errors.
 * Provides factory methods for creating common exception types with appropriate messages.
 */
public class OrCashBuddyException extends Exception {

    /**
     * Threshold for detecting integer overflow when parsing expense indices.
     * If an index is within this range of Integer.MAX_VALUE, it's considered overflow.
     */
    private static final int INDEX_OVERFLOW_THRESHOLD = 1000;

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

    // ========== Prefix-Related Exceptions ==========
    /**
     * Creates an exception for missing prefix in command arguments.
     *
     * @param prefix the missing prefix (e.g., "a/", "desc/")
     * @return OrCashBuddyException for missing prefix
     */
    public static OrCashBuddyException missingPrefix(String prefix) {
        return new OrCashBuddyException("Missing prefix: " + prefix);
    }

    //@@author limzerui
    // ========== Amount-Related Exceptions ==========
    /**
     * Creates an exception for empty amount field.
     *
     * @return OrCashBuddyException for empty amount
     */
    public static OrCashBuddyException emptyAmount(String commandName) {
        return new OrCashBuddyException("Amount missing after 'a/' " +
                "for '" + commandName + "' command");
    }

    /**
     * Creates an exception for invalid amount format.
     *
     * @param amountStr the invalid amount string
     * @return OrCashBuddyException for invalid amount
     */
    public static OrCashBuddyException invalidAmount(String amountStr) {
        return new OrCashBuddyException("Amount is not numeric: " + amountStr);
    }
    //@@author

    /**
     * Creates an exception for non-positive amounts.
     *
     * @param amountStr the invalid amount string
     * @return OrCashBuddyException for non-positive amount
     */
    public static OrCashBuddyException amountNotPositive(String amountStr) {
        return new OrCashBuddyException("Amount must be at least $0.01: " + amountStr);
    }

    /**
     * Creates an exception for amounts that are too large.
     *
     * @param amountStr the invalid amount string
     * @return OrCashBuddyException for amount too large
     */
    public static OrCashBuddyException amountTooLarge(String amountStr) {
        return new OrCashBuddyException("Amount is too large and would lose precision: " + amountStr);
    }

    // ========== Description-Related Exceptions ==========

    /**
     * Creates an exception for empty description field.
     *
     * @return OrCashBuddyException for empty description
     */
    public static OrCashBuddyException emptyDescription(String commandName) {
        return new OrCashBuddyException("Description is missing after 'desc/' " +
                "for '" + commandName + "' command");
    }

    /**
     * Creates an exception for empty category field.
     *
     * @return OrCashBuddyException for empty category
     */
    public static OrCashBuddyException emptyCategory(String commandName) {
        return new OrCashBuddyException("Category is missing after 'cat/' for '"
                + commandName + "' command");
    }

    /**
     * Creates an exception for invalid category format.
     *
     * @param category the invalid category string
     * @return OrCashBuddyException for invalid category
     */
    public static OrCashBuddyException invalidCategory(String category) {
        return new OrCashBuddyException(
                "Category must start with a letter and contain only letters, numbers, spaces, or hyphens: "
                        + category);
    }

    /**
     * Creates an exception for non-ASCII text in user input.
     *
     * @param fieldName the name of the field containing non-ASCII characters
     * @return OrCashBuddyException indicating ASCII-only support
     */
    public static OrCashBuddyException nonAsciiInput(String fieldName) {
        return new OrCashBuddyException(fieldName + " supports ASCII characters only.");
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
     * Creates an exception for expense index that is too large to process.
     *
     * @return OrCashBuddyException for index overflow
     */
    public static OrCashBuddyException expenseIndexTooLarge() {
        return new OrCashBuddyException("Expense index is too large to process");
    }

    //@@author muadzyamani
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

        if (index >= Integer.MAX_VALUE - INDEX_OVERFLOW_THRESHOLD) {
            return new OrCashBuddyException(
                    "Expense index must be between 1 and " + maxIndex +
                            ". The number entered is too large to process");
        }

        return new OrCashBuddyException(
                "Expense index must be between 1 and " + maxIndex + ", but got " + index);
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
