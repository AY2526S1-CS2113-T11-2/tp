# orCASHbuddy User Guide

Welcome to the user guide for orCASHbuddy, a lightweight command-line assistant for tracking and managing expenses.

orCASHbuddy is designed for Residential College 4 (RC4) Interest Group treasurers who need a quick and reliable way to record spending, set budgets, and monitor group finances.

This document explains how to set up the application, describes every available command, and provides quick references for day-to-day use.

---

## Table of Contents
- [Quick Start](#quick-start)
- [Using This Guide](#using-this-guide)
- [Features](#features)
    - [Viewing Help: `help`](#viewing-help-help)
    - [Adding an Expense: `add`](#adding-an-expense-add)
    - [Setting a Budget: `setbudget`](#setting-a-budget-setbudget)
    - [Budget Usage Alerts](#budget-usage-alerts)
    - [Listing Expenses and Summary: `list`](#listing-expenses-and-summary-list)
    - [Editing an Expense: `edit`](#editing-an-expense-edit)
    - [Marking an Expense as Paid: `mark`](#marking-an-expense-as-paid-mark)
    - [Unmarking an Expense: `unmark`](#unmarking-an-expense-unmark)
    - [Deleting an Expense: `delete`](#deleting-an-expense-delete)
    - [Finding an Expense: `find`](#finding-an-expense-find)
    - [Sorting Expenses by Amount: `sort`](#sorting-expenses-by-amount-sort)
    - [Exiting the Application: `bye`](#exiting-the-application-bye)
    - [Saving and Loading Data](#saving-and-loading-data)
- [FAQ](#faq)
- [Command Summary](#command-summary)

---

## Quick Start

1. **Install Java 17.** orCASHbuddy requires Java 17 or later. You can verify your version by running `java -version` in your terminal.
2. **Download the application.** Grab the latest `orcashbuddy.jar` from your course release or the project's GitHub Releases page and place it in a convenient folder.
3. **Launch the program.** Open a terminal in the folder containing the JAR and run:
   ```bash
   java -jar orcashbuddy.jar
   ```
   Once the program starts, you’ll see a welcome message and the command menu in your terminal window, as shown below.
    ```
   Welcome to orCASHbuddy
   ---------------------------------------------------------------
   Add an expense:                        add a/AMOUNT desc/DESCRIPTION [cat/CATEGORY]
   Set a budget:                          setbudget a/AMOUNT
   List all expenses & statistics:        list
   Find expenses:                         find cat/CATEGORY or find desc/DESCRIPTION
   Mark an expense as paid:               mark EXPENSE_INDEX
   Mark an expense as unpaid:             unmark EXPENSE_INDEX
   Delete an expense:                     delete EXPENSE_INDEX
   Edit an expense:                       edit id/INDEX [a/AMOUNT] [desc/DESCRIPTION] [cat/CATEGORY]
   Sort all expenses in descending order: sort
   Exit the application:                  bye
   ---------------------------------------------------------------
   ```

> **Note:** The application automatically saves your data to a `data` folder. Your expenses and budget will persist between sessions.

---

## Using This Guide

- **Command keywords** (e.g., `add`, `list`) are **case-insensitive**.
- **Placeholders** in `UPPER_SNAKE_CASE` are values you should replace with your own data.
- **Optional parameters** are shown in brackets like `[cat/CATEGORY]`. Do not type the brackets themselves.
- **Amounts** must be positive decimal numbers between $0.01 and $1,000,000,000,000 (e.g., `12.50`). Invalid inputs will display an informative error message.
- **Expense indices** are **1-based** and correspond to the numbering shown by the `list` command.
- **Text fields** (descriptions, categories, search terms) accept **ASCII characters only**. Non-ASCII input such as Chinese or Arabic text is rejected.

---

## Features

### Viewing Help: `help`

Displays all available commands and their formats in the terminal, providing a quick reference without leaving the application.

**Format:** `help`

**Example:**
```
help
```

**Expected output:**
```
---------------------------------------------------------------
Add an expense:                        add a/AMOUNT desc/DESCRIPTION [cat/CATEGORY]
Set a budget:                          setbudget a/AMOUNT
List all expenses & statistics:        list
Find expenses:                         find cat/CATEGORY or find desc/DESCRIPTION
Mark an expense as paid:               mark EXPENSE_INDEX
Mark an expense as unpaid:             unmark EXPENSE_INDEX
Delete an expense:                     delete EXPENSE_INDEX
Edit an expense:                       edit id/INDEX [a/AMOUNT] [desc/DESCRIPTION] [cat/CATEGORY]
Sort all expenses in descending order: sort
Exit the application:                  bye
---------------------------------------------------------------
```

**Notes:**
- This command does not accept any arguments and will reject input with extra arguments
- Use this whenever you need a quick reminder of supported commands
- The help menu is also displayed when you first launch orCASHbuddy

---

<!-- @@author limzerui -->
### Adding an Expense: `add`

Creates a new expense entry with an amount, description, and optional category.

**Format:** `add a/AMOUNT desc/DESCRIPTION [cat/CATEGORY]`

**Parameters:**
- `AMOUNT` — Positive number between $0.01 and $1,000,000,000,000 with up to two decimal places (e.g., `12.50`). Any digits beyond two decimal places will be ignored.
- `DESCRIPTION` — Short sentence describing the expense using ASCII characters only.
- `CATEGORY` (optional) — Tag to group similar expenses. It must start with a letter and can include ASCII letters, numbers, spaces, or hyphens (maximum 20 characters). If omitted, the category defaults to `Uncategorized`.

**Example:**
```
add a/32.90 desc/event poster cat/publicity
```

**Expected output:**
```
---------------------------------------------------------------
New Expense:
[ ] [publicity] event poster - $32.90
---------------------------------------------------------------
```

**Notes:**
- The newly added expense appears as `[ ] [CATEGORY] DESCRIPTION - $AMOUNT`
- The `[ ]` will change to `[X]` once the expense is marked as paid
- New expenses are added to the end of your list

---

<!-- @@author aydrienlaw -->
### Setting a Budget: `setbudget`

Defines your total spending budget for expense tracking and alerts.

**Format:** `setbudget a/AMOUNT`

**Parameters:**
- `AMOUNT` — Your total budget as a positive number between $0.01 and $1,000,000,000,000 with up to two decimal places. Any digits beyond two decimal places will be ignored.

**Example:**
```
setbudget a/200
```

**Expected output:**
```
---------------------------------------------------------------
Your total budget is now $200.00.
---------------------------------------------------------------
```
**Notes:**
- Setting a new budget overwrites the previous value
- Only **marked** expenses count toward budget usage
- The remaining balance updates automatically when expenses are marked, unmarked, edited, or deleted

---

## Budget Usage Alerts

orCASHbuddy provides real-time visual alerts to help you monitor your spending against your budget. These alerts appear automatically whenever a command affects your total expenses or budget amount.

### When Alerts Appear

Budget usage alerts are triggered by commands that modify your financial status:

**Commands that trigger alerts:**
- `setbudget` — When you set or update your budget
- `mark` — When you mark an expense as paid (increases total expenses)
- `unmark` — When you unmark an expense (decreases total expenses)
- `edit` — When you edit a marked expense's amount (changes total expenses)
- `delete` — When you delete a marked expense (decreases total expenses)
- `list` — Always displays the current budget status

### Alert Format

The alert consists of three components:

1. **Spending Summary**
   ```
   Spent: $25.00 / $200.00
   ```
   Shows your total marked expenses versus your budget.

2. **Progress Bar**
   ```
   Budget Used: [========|---------------------]  12.50%
   ```
   Visual representation of budget usage, where the `|` marker indicates your current spending level.

3. **Balance Information**
   ```
   (Remaining: $175.00)
   ```
   Shows how much budget remains (or how much you're over if negative).

### Color-Coded Warnings

The progress bar changes color based on your spending level to provide quick visual feedback:

- <span style="color: green;">**Green**</span> (Below 75%): Your spending is under control
- <span style="color: #DAA520;">**Yellow**</span>: Warning that you're approaching your budget limit
- <span style="color: Red;">**red**</span> (Over 100%): Alert that you've exceeded your budget

> **Note:** The colors appear automatically in your terminal using ANSI color codes. The examples below are shown in plain text for clarity.

### Examples

**After marking an expense:**
```
---------------------------------------------------------------
Marked Expense:
[X] [publicity] event poster - $25.00
---------------------------------------------------------------
BUDGET STATUS
Spent: $25.00 / $200.00
Budget Used: [====|-------------------------]  12.50%  (Remaining: $175.00)
---------------------------------------------------------------
```

**After editing a marked expense to a higher amount:**
```
---------------------------------------------------------------
Edited Expense:
[X] [publicity] event poster - $169.90
---------------------------------------------------------------
BUDGET STATUS
Spent: $169.90 / $200.00
Budget Used: [=========================|----]  84.95%  (Remaining: $30.10)
---------------------------------------------------------------
```

**After exceeding your budget:**
```
---------------------------------------------------------------
BUDGET STATUS
Spent: $210.00 / $200.00
Budget Used: [==============================|]  105.00%  (Over by: $10.00)
---------------------------------------------------------------
```

### Notes

- Alerts only appear when you have a budget set (use `setbudget` to define one)
- If no budget is set, you'll see `[no budget set]` instead of the progress bar
- Only **marked** expenses count toward your total expenses
- The alert automatically updates to reflect your current financial status
- The alert displays "Over by" amount when remaining balance is negative, and "Remaining" amount otherwise.

---

<!-- @@author gumingyoujia -->
### Listing Expenses and Summary: `list`

Shows your current budget, total paid expenses, remaining balance, and all recorded expense in the order added.

**Format:** `list`

**Example:**
```
list
```

**Expected output (with expenses):**
```
---------------------------------------------------------------
BUDGET STATUS
Spent: $25.00 / $200.00
Budget Used: [====|-------------------------] 12.50%  (Remaining: $175.00)

Here is your list of expenses:
1. [X] [publicity] event poster - $25.00
---------------------------------------------------------------
```

**Expected output (no expenses):**
```
---------------------------------------------------------------
BUDGET STATUS
Spent: $0.00 / $200.00
Budget Used: [|-----------------------------]  0.00%  (Remaining: $200.00)

No expenses added so far.
Try: add a/AMOUNT desc/DESCRIPTION [cat/CATEGORY]
---------------------------------------------------------------
```

**Expected output (no budget set):**
```
---------------------------------------------------------------
BUDGET STATUS
[no budget set]

Here is your list of expenses:
1. [ ] [publicity] event poster - $25.00
---------------------------------------------------------------
```

**Notes:**
- This command does not accept any arguments and will reject input with extra arguments
- If you have no expenses yet, orCASHbuddy suggests adding one with the `add` command
- The indices shown (1, 2, 3...) are used for `mark`, `unmark`, `edit`, and `delete` commands
- Marked expenses display `[X]`, unmarked expenses display `[ ]`

---

<!-- @@author gumingyoujia -->
### Editing an Expense: `edit`

Modifies the amount, description, and/or category of an existing expense. Updates the expense list and recalculates the remaining budget.

**Format:** `edit id/INDEX [a/AMOUNT] [desc/DESCRIPTION] [cat/CATEGORY]`

**Parameters:**
- `INDEX` — Positive integer index of the expense in the list (use `list` to see indices).
- `AMOUNT` (optional) — Positive number between $0.01 and $1,000,000,000,000 with up to two decimal places (e.g., `12.50`). Any digits beyond two decimal places will be ignored.
- `DESCRIPTION` (optional) — Short sentence describing the expense using ASCII characters only.
- `CATEGORY` (optional) — It must start with a letter and can include ASCII letters, numbers, spaces, or hyphens (maximum 20 characters).

**Notes:**
- You can change multiple attributes of the expense at once.
- It triggers alerts if the edited amount is higher than original amount when spending approaches or exceeds the budget.

**Examples:**
```
edit id/1 a/25
```

**Expected output:**
```
---------------------------------------------------------------
Edited Expense:
[ ] [publicity] event poster - $25.00
---------------------------------------------------------------
```

**Notes:**
- You must provide at least the `id/INDEX` parameter
- If you don't provide optional parameters, those fields remain unchanged
- Use `list` first to verify the correct index

---

<!-- @@author muadzyamani -->
### Marking an Expense as Paid: `mark`

Flags an expense as paid, adding its amount to your total expenses.

**Format:** `mark INDEX`

**Parameters:**
- `INDEX` — The expense number shown in the `list` command (must be a positive integer).

**Example:**
```
mark 1
```

**Expected output:**
```
---------------------------------------------------------------
Marked Expense:
[X] [publicity] event poster - $25.00
---------------------------------------------------------------
```

**Notes:**
- Use `list` first to see the correct index
- Only marked expenses count toward your budget usage
- Marked expenses are flagged with `[X]`

---

### Unmarking an Expense: `unmark`

Reverts a previously marked expense to unpaid,  removing its amount from your total expenses.

**Format:** `unmark INDEX`

**Parameters:**
- `INDEX` — The expense number shown in the `list` command (must be a positive integer).

**Example:**
```
unmark 1
```

**Expected output:**
```
---------------------------------------------------------------
Unmarked Expense:
[ ] [publicity] event poster - $25.00
---------------------------------------------------------------
```

**Notes:**
- Use `list` first to verify the correct index
- Unmarking increases your remaining balance by the expense amount

---

<!-- @@author saheer17 -->
### Deleting an Expense: `delete`

Removes an expense permanently from your list.

**Format:** `delete INDEX`

**Parameters:**
- `INDEX` — The expense number shown in the `list` command (must be a positive integer).

**Example:**
```
delete 1
```

**Expected output (unmarked expense):**
```
---------------------------------------------------------------
Deleted Expense:
[ ] [publicity] poster printing - $25.00
---------------------------------------------------------------
```

**Notes:**
- There is no undo for deletion
- Use `list` first to verify the correct index
- After deletion, all expenses below the deleted one shift up by one position
- If performing multiple deletions, check indices with `list` after each deletion 
- After deletion, use `list` to confirm the remaining expenses and updated totals

---

<!-- @@author muadzyamani -->
### Finding an Expense: `find`

Searches for expenses by category or description keyword, displaying all matching results.

**Format:** `find cat/CATEGORY` or `find desc/DESCRIPTION`

**Parameters:**
- `CATEGORY` — Category keyword to search for (case-insensitive, ASCII characters only).
- `DESCRIPTION` — Description keyword to search for (case-insensitive, ASCII characters only).

**Examples:**
```
find desc/poster
```

**Expected output (matches found):**
```
---------------------------------------------------------------
Found 1 expense(s) matching category: publicity
1. [ ] [publicity] event poster - $25.00
---------------------------------------------------------------
```

**Expected output (no matches):**
```
---------------------------------------------------------------
No expenses found matching description: poster
---------------------------------------------------------------
```

If no matches are found, orCASHbuddy displays "No expenses found matching [category/description]: [search term]".

**Notes:**
- Search is case-insensitive (searching for "Food" will find "food", "FOOD", etc.)
- Search looks for partial matches (searching "post" will find "poster")
- The numbering in search results (1, 2, 3...) is **display-only** and does not correspond to the indices used by other commands
- Always use `list` to verify the correct index before modifying expenses

---

<!-- @@author saheer17 -->
### Sorting Expenses by Amount: `sort`

Displays all expenses in descending order of amount (largest first). Sorting does not change the original order used by other commands.

**Format:** `sort`

**Example:**
```
sort
```

**Expected output (with expenses):**
```
---------------------------------------------------------------
Here is your list of sorted expenses, starting with the highest amount:
1. [X] [publicity] event poster - $25.00
2. [ ] [publicity] poster printing - $10.00
3. [ ] [Uncategorized] venue booking - $4.50
---------------------------------------------------------------
```

**Expected output (no expenses):**
```
---------------------------------------------------------------
No expenses added so far.
Try: add a/AMOUNT desc/DESCRIPTION [cat/CATEGORY]
---------------------------------------------------------------
```

**Notes:**
- This command does not accept any arguments and will reject input with extra arguments
- The sorting is temporary and does not change the order in `list`
- The numbering shown (1, 2, 3...) is **display-only** and does not correspond to the indices used by `mark`, `unmark`, `edit`, or `delete`
- Always use `list` to verify the correct index before modifying expenses
- Useful for identifying your largest expenses at a glance

---

<!-- @@author limzerui -->
### Exiting the Application: `bye`

Closes orCASHbuddy gracefully.

**Format:** `bye`

**Example:**
```
bye
```

**Expected output:**
```
---------------------------------------------------------------
Bye. Hope to see you again soon!
---------------------------------------------------------------
```

**Notes:**
- This command does not accept any arguments and will reject input with extra arguments
- You can also terminate the program by closing the terminal window, but using `bye` ensures the farewell message is displayed
- All data is saved automatically before exiting

---

<!-- @@author saheer17 -->
### Saving and Loading Data

**Automatic Saving:**
- The application saves your data automatically after every command
- Data is stored in a `data` folder as `appdata.ser`
- You never need to manually save

**Automatic Loading:**
- When you start orCASHbuddy, it automatically loads your previous session
- If no saved data exists, you start with an empty expense list and zero budget

**Data Location:**
- The `data` folder is created in the same directory as `orCASHbuddy.main.jar`
- Do not manually edit or delete `appdata.ser` unless you want to reset all data

**What's Saved:**
- All expenses (amount, description, category, marked/unmarked status)
- Your current budget
- Total expenses and remaining balance

**Notes:**
- If the save file is corrupted or incompatible, you'll start with a fresh session
- The application will notify you if there are any issues loading data

---

## FAQ

**Q: What happens if I mistype a command?**

**A:** orCASHbuddy prints a helpful error message explaining what went wrong (e.g., "Missing prefix 'a/'" or "Amount must be positive"). Additionally, a usage hint of the attempted command will be displayed.

---

**Q: Why don't the numbers in `find` results match my list indices?**

**A:** The `find` and `sort` commands renumber results (1, 2, 3...) for easier reading. These numbers are **display-only** and do not correspond to the actual indices used by `mark`, `unmark`, `edit`, or `delete`. Always use `list` first to verify the correct index before modifying expenses.

---

**Q: Can I have multiple expenses with the same description or category?**

**A:** Yes! orCASHbuddy supports duplicate descriptions and categories. Each expense is tracked independently by its position in the list.

---

**Q: What happens if I mark an expense that's already marked?**

**A:** You'll receive a message indicating the expense is already in that state ("This expense is already marked" or "This expense is already unmarked"). No changes are made to the expense or your spending.

---

**Q: Does deleting an expense change the indices of other expenses?**

**A:** Yes. When you delete an expense, all expenses below it shift up by one position. Always check the current indices with `list` before performing multiple deletions.

---

**Q: What if my remaining balance is negative?**

**A:** orCASHbuddy allows negative balances and will display them as such (e.g., "Remaining balance: $-45.30"). You'll also see a red alert message indicating you've exceeded your budget, along with how much you're over.

---

**Q: Can I change my budget after setting it?**

**A:** Yes! Simply use the `setbudget` command again with a new amount. The new budget overwrites the old one, and your remaining balance is recalculated based on your current marked expenses.

---

**Q: Where is my data stored?**

**A:** Your data is saved in a file called `appdata.ser` inside a `data` folder, located in the same directory as `orCASHbuddy.main.jar`. This file is automatically created when you first add an expense or set a budget.

---

**Q: Do unmarked expenses count toward my budget?**

**A:** No. Only **marked** expenses (shown with `[X]`) count toward your total expenses and affect your remaining balance. Unmarked expenses (shown with `[ ]`) are tracked but don't impact your budget calculations.

---

## Command Summary

| Action | Format | Example                                 |
|--------|--------|-----------------------------------------|
| View help | `help` | `help`                                  |
| Add expense | `add a/AMOUNT desc/DESCRIPTION [cat/CATEGORY]` | `add a/32.90 desc/poster cat/publicity` |
| Set budget | `setbudget a/AMOUNT` | `setbudget a/200`                       |
| List summary | `list` | `list`                                  |
| Edit expense | `edit id/INDEX [a/AMOUNT] [desc/DESCRIPTION] [cat/CATEGORY]` | `edit id/1 a/25`                        |
| Mark expense | `mark INDEX` | `mark 1`                                |
| Unmark expense | `unmark INDEX` | `unmark 1`                              |
| Delete expense | `delete INDEX` | `delete 1`                              |
| Find expense | `find cat/CATEGORY` or `find desc/DESCRIPTION` | `find desc/poster`                      |
| Sort expenses | `sort` | `sort`                                  |
| Exit | `bye` | `bye`                                   |

---

**For detailed explanations, parameter notes, and expected outcomes, refer to the [Features](#features) section above.**

---

*Last updated: v2.1*
