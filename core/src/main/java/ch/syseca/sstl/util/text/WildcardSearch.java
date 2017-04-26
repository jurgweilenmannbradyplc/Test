package ch.syseca.sstl.util.text;


/**
 * Class that implements a wildcard compare. There are two types of wildcards:
 *
 * <ol>
 * <li>single char wildcard (default is {@code _} ): it matches excactly to 1 char</li>
 * <li>multi char wildcard (default is {@code *} ): it matches 0..n chars</li>
 * </ol>
 *
 * <p>By default, the characters are compared in 'case insensitive' mode. This means, that {@code A} matches {@code A}
 * and {@code a}.</p>
 *
 * <p>Examples:</p>
 *
 * <ul>
 * <li>{@code t*} matches {@code test} or {@code tube} or {@code t}</li>
 * <li>t_st* matches 'test' or 'taste', but not 'tst'</li>
 * </ul>
 */

public final class WildcardSearch {

    private static final char DEFAULT_SINGLE_CHAR_WILDCARD = '_';

    private static final char DEFAULT_MULTI_CHAR_WILDCARD = '*';

    private static final boolean DEFAULT_CASE_SENSITIVE = false;

    private static final char ESCAPE_CHAR = '\\';

    private char singleCharWildcard;

    private char multiCharWildcard;

    private boolean caseSensitive;


    /**
     * WildcardString constructor.
     */
    private WildcardSearch() {
        super();
    }


    /**
     * Returns a new instance using the default wildcard values.
     *
     * @return  Neue Instanz.
     */
    public static WildcardSearch getInstance() {
        return getInstance(DEFAULT_MULTI_CHAR_WILDCARD, DEFAULT_SINGLE_CHAR_WILDCARD, DEFAULT_CASE_SENSITIVE);
    }


    /**
     * Returns a new instance using the default wildcard values using the given case sensitivity.
     *
     * @param   caseSensitive  If <code>false</code>, upper and lower case character are treated as equals.
     *
     * @return  Neue Instanz.
     */
    public static WildcardSearch getInstance(boolean caseSensitive) {
        return getInstance(DEFAULT_MULTI_CHAR_WILDCARD, DEFAULT_SINGLE_CHAR_WILDCARD, caseSensitive);
    }


    /**
     * Returns a new instance using the given values.
     *
     * @param   multiCharWildcard   Character that matches {@code 0..n} characters. This is normally the {@code *}
     *                              character.
     * @param   singleCharWildcard  Character that matches exactly one character. This is normally the {@code _}
     *                              character.
     * @param   caseSensitive       If <code>false</code>, upper and lower case character are treated as equals.
     *
     * @return  Neue Instanz.
     */
    public static WildcardSearch getInstance(char multiCharWildcard, char singleCharWildcard, boolean caseSensitive) {
        final WildcardSearch result = new WildcardSearch();

        result.caseSensitive = caseSensitive;
        result.multiCharWildcard = multiCharWildcard;
        result.singleCharWildcard = singleCharWildcard;

        return result;
    }


    /**
     * Returns <code>true</code> if the text matches the pattern. Both values must not be <code>null</code>, otherwise
     * an {@link IllegalArgumentException} is thrown.
     *
     * @param   text     Text to test.
     * @param   pattern  Wildcard pattern.
     *
     * @return  <code>true</code> if the {@code text} matches the {@code pattern}.
     *
     * @throws  IllegalArgumentException  One of the parameter is <code>null</code>.
     */
    public boolean matches(String text, String pattern) throws IllegalArgumentException {

        if (text == null) {
            throw new IllegalArgumentException(SimpleTextSupplier.argMustNotBeNull("text").get());
        }
        if (pattern == null) {
            throw new IllegalArgumentException(SimpleTextSupplier.argMustNotBeNull("pattern").get());
        }

        return matches(text, pattern, 0, 0);
    }


    /**
     * Returns <code>true</code> if the text matches the pattern.
     *
     * @param   text        Text to test.
     * @param   pattern     Wildcard pattern.
     * @param   textPos     The actual position within the text.
     * @param   patternPos  The actual position within the pattern.
     *
     * @return  <code>true</code> if the {@code text} matches the {@code pattern}.
     */
    private boolean matches(String text, String pattern, int textPos, int patternPos) {
        char patternChar;
        char textChar;

        if ((textPos >= text.length()) && (patternPos >= pattern.length())) {

            // The String matches the pattern. Stop recursion.
            return true;
        }

        if (patternPos >= pattern.length()) {

            // The string doesn't matche the pattern. Stop recursion.
            return false;
        }

        // test if multi char wildcard
        if (pattern.charAt(patternPos) == this.multiCharWildcard) {
            for (int i = textPos; i < text.length() + 1; i++) {
                if (matches(text, pattern, i, patternPos + 1)) {
                    return true;
                }
            }
            return false;
        }

        // test if single char wildcard
        if (pattern.charAt(patternPos) == this.singleCharWildcard) {
            return matches(text, pattern, textPos + 1, patternPos + 1);
        }

        // test if escape char
        if (pattern.charAt(patternPos) == ESCAPE_CHAR) {
            patternPos++; // compare to next char
            if (patternPos >= pattern.length()) {

                // no char follows the escape char. This is an error. But we are smart and
                // accept this as normal char
                patternPos--;
            }
        }

        if (textPos >= text.length()) {

            // The string doesn't matche the pattern. Stop recursion.
            return false;
        }

        // chars must be equal
        patternChar = pattern.charAt(patternPos);
        textChar = text.charAt(textPos);
        if (!this.caseSensitive) {
            patternChar = Character.toLowerCase(patternChar);
            textChar = Character.toLowerCase(textChar);
        }
        if (patternChar == textChar) {
            return matches(text, pattern, textPos + 1, patternPos + 1);
        }

        return false;
    }

}
