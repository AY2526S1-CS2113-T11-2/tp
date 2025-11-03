package seedu.orcashbuddy.parser;

import org.junit.jupiter.api.Test;
import seedu.orcashbuddy.exception.OrCashBuddyException;
import seedu.orcashbuddy.expense.Expense;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InputValidatorTest {

    private static final String COMMAND = "test";

    @Test
    void validateAmount_validNumber_returnsDouble() throws Exception {
        assertEquals(12.5, InputValidator.validateAmount("12.5", COMMAND), 1e-6);
    }

    @Test
    void validateAmount_null_throws() {
        assertThrows(OrCashBuddyException.class, () -> InputValidator.validateAmount(null, COMMAND));
    }

    @Test
    void validateAmount_notNumeric_throws() {
        assertThrows(OrCashBuddyException.class, () -> InputValidator.validateAmount("abc", COMMAND));
    }

    @Test
    void validateAmount_notPositive_throws() {
        assertThrows(OrCashBuddyException.class, () -> InputValidator.validateAmount("-5", COMMAND));
    }

    @Test
    void validateAmount_belowCent_throws() {
        assertThrows(OrCashBuddyException.class, () -> InputValidator.validateAmount("0.009", COMMAND));
    }

    @Test
    void validateAmount_minimumCent_allowed() throws Exception {
        assertEquals(0.01, InputValidator.validateAmount("0.01", COMMAND), 1e-6);
    }

    @Test
    void validateAmount_nan_throws() {
        assertThrows(OrCashBuddyException.class, () -> InputValidator.validateAmount("NaN", COMMAND));
    }

    @Test
    void validateAmount_infinity_throws() {
        assertThrows(OrCashBuddyException.class, () -> InputValidator.validateAmount("Infinity", COMMAND));
    }

    @Test
    void validateAmount_negativeInfinity_throws() {
        assertThrows(OrCashBuddyException.class, () -> InputValidator.validateAmount("-Infinity", COMMAND));
    }

    @Test
    void validateAmount_maximumAllowed_allowed() throws Exception {
        // 1 trillion should be accepted
        assertEquals(1_000_000_000_000.0, InputValidator.validateAmount("1000000000000", COMMAND), 1e-6);
    }

    @Test
    void validateAmount_exceedsMaximum_throws() {
        // Over 1 trillion should be rejected
        assertThrows(OrCashBuddyException.class, () ->
            InputValidator.validateAmount("1000000000001", COMMAND));
    }

    @Test
    void validateAmount_veryLargeWithPrecisionLoss_throws() {
        // User's example - 26 digits that would lose precision
        assertThrows(OrCashBuddyException.class, () ->
            InputValidator.validateAmount("22222222222222222222222222", COMMAND));
    }

    @Test
    void validateDescription_valid_returnsTrimmed() throws Exception {
        assertEquals("Lunch", InputValidator.validateDescription("  Lunch  ", COMMAND));
    }

    @Test
    void validateDescription_empty_throws() {
        assertThrows(OrCashBuddyException.class, () -> InputValidator.validateDescription("   ", COMMAND));
    }

    @Test
    void validateCategory_null_returnsDefault() throws Exception {
        assertEquals(Expense.DEFAULT_CATEGORY, InputValidator.validateCategory(null, COMMAND));
    }

    @Test
    void validateCategory_valid_returnsTrimmed() throws Exception {
        assertEquals("Food Court", InputValidator.validateCategory("  Food Court  ", COMMAND));
    }

    @Test
    void validateCategory_empty_throws() {
        assertThrows(OrCashBuddyException.class, () -> InputValidator.validateCategory("   ", COMMAND));
    }

    @Test
    void validateCategory_invalidPattern_throws() {
        assertThrows(OrCashBuddyException.class, () -> InputValidator.validateCategory("123*Food", COMMAND));
    }

    @Test
    void validateDescription_nonAscii_throws() {
        assertThrows(OrCashBuddyException.class, () -> InputValidator.validateDescription("午餐", COMMAND));
    }

    @Test
    void validateCategory_nonAscii_throws() {
        assertThrows(OrCashBuddyException.class, () -> InputValidator.validateCategory("مطبخ", COMMAND));
    }

    @Test
    void validateIndex_valid_returnsInt() throws Exception {
        assertEquals(3, InputValidator.validateIndex("3", COMMAND));
    }

    @Test
    void validateIndex_missing_throws() {
        assertThrows(OrCashBuddyException.class, () -> InputValidator.validateIndex("", COMMAND));
    }

    @Test
    void validateIndex_tooSmall_throws() {
        assertThrows(OrCashBuddyException.class, () -> InputValidator.validateIndex("0", COMMAND));
    }

    @Test
    void validateIndex_notInteger_throws() {
        assertThrows(OrCashBuddyException.class, () -> InputValidator.validateIndex("one", COMMAND));
    }
}
