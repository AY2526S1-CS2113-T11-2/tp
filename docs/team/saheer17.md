# Project: orCASHbuddy

## Overview

orCASHbuddy is a Java 17 command-line application that helps students track expenses against a lightweight budget without the overhead of spreadsheets. It provides a fast way to log expenses, manage a simple budget, and gain quick insights into spending habits.

## Summary of Contributions

*   **Code contributed:** [Link to tP Code Dashboard](https://nus-cs2113-ay2526s1.github.io/tp-dashboard/?search=saheer17&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2025-09-19T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&filteredFileName=)

*   **Contributions to the UG:** Documented sort and delete command usage and explained the storage system.

*   **Contributions to the DG:** 
    *   Documented the sort and delete functionalities with `delete-sequence.puml` and `sort-sequence.puml` sequence diagrams. 
    *   Wrote the Storage component overview and implementation details, supported by `storage-component-class.puml` and `storage-manager-sequence.puml` diagrams.

*   **Enhancements implemented:**
    *   **`delete` Feature Implementation:**
        *   **Functionality:** Enables users to remove expenses by index, maintaining an accurate list.
        *   **Class Contributions:**
            *   `Parser.java`: Implemented the `parseDeleteCommand` method to interpret user input for the delete command, validate index and create a DeleteCommand object.
            *   `InputValidator.java`: Added the `validateIndex` method to validate range and format of index.
            *   `DeleteCommand.java`: Developed the `execute` method to remove the specified expense from the `ExpenseManager`, display confirmation via the `Ui` and update the budget status accordingly.
            *   `ExpenseManager.java`: Added `deleteExpense` with index checks, budget updates, and assertions for consistency.
            *   `Ui.java`: Contributed to displaying user feedback for deleted expense via `showDeletedExpense`.
        *   **Exceptions Added:** Added new `OrCashBuddyException` cases (`missingExpenseIndex`, `invalidExpenseIndex`).
        *   **Testing:** Covered valid, invalid, and marked-expense deletion scenarios.
        
    *   **`sort` Feature Implementation:**
        *   **Functionality:** Sorts expenses in descending order by amount to help users identify major spending.
        *   **Class Contributions:**
            *   `Parser.java`: Added `parseSortCommand()` to validate input and instantiate `SortCommand`.
            *   `SortCommand.java`: Implemented the `execute` method to call `ExpenseManager.sortExpenses()`, handle empty-list cases gracefully and delegate display responsibilities to the `Ui`.
            *   `ExpenseManager.java`: Implemented the `sortExpenses` method, which generates a new list sorted by expense amount in descending order, maintaining immutability of the original list.
            *   `Ui.java`: Implemented `showSortedExpenseList()` for clear display.
        *   **Testing:** Implemented tests to confirm correct sorting behavior, empty-list handling and UI output formatting.
        
    *   **`Storage` Feature Implementation:**
        *   **Functionality:** Provides persistent saving/loading of expenses and budgets.
        *   **Class Contributions:**
            *   `StorageManager.java`: Implemented file handling for saving/loading serialized `ExpenseManager` objects with logging and exception handling.
            *   `ExpenseManager.java`: Made Serializable and ensured stable internal state.
            *   `Ui.java`: Enhanced with `showError()` for file-related issues.
        *   **Exceptions Added:** Implemented user-facing error handling for `IOException` and `SecurityException` cases, ensuring resilience and recovery.
        *   **Testing:** Verified that expenses persist correctly across application runs, including scenarios for missing files.
        
*   **Contributions to team-based tasks:**
    *   Created and formatted a shared Excel sheet to collaboratively manage and track User Stories by category.
    *   Maintained the issue tracker and created product demonstration video.

*   **Review/mentoring contributions:**
    *   Reviewed and mentored teammates on feature design and implementation, e.g. pull request [#55](https://github.com/AY2526S1-CS2113-T11-2/tp/pull/55), where I identified edge cases and suggested refactoring improvements.
    *   Provided consistent support during team meetings and online discussions to clarify requirements and maintain shared understanding of objectives.
    *   Provided constructive suggestions to improve code refactorability and proposed solutions for identified bugs.


