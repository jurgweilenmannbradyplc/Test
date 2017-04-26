package ch.syseca.sstl.util.beanutil.expression;


import org.apache.commons.beanutils.expression.DefaultResolver;


public class ExtResolver extends DefaultResolver {

    private static final char NESTED = '.'; // copied from DefaultResolver because declared as private
    private static final char MAPPED_START = '('; // copied from DefaultResolver because declared as private
    private static final char MAPPED_END = ')';// copied from DefaultResolver because declared as private
    private static final char INDEXED_START = '[';// copied from DefaultResolver because declared as private
    private static final char INDEXED_END = ']'; // copied from DefaultResolver because declared as private

    private static final char KEY_QUOTE = '\'';


    /**
     * Return the map key from the property expression or <code>null</code>.
     *
     * @param expression The property expression
     * @return The index value
     * @throws IllegalArgumentException If the mapped property is illegally formed.
     */
    @Override
    public String getKey(String expression) {
        if (expression == null || expression.length() == 0) {
            return null;
        }
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (c == NESTED || c == INDEXED_START) {
                return null;
            } else if (c == MAPPED_START) {

                if (isQuotedString(expression, i + 1)) {
                    return getQuotedString(expression);
                } else {
                    int end = expression.indexOf(MAPPED_END, i);
                    if (end < 0) {
                        throw new IllegalArgumentException("Missing End Delimiter");
                    }
                    return expression.substring(i + 1, end);
                }
            }
        }
        return null;
    }


    private boolean isQuotedString(String expression, int fromIndex) {
        return expression.indexOf(KEY_QUOTE, fromIndex) >= fromIndex;
    }


    private String getQuotedString(String expression) {
        final StringBuilder key = new StringBuilder();
        int begin;
        boolean escaped = false;

        begin = expression.indexOf(KEY_QUOTE) + 1;
        for (int i = begin; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (escaped) {
                key.append(c);
                escaped = false;
            }
            else if (c == KEY_QUOTE) {
                if (isChar(expression, i + 1, KEY_QUOTE)) {
                    escaped = true;
                }
                else {
                    break;
                }
            }
            else {
                key.append(c);
            }
        }

        return key.toString();
    }


    private boolean isChar(String expression, int pos, char c) {
        return pos < expression.length() && expression.charAt(pos) == c;
    }


    /**
     * Extract the next property expression from the
     * current expression.
     *
     * @param expression The property expression
     * @return The next property expression
     */
    @Override
    public String next(String expression) {
        if (expression == null || expression.length() == 0) {
            return null;
        }
        boolean indexed = false;
        boolean mapped = false;
        boolean quoted = false;
        boolean quoteStarted = false;
        boolean quoteEnded = false;
        boolean escaped = false;

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (indexed) {
                if (c == INDEXED_END) {
                    return expression.substring(0, i + 1);
                }
            } else if (mapped) {
                if (c == KEY_QUOTE) {
                    if (escaped) {
                        escaped = false;
                    } else if (!quoteStarted) {
                        quoteStarted = true;
                    } else if (!isChar(expression, i + 1, KEY_QUOTE)) {
                        // not escaped. Its't the end of the quote
                        quoteEnded = true;
                    } else {
                        escaped = true;
                    }
                } else if (c == MAPPED_END) {
                    if (!quoted || quoteEnded) {
                        return expression.substring(0, i + 1);
                    }
                }
            } else {
                if (c == NESTED) {
                    return expression.substring(0, i);
                } else if (c == MAPPED_START) {
                    mapped = true;
                    quoted = isQuotedString(expression, i + 1);
                } else if (c == INDEXED_START) {
                    indexed = true;
                }
            }
        }
        return expression;
    }

}
