package seedu.orcashbuddy.parser;

import seedu.orcashbuddy.exception.OrCashBuddyException;
import seedu.orcashbuddy.expense.Expense;

/**
 * Provides static helpers to sanity-check raw argument values
 * extracted from the user's input.
 * <p>
 * This class enforces constraints such as:
 * <ul>
 *     <li>Amount is a valid positive number</li>
 *     <li>Description is non-empty</li>
 *     <li>Category is non-empty and alphanumeric (within limits)</li>
 *     <li>Index is a positive integer</li>
 * </ul>
 *
 * The methods here throw {@link OrCashBuddyException} with user-friendly
 * messages so the caller can surface them directly.
 */
public class InputValidator {

    /**
     * Maximum allowed amount to prevent precision loss with double representation.
     * Set to 1 trillion, which is well within double precision limits.
     */
    private static final double MAX_AMOUNT = 1_000_000_000_000.0; // 1 trillion

    /**
     * Regex pattern for validating category format.
     * Must start with a letter and contain only letters, numbers, spaces, or hyphens.
     * Maximum length is 20 characters.
     */
    private static final String CATEGORY_PATTERN = "[A-Za-z][A-Za-z0-9\\s-]{0,19}";

    //@@author limzerui

    /**
     * Validates and parses an amount string.
     *
     * @param amountStr   raw string after {@code a/}
     * @param commandName the command being validated (used in error messages)
     * @return the parsed amount as a double
     * @throws OrCashBuddyException if the amount is missing, non-numeric, or not positive
     */
    public static double validateAmount(String amountStr, String commandName) throws OrCashBuddyException {
        if (amountStr == null || amountStr.isEmpty()) {
            throw OrCashBuddyException.emptyAmount(commandName);
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            throw OrCashBuddyException.invalidAmount(amountStr);
        }

        if (Double.isNaN(amount) || Double.isInfinite(amount)) {
            throw OrCashBuddyException.invalidAmount(amountStr);
        }

        if (amount < 0.01) {
            throw OrCashBuddyException.amountNotPositive(amountStr);
        }

        if (amount > MAX_AMOUNT) {
            throw OrCashBuddyException.amountTooLarge(amountStr);
        }

        amount = Math.floor(amount * 100.0) / 100.0;

        return amount;
    }

    /**
     * Validates that a description is present and non-empty,
     * and trims surrounding whitespace.
     *
     * @param description raw description after {@code desc/}
     * @param commandName the command being validated (for contextual error messages)
     * @return a trimmed, non-empty description
     * @throws OrCashBuddyException if the description is blank or missing
     */
    public static String validateDescription(String description, String commandName) throws OrCashBuddyException {
        if (description == null || description.trim().isEmpty()) {
            throw OrCashBuddyException.emptyDescription(commandName);
        }
        String trimmed = description.trim();
        ensureAscii(trimmed, "Description");
        return trimmed;
    }

    /**
     * Validates and normalizes a category string.
     * If {@code null}, falls back to {@link Expense#DEFAULT_CATEGORY}.
     * Otherwise, ensures it meets format constraints (alphanumeric and reasonable length).
     *
     * @param category    raw category after {@code cat/}
     * @param commandName the command being validated
     * @return a trimmed, valid category string
     * @throws OrCashBuddyException if the category is empty or malformed
     */
    public static String validateCategory(String category, String commandName) throws OrCashBuddyException {
        if (category == null) {
            return Expense.DEFAULT_CATEGORY;
        }

        String trimmed = category.trim();
        if (trimmed.isEmpty()) {
            throw OrCashBuddyException.emptyCategory(commandName);
        }

        ensureAscii(trimmed, "Category");

        if (!trimmed.matches(CATEGORY_PATTERN)) {
            throw OrCashBuddyException.invalidCategory(trimmed);
        }

        return trimmed;
    }

    //@@author saheer17
    /**
     * Validates an index argument used for commands like delete/mark/edit.
     *
     * @param input       the raw index string (e.g. "3")
     * @param commandName the command being validated
     * @return the parsed 1-based index as an int
     * @throws OrCashBuddyException if the index is missing, not a valid number, or less than 1
     */
    public static int validateIndex(String input, String commandName) throws OrCashBuddyException {
        if (input == null || input.isEmpty()) {
            throw OrCashBuddyException.missingExpenseIndex(commandName);
        }

        String trimmed = input.trim();

        try {
            int index = Integer.parseInt(trimmed);
            if (index < 1) {
                throw OrCashBuddyException.expenseIndexTooSmall();
            }
            return index;
        } catch (NumberFormatException e) {
            if (trimmed.matches("-?\\d+")) {
                throw OrCashBuddyException.expenseIndexTooLarge();
            }
            throw OrCashBuddyException.invalidExpenseIndex();
        }
    }

    static void ensureAscii(String value, String fieldName) throws OrCashBuddyException {
        boolean isAscii = value.chars().allMatch(ch -> ch >= 0x20 && ch <= 0x7E);
        if (!isAscii) {
            throw OrCashBuddyException.nonAsciiInput(fieldName);
        }
    }
}
