# Project: orCASHbuddy

## Overview

**orCASHbuddy** is a Java 17 CLI app built for **RC4 Interest Group treasurers** to manage budgets efficiently. It eliminates the need for spreadsheets by offering intuitive commands to track expenses, set budgets, and monitor financial health.

## Summary of Contributions

### Code Contributed
[View full breakdown on tP Code Dashboard](https://nus-cs2113-ay2526s1.github.io/tp-dashboard/?search=aydrienlaw&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2025-09-19T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=aydrienlaw&tabRepo=AY2526S1-CS2113-T11-2%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

---

### Enhancements Implemented

#### 1. `setbudget` Feature
- **Functionality:** Allows users to set an overall spending budget, automatically updating balance and budget status in real-time.
- **Justification:** Establishes a clear spending limit for RC4 treasurers to manage finances responsibly.
- **Main Contributions:**
    - Implemented `SetBudgetCommand.java` for command logic and validation.
    - Integrated with `ExpenseManager.java` to store and update the budget state.
    - Enhanced `Ui.java` to display budget confirmation and usage summary.

#### 2. Budget Usage Alert
- **Functionality:** Provides visual and textual alerts when spending approaches or exceeds the set budget threshold.
- **Justification:** Encourages fiscal awareness and prevents budget overruns.
- **Main Contributions:**
    - Added validation logic in `ExpenseManager.java` to trigger alerts.
    - Updated `Ui.java` with progress bar and warning indicators.
    - Coordinated alert updates across `add`, `edit`, `delete`, `mark`, and `unmark` commands.

---

### Documentation Contributions

#### User Guide
- Authored detailed documentation for:
    - `Set Budget` feature section.
    - `Budget Usage Alert` section.
- Refactored entire User Guide for tone, formatting, and visual consistency.

#### Developer Guide
- Wrote and refined component sections:
    - **Model Component**
    - **Logic Component**
    - **Set Budget Feature Implementation**
    - **Appendix E: Manual Testing**
- Refactored and polished Developer Guide formatting for visual clarity and structural consistency.

---

### Team-Based Contributions
- Conducted **major refactoring before v1.0 release** to improve code maintainability.
    - Applied **Separation of Concerns (SoC)** and **SLAP principles**.
    - Refactored monolithic “God classes” into modular packages:
        - `ui`, `parser`, `commands`, `exception`, `storage`
- Enforced consistent naming conventions and best practices throughout the project.
- Maintained issue tracker, git conventions, and project documentation standards.

---

### Review / Mentoring Contributions
- Reviewed and provided targeted feedback on teammates’ PRs (e.g., [PR #86](https://github.com/AY2526S1-CS2113-T11-2/tp/pull/86)).
    - Advised on documentation standards for custom exceptions.
    - Ensured code style uniformity and adherence to Java coding conventions.
- Participated in feature design discussions to ensure cohesive command syntax and user experience.
