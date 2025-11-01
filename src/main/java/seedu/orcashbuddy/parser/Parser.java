package seedu.orcashbuddy.parser;

import seedu.orcashbuddy.command.Command;
import seedu.orcashbuddy.command.AddCommand;
import seedu.orcashbuddy.command.FindCommand;
import seedu.orcashbuddy.command.SetBudgetCommand;
import seedu.orcashbuddy.command.DeleteCommand;
import seedu.orcashbuddy.command.MarkCommand;
import seedu.orcashbuddy.command.UnmarkCommand;
import seedu.orcashbuddy.command.ListCommand;
import seedu.orcashbuddy.command.HelpCommand;
import seedu.orcashbuddy.command.InvalidCommand;
import seedu.orcashbuddy.command.SortCommand;
import seedu.orcashbuddy.command.ByeCommand;
import seedu.orcashbuddy.command.EditCommand;
import seedu.orcashbuddy.exception.OrCashBuddyException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Parses raw user input into executable {@link Command} objects.
 * <p>
 * Flow:
 * <ol>
 *   <li>Split input into a command word and its argument string</li>
 *   <li>Dispatch to a specific {@code parseXxxCommand(...)} method</li>
 *   <li>Wrap parser/validation failures in {@link InvalidCommand}</li>
 * </ol>
 */
public class Parser {
    private static final Logger LOGGER = Logger.getLogger(Parser.class.getName());
    private static final int COMMAND_WORD_INDEX = 0;
    private static final int ARGUMENTS_INDEX = 1;
    private static final int MAX_SPLIT_PARTS = 2;

    // Command prefixes
    private static final String AMOUNT_PREFIX = "a/";
    private static final String DESCRIPTION_PREFIX = "desc/";
    private static final String CATEGORY_PREFIX = "cat/";
    private static final String INDEX_PREFIX = "id/";

    /**
     * Parses the user input and returns the corresponding {@link Command}.
     *
     * @param userInput the raw input string from the user
     * @return the {@link Command} object to be executed
     */
    public Command parse(String userInput) {
        assert userInput != null : "User input must not be null";

        String trimmed = userInput.trim();
        String[] words = trimmed.split("\\s+", MAX_SPLIT_PARTS);
        String commandWord = words[COMMAND_WORD_INDEX].toLowerCase();
        String arguments = (words.length > ARGUMENTS_INDEX) ? words[ARGUMENTS_INDEX] : "";

        LOGGER.fine(() -> "Parsing command: " + commandWord);

        try {
            switch (commandWord) {
            case "add":
                return parseAddCommand(arguments);
            case "setbudget":
                return parseSetBudgetCommand(arguments);
            case "delete":
                return parseDeleteCommand(arguments);
            case "mark":
                return parseMarkCommand(arguments);
            case "unmark":
                return parseUnmarkCommand(arguments);
            case "list":
                return new ListCommand();
            case "help":
                return new HelpCommand();
            case "sort":
                return parseSortCommand(arguments);
            case "find":
                return parseFindCommand(arguments);
            case "edit":
                return parseEditCommand(arguments);
            case "bye":
                return parseByeCommand(arguments);
            default:
                return new InvalidCommand();
            }
        } catch (OrCashBuddyException e) {
            LOGGER.log(Level.WARNING, "Error parsing command: " + e.getMessage(), e);
            return new InvalidCommand(e);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Unexpected error parsing command: " + e.getMessage(), e);
            return new InvalidCommand();
        }
    }

    // ========== Command Parsing Methods ==========

    //@@author limzerui
    /**
     * Parses the {@code add} command and creates an {@link AddCommand}.
     *
     * @param arguments the argument string after {@code add}
     * @return an {@link AddCommand}
     * @throws OrCashBuddyException if required fields are missing/invalid
     */
    private Command parseAddCommand(String arguments) throws OrCashBuddyException {
        ArgumentParser argParser = new ArgumentParser(arguments,
                AMOUNT_PREFIX, DESCRIPTION_PREFIX, CATEGORY_PREFIX);
        String amountStr = argParser.getValue(AMOUNT_PREFIX);
        String descStr = argParser.getValue(DESCRIPTION_PREFIX);
        String categoryStr = argParser.getOptionalValue(CATEGORY_PREFIX);

        double amount = InputValidator.validateAmount(amountStr, "add");
        String description = InputValidator.validateDescription(descStr, "add");
        String category = InputValidator.validateCategory(categoryStr, "add");

        return new AddCommand(amount, description, category);
    }

