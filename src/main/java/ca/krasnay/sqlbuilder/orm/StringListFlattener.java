package ca.krasnay.sqlbuilder.orm;

import java.util.ArrayList;
import java.util.List;

/**
 * Flattens a list of strings into a single string using a separator char. An
 * escape char is used to protect separator chars appearing within the data. By
 * default, the separator char is a comma and the escape char is the backslash.
 */
public class StringListFlattener {

    private boolean convertEmptyToNull = false;

    private char separator;
    private char escapeChar;

    public StringListFlattener(char separator, char escapeChar) {
        this.separator = separator;
        this.escapeChar = escapeChar;
    }

    public StringListFlattener(char separator) {
        this(separator, '\\');
    }

    public StringListFlattener() {
        this(',', '\\');
    }


    /**
     * Splits the given string.
     */
    public List<String> split(String s) {

        List<String> result = new ArrayList<String>();

        if (s == null) {
            return result;
        }

        boolean seenEscape = false;

        // Used to detect if the last char was a separator,
        // in which case we append an empty string
        boolean seenSeparator = false;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            seenSeparator = false;
            char c = s.charAt(i);
            if (seenEscape) {
                if (c == escapeChar || c == separator) {
                    sb.append(c);
                } else {
                    sb.append(escapeChar).append(c);
                }
                seenEscape = false;
            } else {
                if (c == escapeChar) {
                    seenEscape = true;
                } else if (c == separator) {
                    if (sb.length() == 0 && convertEmptyToNull) {
                        result.add(null);
                    } else {
                        result.add(sb.toString());
                    }
                    sb.setLength(0);
                    seenSeparator = true;
                } else {
                    sb.append(c);
                }
            }
        }

        // Clean up
        if (seenEscape) {
            sb.append(escapeChar);
        }

        if (sb.length() > 0 || seenSeparator) {

            if (sb.length() == 0 && convertEmptyToNull) {
                result.add(null);
            } else {
                result.add(sb.toString());
            }

        }

        return result;
    }

    /**
     * Joins the given list into a single string.
     */
    public String join(List<String> list) {

        if (list == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String s : list) {

            if (s == null) {
                if (convertEmptyToNull) {
                    s = "";
                } else {
                    throw new IllegalArgumentException("StringListFlattener does not support null strings in the list. Consider calling setConvertEmptyToNull(true).");
                }
            }

            if (!first) {
                sb.append(separator);
            }

            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c == escapeChar || c == separator) {
                    sb.append(escapeChar);
                }
                sb.append(c);
            }

            first = false;
        }
        return sb.toString();
    }

    public StringListFlattener setConvertEmptyToNull(boolean convertEmptyToNull) {
        this.convertEmptyToNull = convertEmptyToNull;
        return this;
    }


}
