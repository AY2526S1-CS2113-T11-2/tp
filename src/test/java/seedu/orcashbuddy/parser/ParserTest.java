package seedu.orcashbuddy.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.orcashbuddy.command.AddCommand;
import seedu.orcashbuddy.command.ByeCommand;
import seedu.orcashbuddy.command.EditCommand;
import seedu.orcashbuddy.command.FindCommand;
import seedu.orcashbuddy.command.InvalidCommand;
import seedu.orcashbuddy.command.ListCommand;
import seedu.orcashbuddy.command.MarkCommand;
import seedu.orcashbuddy.command.SetBudgetCommand;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class ParserTest {

    private Parser parser;

    @BeforeEach
    void setUp() {
        parser = new Parser();
    }

    @Test
    void parse_addCommand_returnsAddCommand() {
        assertInstanceOf(AddCommand.class, parser.parse("add a/5 desc/Coffee cat/Drinks"));
    }

    @Test
    void parse_setbudget_returnsSetBudgetCommand() {
        assertInstanceOf(SetBudgetCommand.class, parser.parse("setbudget a/100"));
    }

    @Test
    void parse_mark_returnsMarkCommand() {
        assertInstanceOf(MarkCommand.class, parser.parse("mark 2"));
    }

    @Test
    void parse_list_returnsListCommand() {
        assertInstanceOf(ListCommand.class, parser.parse("list"));
    }

    @Test
    void parse_findByDescription_returnsFindCommand() {
        assertInstanceOf(FindCommand.class, parser.parse("find desc/Grab"));
    }

    @Test
    void parse_findNonAsciiDescription_returnsInvalidCommand() {
        assertInstanceOf(InvalidCommand.class, parser.parse("find desc/午餐"));
    }

    @Test
    void parse_edit_returnsEditCommand() {
        assertInstanceOf(EditCommand.class, parser.parse("edit id/1 desc/New Lunch cat/Food"));
    }

    @Test
    void parse_bye_returnsByeCommand() {
        assertInstanceOf(ByeCommand.class, parser.parse("bye"));
    }

    @Test
    void parse_byeWithArguments_returnsInvalidCommand() {
        assertInstanceOf(InvalidCommand.class, parser.parse("bye later"));
    }

    @Test
    void parse_unknownCommand_returnsInvalidCommand() {
        assertInstanceOf(InvalidCommand.class, parser.parse("unknown something"));
    }

    @Test
    void parse_addMissingDescription_returnsInvalidCommand() {
        assertInstanceOf(InvalidCommand.class, parser.parse("add a/10"));
    }

    @Test
    void parse_addNonAsciiDescription_returnsInvalidCommand() {
        assertInstanceOf(InvalidCommand.class, parser.parse("add a/5 desc/午餐"));
    }
}
