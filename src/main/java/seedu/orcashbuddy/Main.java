package seedu.orcashbuddy;

import seedu.orcashbuddy.command.Command;
import seedu.orcashbuddy.exception.OrCashBuddyException;
import seedu.orcashbuddy.parser.Parser;
import seedu.orcashbuddy.storage.ExpenseManager;
import seedu.orcashbuddy.storage.StorageManager;
import seedu.orcashbuddy.ui.Ui;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Entry point and main loop for the orCASHbuddy application.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Load saved expenses/budget from disk</li>
 *   <li>Read user commands in a loop</li>
 *   <li>Parse and execute each command</li>
 *   <li>Persist changes after each successful command</li>
 *   <li>Gracefully shut down on {@code bye}</li>
 * </ul>
 */
public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private final Ui ui;
    private final ExpenseManager expenseManager;
    private final Parser parser;

    // Configure root logger level once.
    static {
        configureConsoleEncoding();
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.OFF);
        for (Handler handler : rootLogger.getHandlers()) {
            handler.setLevel(Level.INFO);
        }
    }

    /**
     * Constructs the main application object.
     * Loads persisted data (if any) via {@link StorageManager}.
     */
    public Main() {
        this.ui = new Ui();
        this.expenseManager = StorageManager.loadExpenseManager(ui);
        this.parser = new Parser();
    }

    /**
     * Runs the interactive command loop:
     * shows the welcome banner, reads commands,
     * executes them, and stops when a command indicates exit.
     */
    public void run() {
        ui.showWelcome();

        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8.name());
        boolean shouldExit = false;
        while (!shouldExit) {
            String input = readInput(scanner);
            if (input == null) {
                break;
            }

            shouldExit = executeCommand(input);
        }
    }

    /**
     * Reads user input from {@link Scanner}.
     * If the input stream is closed (EOF), returns {@code null}.
     *
     * @param scanner the scanner to read from
     * @return the user's raw input line, or {@code null} if no more input is available
     */
    private String readInput(Scanner scanner) {
        try {
            return scanner.nextLine();
        } catch (NoSuchElementException e) {
            LOGGER.info("Input closed, exiting application");
            return null;
        }
    }

    /**
     * Parses and executes a single user command.
     * Also persists updated data after successful execution.
     *
     * @param input the user input string
     * @return {@code true} if the executed command signals application exit; {@code false} otherwise
     */
    private boolean executeCommand(String input) {
        try {
            Command command = parser.parse(input);
            command.execute(expenseManager, ui);
            StorageManager.saveExpenseManager(expenseManager, ui);
            return command.isExit();
        } catch (OrCashBuddyException e) {
            // Handle expected application exceptions
            LOGGER.log(Level.INFO, "Application error: " + e.getMessage());
            ui.showError(e.getMessage());
        } catch (Exception e) {
            // Handle unexpected exceptions
            LOGGER.log(Level.WARNING, "Unexpected error executing command: " + e.getMessage(), e);
            ui.showError("An unexpected error occurred while processing your command.");
        }
        return false;
    }

    /**
     * Ensures stdout/stderr always emit UTF-8 so Unicode descriptions remain intact
     * even on legacy Windows consoles that default to a non-Unicode code page.
     */
    private static void configureConsoleEncoding() {
        Charset utf8 = StandardCharsets.UTF_8;
        try {
            // Flush existing streams before swapping to avoid dropping buffered text.
            System.out.flush();
            System.err.flush();

            PrintStream utf8Out = new PrintStream(new FileOutputStream(FileDescriptor.out), true, utf8);
            PrintStream utf8Err = new PrintStream(new FileOutputStream(FileDescriptor.err), true, utf8);
            System.setOut(utf8Out);
            System.setErr(utf8Err);

            // On supported JVMs this informs internal writers about the desired charset.
            System.setProperty("sun.stdout.encoding", utf8.name());
            System.setProperty("sun.stderr.encoding", utf8.name());
        } catch (Exception e) {
            LOGGER.log(Level.FINE, "Unable to switch console streams to UTF-8", e);
        }

        if (isWindows()) {
            tryEnableWindowsUtf8Console();
        }
    }

    /**
     * Attempts to switch the Windows console code page to UTF-8 (65001) so that incoming user
     * input and outgoing text align with the UTF-8 streams we configure. Suppresses the
     * "Active code page" banner to avoid cluttering the UI.
     */
    private static void tryEnableWindowsUtf8Console() {
        runSilently("cmd", "/c", "chcp 65001 > nul");
        // PowerShell hosts cache their own input/output Encoding properties; align them as well.
        runSilently("powershell", "-NoProfile", "-NonInteractive",
                "[Console]::OutputEncoding=[System.Text.Encoding]::UTF8; " +
                        "[Console]::InputEncoding=[System.Text.Encoding]::UTF8");
    }

    private static boolean isWindows() {
        String osName = System.getProperty("os.name");
        return osName != null && osName.toLowerCase().contains("win");
    }

    private static void runSilently(String... command) {
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.redirectErrorStream(true);
        try {
            Process process = builder.start();
            try (InputStream stream = process.getInputStream()) {
                while (stream.read() != -1) {
                    // Drain output to keep the console clean.
                }
            }
            process.waitFor();
        } catch (IOException e) {
            LOGGER.log(Level.FINEST, "Failed to execute console setup command", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.log(Level.FINEST, "Interrupted while executing console setup command", e);
        }
    }

    /**
     * Program entry point. Creates a {@link Main} instance and starts the REPL loop.
     *
     * @param args standard {@code main} arguments (unused)
     */
    public static void main(String[] args) {
        new Main().run();
    }
}