    /**
     * Parses the {@code bye} command and creates a {@link ByeCommand}.
     *
     * @param arguments the arguments after the command word (should be empty)
     * @return a {@link ByeCommand}
     * @throws OrCashBuddyException if unexpected arguments are provided
     */
    private Command parseByeCommand(String arguments) throws OrCashBuddyException {
        if (arguments != null && !arguments.isBlank()) {
            throw new OrCashBuddyException("'bye' command does not take any arguments");
        }
        return new ByeCommand();
    }

    //@@author aydrienlaw
    /**
     * Parses the {@code setbudget} command and creates a {@link SetBudgetCommand}.
     *
     * @param arguments the arguments after {@code setbudget}
     * @return a {@link SetBudgetCommand}
     * @throws OrCashBuddyException if the budget amount is invalid
     */
    private Command parseSetBudgetCommand(String arguments) throws OrCashBuddyException {
        ArgumentParser argParser = new ArgumentParser(arguments);
        String amountStr = argParser.getValue(AMOUNT_PREFIX);

        double budget = InputValidator.validateAmount(amountStr,"setbudget");
        return new SetBudgetCommand(budget);
    }

    //@@author saheer17
    /**
     * Parses the {@code delete} command and creates a {@link DeleteCommand}.
     *
     * @param arguments the 1-based index of the expense to delete
     * @return a {@link DeleteCommand}
     * @throws OrCashBuddyException if the index is missing or invalid
     */
    private Command parseDeleteCommand(String arguments) throws OrCashBuddyException {
        int index = InputValidator.validateIndex(arguments, "delete");
        return new DeleteCommand(index);
    }

    //@@author muadzyamani
    /**
     * Parses the {@code mark} command and creates a {@link MarkCommand}.
     *
     * @param arguments the 1-based index of the expense to mark
     * @return a {@link MarkCommand}
     * @throws OrCashBuddyException if the index is missing or invalid
     */
    private Command parseMarkCommand(String arguments) throws OrCashBuddyException {
        int index = InputValidator.validateIndex(arguments, "mark");
        return new MarkCommand(index);
    }

    /**
     * Parses the {@code unmark} command and creates an {@link UnmarkCommand}.
     *
     * @param arguments the 1-based index of the expense to unmark
     * @return an {@link UnmarkCommand}
     * @throws OrCashBuddyException if the index is missing or invalid
     */
    private Command parseUnmarkCommand(String arguments) throws OrCashBuddyException {
        int index = InputValidator.validateIndex(arguments, "unmark");
        return new UnmarkCommand(index);
    }

    /**
     * Parses the {@code find} command and creates a {@link FindCommand}.
     * The user may search by {@code cat/} or {@code desc/}.
     *
     * @param arguments the argument string after {@code find}
     * @return a {@link FindCommand} configured for category or description search
     * @throws OrCashBuddyException if neither category nor description is provided
     */
    private Command parseFindCommand(String arguments) throws OrCashBuddyException {
        ArgumentParser argParser = new ArgumentParser(arguments);

        String category = argParser.getOptionalValue(CATEGORY_PREFIX);
        if (category != null && !category.trim().isEmpty()) {
            return new FindCommand("category", category.trim());
        }

        String description = argParser.getOptionalValue(DESCRIPTION_PREFIX);
        if (description != null && !description.trim().isEmpty()) {
            return new FindCommand("description", description.trim());
        }

        throw new OrCashBuddyException("Missing search criteria for 'find' command");
    }

    //@@author gumingyoujia
    /**
     * Parses the {@code edit} command and creates an {@link EditCommand}.
     * Supports partial edits of amount, description, and/or category.
     *
     * @param arguments the argument string after {@code edit}
     * @return an {@link EditCommand} with the target index and updated fields
     * @throws OrCashBuddyException if the index is invalid or no such expense exists
     */
    private Command parseEditCommand(String arguments) throws OrCashBuddyException {
        ArgumentParser argParser = new ArgumentParser(arguments);
        String indexString = argParser.getValue(INDEX_PREFIX);
        int index = InputValidator.validateIndex(indexString, "edit");

        String amountStr = argParser.getOptionalValue(AMOUNT_PREFIX);
        String descStr = argParser.getOptionalValue(DESCRIPTION_PREFIX);
        String categoryStr = argParser.getOptionalValue(CATEGORY_PREFIX);

        Double amount = (amountStr==null) ? null : InputValidator.validateAmount(amountStr,"edit");
        String description = (descStr==null) ? null : InputValidator.validateDescription(descStr, "edit");
        String category = (categoryStr==null) ? null : InputValidator.validateCategory(categoryStr, "edit");

        return new EditCommand(index, amount, description, category);
    }

    //@@author saheer17
    private Command parseSortCommand(String arguments) throws OrCashBuddyException {
        if (arguments != null && !arguments.isBlank()) {
            throw new OrCashBuddyException("'sort' command does not take any arguments");
        }
        return new SortCommand();
    }
}
