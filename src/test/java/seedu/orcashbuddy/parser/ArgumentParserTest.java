package seedu.orcashbuddy.parser;

import org.junit.jupiter.api.Test;
import seedu.orcashbuddy.exception.OrCashBuddyException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArgumentParserTest {

    @Test
    void getValue_existingPrefix_returnsTrimmedValue() throws Exception {
        ArgumentParser parser = new ArgumentParser("a/ 42.0 desc/ Latte cat/ Coffee ");
        assertEquals("42.0", parser.getValue("a/"));
        assertEquals("Latte", parser.getValue("desc/"));
    }

    @Test
    void getValue_missingPrefix_throws() {
        ArgumentParser parser = new ArgumentParser("a/5 desc/Snack");
        assertThrows(OrCashBuddyException.class, () -> parser.getValue("cat/"));
    }

    @Test
    void getOptionalValue_missingPrefix_returnsNull() {
        ArgumentParser parser = new ArgumentParser("a/5 desc/Snack");
        assertNull(parser.getOptionalValue("cat/"));
    }

    @Test
    void getOptionalValue_present_returnsTrimmedValue() {
        ArgumentParser parser = new ArgumentParser("a/5 desc/Snack cat/ Treats ");
        assertEquals("Treats", parser.getOptionalValue("cat/"));
    }

    @Test
    void duplicatePrefixInsideValue_treatedAsLiteralText() throws Exception {
        ArgumentParser parser = new ArgumentParser("a/12.50 desc/venue booking a/12345");
        assertEquals("12.50", parser.getValue("a/"));
        assertEquals("venue booking a/12345", parser.getValue("desc/"));
    }
}
