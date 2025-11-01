package seedu.orcashbuddy.parser;

import seedu.orcashbuddy.exception.OrCashBuddyException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Lightweight helper that extracts prefixed argument values from
 * the argument portion of a user command.
 * <p>
 * Example input: {@code "a/50 desc/Lunch cat/Food"}.
 * <ul>
 *   <li>{@code a/} → "50"</li>
 *   <li>{@code desc/} → "Lunch"</li>
 *   <li>{@code cat/} → "Food"</li>
 * </ul>
 *
 * This class does not validate semantics (e.g. positive amounts);
 * validation is handled by {@link InputValidator}. It only slices
 * out raw string segments after known prefixes.
 */
public class ArgumentParser {
    private static final String[] ALL_PREFIXES = {"id/", "a/", "desc/", "cat/"};

    private final String input;
    private final String[] activePrefixes;
    private final Pattern prefixPattern;
    private final Map<String, String> parsedValues = new HashMap<>();
    private boolean parsed;

    /**
     * Creates a new {@code ArgumentParser} for a given argument string.
     *
     * @param input the raw argument substring after the command word
     */
    public ArgumentParser(String input) {
        this(input, ALL_PREFIXES);
    }

    public ArgumentParser(String input, String... allowedPrefixes) {
        this.input = input.trim();
        this.activePrefixes = normalisePrefixes(allowedPrefixes);
        this.prefixPattern = buildPrefixPattern(this.activePrefixes);
    }

    //@@author limzerui
    /**
     * Returns the value associated with a required prefix.
     * For example, calling {@code getValue("a/")} on
     * {@code "a/50 desc/lunch"} returns {@code "50"}.
     *
     * @param prefix the prefix to search for (e.g. {@code "a/"} or {@code "desc/"})
     * @return the value mapped to that prefix, trimmed
     * @throws OrCashBuddyException if the prefix is missing
     */
    public String getValue(String prefix) throws OrCashBuddyException {
        ensureParsed();
        String value = parsedValues.get(prefix);
        if (value == null) {
            throw new OrCashBuddyException("Missing prefix: " + prefix);
        }
        return value;
    }

    /**
     * Returns the value associated with an optional prefix.
     * Behaves like {@link #getValue(String)} except it returns {@code null}
     * if the prefix does not appear.
     *
     * @param prefix the prefix to search for (e.g. {@code "cat/"})
     * @return the associated value (trimmed), or {@code null} if the prefix is absent
     */
    public String getOptionalValue(String prefix) {
        ensureParsed();
        return parsedValues.get(prefix);
    }

    /**
     * Lazily tokenises the raw input. Subsequent lookups reuse the cached result.
     */
    private void ensureParsed() {
        if (parsed) {
            return;
        }
        parseArguments();
        parsed = true;
    }

    /**
     * Tokenises the raw input into prefix/value pairs.
     * <p>
     * Duplicate prefixes encountered while another prefix is active (for example {@code desc/... a/...})
     * are treated as literal text and left inside the current value. This avoids silently truncating values
     * such as descriptions that legitimately contain strings like {@code "a/12345"}. Should future features
     * require explicit escaping or duplicate detection, the rule can be tightened here without affecting callers.
     */
    private void parseArguments() {
        Matcher matcher = prefixPattern.matcher(input);
        String activePrefix = null;
        int valueStart = -1;

        while (matcher.find()) {
            String nextPrefix = matcher.group(1);

            if (activePrefix == null) {
                activePrefix = nextPrefix;
                valueStart = matcher.end();
                continue;
            }

            boolean duplicateOfCurrent = nextPrefix.equals(activePrefix);
            boolean alreadyCaptured = parsedValues.containsKey(nextPrefix);
            if (duplicateOfCurrent || alreadyCaptured) {
                // Keep scanning so the duplicate prefix characters remain inside the current value.
                continue;
            }

            String value = input.substring(valueStart, matcher.start()).trim();
            parsedValues.put(activePrefix, value);
            activePrefix = nextPrefix;
            valueStart = matcher.end();
        }

        if (activePrefix != null && !parsedValues.containsKey(activePrefix)) {
            String value = input.substring(valueStart).trim();
            parsedValues.put(activePrefix, value);
        }
    }

    private static String[] normalisePrefixes(String[] prefixes) {
        if (prefixes == null || prefixes.length == 0) {
            return ALL_PREFIXES;
        }
        String[] filtered = Arrays.stream(prefixes)
                .filter(p -> p != null && !p.isBlank())
                .distinct()
                .toArray(String[]::new);
        return filtered.length == 0 ? ALL_PREFIXES : filtered;
    }

    private static Pattern buildPrefixPattern(String[] prefixes) {
        String alternatives = Arrays.stream(prefixes)
                .map(Pattern::quote)
                .collect(Collectors.joining("|"));
        return Pattern.compile("(?<!\\S)(" + alternatives + ")");
    }
}
