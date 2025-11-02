# Project: orCASHbuddy

## Overview

orCASHbuddy is a Java 17 command-line application that helps students track expenses against a lightweight budget without the overhead of spreadsheets. It provides a fast way to log expenses, manage a simple budget, and gain quick insights into spending habits.

## Summary of Contributions

*   **Code contributed:** [Link to tP Code Dashboard](https://nus-cs2113-ay2526s1.github.io/tp-dashboard/?search=muadzyamani&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2025-09-19T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=muadzyamani&tabRepo=AY2526S1-CS2113-T11-2%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code~other&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

*   **Enhancements implemented:**
    *   **`mark` and `unmark` Feature Implementation:**
        *   **Functionality:** Developed the `mark` and `unmark` commands to flag expenses as paid or unpaid, providing visual feedback through `[X]` and `[ ]` indicators.
        *   **Contributions:**
            *   `Parser.java`: Implemented `parseMarkCommand` and `parseUnmarkCommand` to extract and validate expense indices from user input.
            *   `InputValidator.java`: Created `validateIndex` method to ensure indices are positive integers and within valid range.
            *   `MarkCommand.java` and `UnmarkCommand.java`: Implemented `execute` methods to orchestrate expense state changes, budget updates, and status alerts.
            *   `ExpenseManager.java`: Developed `markExpense` and `unmarkExpense` methods with budget recalculation logic, including `updateBudgetAfterMark`, `updateBudgetAfterUnmark`, and `recalculateRemainingBalance` helper methods.
            *   `Expense.java`: Implemented `mark()`, `unmark()` methods and `isMarked()` getter to maintain expense payment state.
            *   `Ui.java`: Contributed `showMarkedExpense`, `showUnmarkedExpense` for confirmation display and `showBudgetStatus` for contextual budget alerts.
        *   **Testing:** Authored comprehensive test cases covering valid marking/unmarking, invalid indices, empty lists, budget recalculation scenarios, and edge cases like marking already-marked expenses.

    *   **`find` Feature Implementation:**
        *   **Functionality:** Developed the search functionality enabling users to locate expenses by category or description.
        *   **Contributions:**
            *   `Parser.java`: Implemented `parseFindCommand` to handle both category (`cat/`) and description (`desc/`) search prefixes, with validation for missing criteria.
            *   `FindCommand.java`: Implemented the `execute` method to coordinate search operations and result display, supporting dual search modes.
            *   `ExpenseManager.java`: Developed `findExpensesByCategory` and `findExpensesByDescription` methods using linear search with case-insensitive substring matching.
            *   `Ui.java`: Contributed `showFoundExpenses` to format and display search results with match counts and contextual messaging for empty results.
        *   **Testing:** Created test suites covering category searches, description searches, case sensitivity handling, partial matches, and no-results scenarios.

    *   **Budget Status and Validation Infrastructure:**
        *   Contributed to `OrCashBuddyException.java` with factory methods `expenseIndexTooSmall()` and `expenseIndexOutOfRange()` for index validation errors.
        *   Enhanced `ExpenseManager.java` with `validateIndex` method to enforce index bounds checking across mark, unmark, and delete operations.
        *   Contributed to `determineBudgetStatus()` integration in command workflows to provide immediate budget feedback after state-changing operations.

*   **Contributions to the User Guide:**
    *   Wrote documentation for the `mark`, `unmark`, and `find` features with specifications and examples.
    *   Contributed to the FAQ section with questions addressing marking behavior, and unmarked expense budget impact.

*   **Contributions to the Developer Guide:**
    *   Authored the "Mark/Unmark Expense Feature" section including overview, sequence diagrams, control flow analysis and logging.
    *   Wrote the "Find Expense Feature" section including overview, sequence diagram, control flow, and design rationale.
    *   Created sequence diagrams (`mark-sequence.puml`, `find-sequence.puml`) illustrating interaction flows between components.

*   **Contributions to team-based tasks:**
    *   Coordinated testing for mark/unmark feature ensuring budget calculation accuracy across edge cases.
    *   Contributed to issue tracking and bug triaging for search and marking features.

*   **Review/mentoring contributions:**
    *   Reviewed and provided feedback on teammates' feature implementations, e.g., in pull request [#51](https://github.com/AY2526S1-CS2113-T11-2/tp/pull/51), where I offered suggestions on documentation standards for custom exceptions and ensuring code style uniformity.
    *   Actively participated in team discussions to align search functionality design with overall command syntax patterns and user experience goals.
