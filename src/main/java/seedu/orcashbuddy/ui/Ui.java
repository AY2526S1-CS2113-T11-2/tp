package seedu.orcashbuddy.ui;

import seedu.orcashbuddy.expense.Expense;

import java.util.ArrayList;

/**
 * Handles user interactions such as displaying messages.
 */
public class Ui {

    public void showWelcome() {
        System.out.println("Welcome to orCASHbuddy");
        showMenu();
    }

    /**
     * Prints the main menu of available commands.
     */
    public void showMenu(){
        System.out.println("Add an expense:                        add a/AMOUNT desc/DESCRIPTION [cat/CATEGORY]");
        System.out.println("Set a budget:                          setbudget a/AMOUNT");
        System.out.println("List all expenses & statistics:        list");
        System.out.println("Find expenses:                         find cat/CATEGORY or find desc/DESCRIPTION");
        System.out.println("Mark an expense as paid:               mark EXPENSE_INDEX");
        System.out.println("Mark an expense as unpaid:             unmark EXPENSE_INDEX");
        System.out.println("Delete an expense:                     delete EXPENSE_INDEX");
        System.out.println("Sort all expenses in descending order: sort");
        System.out.println("Exit the application:                  bye");
    }


    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Prints the confirmation for a newly added expense.
     */
    public void showNewExpense(Expense expense) {
        System.out.println("New Expense:");
        System.out.println(expense.formatForDisplay());
    }

    /**
     * Displays a confirmation message that an expense was deleted.
     *
     * @param expense The expense object that was deleted
     */
    public void showDeletedExpense(Expense expense) {
        System.out.println("Deleted Expense:");
        System.out.println(expense.formatForDisplay());
    }

    /**
     * Displays a confirmation message showing the newly set total budget.
     *
     * @param budget the new budget amount to display
     */
    public void showNewBudget(double budget) {
        System.out.println("Your total budget is now $" + String.format("%.2f", budget) + ".");
    }

    /**
     * Displays the total expenses.
     *
     * @param totalExpense The total amount of expenses.
     */
    private void showTotalExpenses(double totalExpense){
        System.out.println("Total expenses: $" + String.format("%.2f", totalExpense));
    }

    /**
     * Displays the budget that has been set.
     *
     * @param budget The budget amount.
     */
    public void showBudget(double budget){
        System.out.println("Budget set: $" + String.format("%.2f", budget));
    }

    /**
     * Displays the remaining balance after expenses.
     *
     * @param remainingBalance The remaining balance amount.
     */
    private void showRemainingBalance(double remainingBalance) {
        System.out.println("Remaining balance: $" + String.format("%.2f", remainingBalance));
    }

    /**
     * Displays the list of expenses.
     * If the list is empty, a message indicating no expenses is shown.
     *
     * @param expenses The list of expenses to display.
     */
    private void showListOfExpenses(ArrayList<Expense> expenses) {
        if (expenses.isEmpty()) {
            showListUsage();
            return;
        }
        System.out.println("Here is the list of expenses:");
        for (int i = 0; i < expenses.size(); i++) {
            System.out.println((i + 1) + ". " + expenses.get(i).formatForDisplay());
        }
    }

    /**
     * Displays total expenses, budget,
     * remaining balance, and the full list of expenses.
     *
     * @param totalExpense The total amount of expenses.
     * @param budget The budget amount.
     * @param remainingBalance The remaining balance after expenses.
     * @param expenses The list of all expenses.
     */
    public void showList(double totalExpense, double budget,
            double remainingBalance, ArrayList<Expense> expenses) {
        showBudget(budget);
        showTotalExpenses(totalExpense);
        showRemainingBalance(remainingBalance);
        showListOfExpenses(expenses);
    }

    /**
     * Displays a confirmation message that an expense was marked.
     *
     * @param expense The expense object that was marked
     */
    public void showMarkedExpense(Expense expense) {
        System.out.println("Marked Expense:");
        System.out.println(expense.formatForDisplay());
    }

