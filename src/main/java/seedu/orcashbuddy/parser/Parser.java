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
import seedu.orcashbuddy.exception.OrCashBuddyException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Parses user input and creates the appropriate Command objects.
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

    /**
     * Parses the user input and returns the corresponding Command object.
     *
     * @param userInput the raw input string from the user
     * @return the Command object to be executed
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
                return new SortCommand();
            case "find":
                return parseFindCommand(arguments);
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
     * Parses the add command and creates an AddCommand object.
     *
     * @param arguments the arguments after the command word
     * @return an AddCommand object
     * @throws OrCashBuddyException if the input is invalid
     */
    private Command parseAddCommand(String arguments) throws OrCashBuddyException {
        ArgumentParser argParser = new ArgumentParser(arguments);
        String amountStr = argParser.getValue(AMOUNT_PREFIX);
        String descStr = argParser.getValue(DESCRIPTION_PREFIX);
        String categoryStr = argParser.getOptionalValue(CATEGORY_PREFIX);

        double amount = InputValidator.validateAmount(amountStr);
        String description = InputValidator.validateDescription(descStr);
        String category = InputValidator.validateCategory(categoryStr);

        return new AddCommand(amount, description, category);
    }

    /**
     * Parses the bye command and creates a ByeCommand object.
     *
     * @param arguments the arguments after the command word
     * @return a ByeCommand object
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
     * Parses the setbudget command and creates a SetBudgetCommand object.
     *
     * @param arguments the arguments after the command word
     * @return a SetBudgetCommand object
     * @throws OrCashBuddyException if the input is invalid
     */
    private Command parseSetBudgetCommand(String arguments) throws OrCashBuddyException {
        ArgumentParser argParser = new ArgumentParser(arguments);
        String amountStr = argParser.getValue(AMOUNT_PREFIX);

        double budget = InputValidator.validateAmount(amountStr);
        return new SetBudgetCommand(budget);
    }

    //@@author saheer17
    private Command parseDeleteCommand(String arguments) throws OrCashBuddyException {
        int index = InputValidator.validateIndex(arguments, "delete");
        return new DeleteCommand(index);
    }

    //@author muadzyamani
    private Command parseMarkCommand(String arguments) throws OrCashBuddyException {
        int index = InputValidator.validateIndex(arguments, "mark");
        return new MarkCommand(index);
    }

    private Command parseUnmarkCommand(String arguments) throws OrCashBuddyException {
        int index = InputValidator.validateIndex(arguments, "unmark");
        return new UnmarkCommand(index);
    }

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
}