    /**
     * Displays a confirmation message that an expense was unmarked.
     *
     * @param expense The expense object that was unmarked
     */
    public void showUnmarkedExpense(Expense expense) {
        System.out.println("Unmarked Expense:");
        System.out.println(expense.formatForDisplay());
    }

    /**
     * Prints the correct usage for the add command.
     */
    public void showAddUsage() {
        System.out.println("Invalid format. Use: add a/AMOUNT desc/DESCRIPTION [cat/CATEGORY]");
    }

    /**
     * Displays usage information for the delete command.
     * Format: delete EXPENSE_INDEX
     */
    public void showDeleteUsage(String errorMessage) {
        System.out.println(errorMessage);
        System.out.println("Invalid format. Use: delete EXPENSE_INDEX");
    }

    /**
     * Displays usage instructions for the {@code setbudget} command
     * when the user provides invalid input.
     * <p>
     * Correct format: {@code setbudget a/AMOUNT}
     */
    public void showSetBudgetUsage() {
        System.out.println("Invalid format. Use: setbudget a/AMOUNT");
    }

    /**
     * Displays usage information for the mark command.
     * Format: mark EXPENSE_INDEX
     *
     * @param errorMessage the specific error message to display
     */
    public void showMarkUsage(String errorMessage) {
        System.out.println(errorMessage);
        System.out.println("Invalid format. Use: mark EXPENSE_INDEX");
        System.out.println("Type list to view expense indices");
    }

    /**
     * Displays usage information for the unmark command.
     * Format: unmark EXPENSE_INDEX
     *
     * @param errorMessage the specific error message to display
     */
    public void showUnmarkUsage(String errorMessage) {
        System.out.println(errorMessage);
        System.out.println("Invalid format. Use: unmark EXPENSE_INDEX");
        System.out.println("Type list to view expense indices");
    }

    /**
     * Displays a message for unknown commands.
     */
    public void showUnknownCommand() {
        System.out.println("Unknown command. Type 'help' to see available commands.");
    }
    /**
     * Displays a message for empty list.
     */
    public void showListUsage() {
        System.out.println("No expenses added so far.");
    }

    /**
     * Displays a general error message.
     *
     * @param message the error message to display
     */
    public void showError(String message) {
        System.out.println("Error: " + message);
    }

    //@@author saheer17
    /**
     * Displays the list of expenses sorted by amount in descending order.
     * If the list is empty, displays a message indicating no expenses.
     *
     * @param sortedExpenses the list of expenses sorted from highest to lowest amount
     */
    public void showSortedList(ArrayList<Expense> sortedExpenses) {
        System.out.println("Here is the list of sorted expenses, starting with the highest amount:");
        for (int i = 0; i < sortedExpenses.size(); i++) {
            assert sortedExpenses.get(i) != null : "Expense in sorted list must not be null";
            System.out.println((i + 1) + ". " + sortedExpenses.get(i).formatForDisplay());
        }
    }

    //@@author muadzyamani
    /**
     * Displays the list of expenses found by the find command.
     * If no expenses are found, displays a message indicating no results.
     *
     * @param foundExpenses the list of expenses that match the search criteria
     * @param searchTerm the term that was searched for
     * @param searchType the type of search performed ("category" or "description")
     */
    public void showFoundExpenses(ArrayList<Expense> foundExpenses, String searchTerm, String searchType) {
        if (foundExpenses.isEmpty()) {
            System.out.println("No expenses found matching " + searchType + ": " + searchTerm);
            return;
        }

        System.out.println("Found " + foundExpenses.size() + " expense(s) matching " +
                searchType + ": " + searchTerm);
        for (int i = 0; i < foundExpenses.size(); i++) {
            System.out.println((i + 1) + ". " + foundExpenses.get(i).formatForDisplay());
        }
    }

    /**
     * Displays usage information for the find command.
     */
    public void showFindUsage() {
        System.out.println("Invalid format. Use: find cat/CATEGORY or find desc/DESCRIPTION");
    }
}
