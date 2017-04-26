package ch.syseca.sstl.util.text;


import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Supplier;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import ch.syseca.sstl.util.list.ListUtil;
import lombok.Getter;


/**
 * This class extends {@link MessageFormat} to support named arguments.
 * Example:
 * <pre>
 *      new MessageFormatNA("Hej {name}, you are {year} years old")
 *              .addNamedArgument("name", "Joe")
 *              .addNamedArgument("year", 99)
 *              .format()
 *  </pre>
 * returns <p>{@code Hej Joe, you are 99 years old}</p>
 * 
 * It's also possible to mix indexed and named arguments:
 * 
 * <pre>
 *      new MessageFormatNA("Hej {0}, you are {year} years old")
 *              .addIndexedArgument("Joe")
 *              .addNamedArgument("year", 99)
 *              .format()
 *  </pre>
 * returns <p>{@code Hej Joe, you are 99 years old}</p> as well.
 *
 */
public class MessageFormatNA extends MessageFormat {

    @FunctionalInterface
    interface ArgumentNameConverter {

        String readAndConvert(String pattern, ParsePosition p);

    }

    @Getter
    private String pattern;

    @Getter
    private TimeZone timeZone;

    private final Map<String, Supplier<?>> namedArgumentSuppliers = new HashMap<>();
    private final List<Object> indexedArguments = new ArrayList<>();

    private Map<String, Integer> nameToIndexMap; // initialised via applyPattern() in constructor of super class!


    public MessageFormatNA(String pattern) {
        super(pattern);
    }


    public MessageFormatNA(String pattern, Locale locale) {
        super(pattern, locale);
    }


    public MessageFormatNA addNamedArgument(String name, Object value) {
        return addNamedArgumentSuplier(name, () -> value);
    }


    public MessageFormatNA addNamedArgumentSuplier(String name, Supplier<?> supplier) {
        namedArgumentSuppliers.put(name, supplier);
        return this;
    }


    public MessageFormatNA addAllNamedArgumentSuplier(Map<String, Supplier<?>> suppliers) {
        namedArgumentSuppliers.putAll(suppliers);
        return this;
    }


    public MessageFormatNA addIndexedArgument(Object value) {
        indexedArguments.add(value);
        return this;
    }


    public MessageFormatNA setIndexedArgument(int pos, Object value) {
        ListUtil.fill(indexedArguments, pos + 1);
        indexedArguments.set(pos, value);
        return this;
    }


    @Override
    public void applyPattern(String pattern) {
        super.applyPattern(namesToIndex(pattern));
        updateDateFormatTimeZone();
    }


    String namesToIndex(String pattern) {
        final MutableInt namedArgIdx = new MutableInt(evaluateMaxArgIndex(pattern));
        String indexOnlyPattern;

        getNameToIndexMap().clear();
        indexOnlyPattern = rebuildPattern(pattern, (t, p) -> {
            if (CharUtils.isAsciiAlpha(t.charAt(p.getIndex()))) {
                int argIdx;
                String argName = parseName(t, p);

                argIdx = getNameToIndexMap().computeIfAbsent(argName, n -> {
                    namedArgIdx.increment();
                    return namedArgIdx.getValue();
                });
                return Integer.toString(argIdx);
            } else {
                return null;
            }
        });

        return indexOnlyPattern;
    }


    int evaluateMaxArgIndex(String pattern) {
        MutableInt result = new MutableInt(-1);

        rebuildPattern(pattern, (t, p) -> {
            if (CharUtils.isAsciiNumeric((t.charAt(p.getIndex())))) {
                int v = parseInt(t, p);
                result.setValue(Math.max(result.getValue(), v));
                return Integer.toString(v);
            } else {
                return null;
            }
        });

        return result.intValue();
    }


    private String rebuildPattern(String pattern, ArgumentNameConverter argNameConverter) {
        final StringBuilder indexedPattern = new StringBuilder();
        int idx = 0;
        int braceCount = 0;
        boolean inQuote = false;
        boolean checkForArgName = false;

        while (idx < pattern.length()) {
            char c = pattern.charAt(idx);

            switch (c) {
                case '{':
                    if (inQuote) {
                        inQuote = false;
                    } else {
                        braceCount++;
                        if (braceCount == 1) {
                            checkForArgName = true;
                        }
                    }
                    indexedPattern.append(c);
                    idx++;
                    break;

                case '}':
                    if (!inQuote) {
                        braceCount--;
                    }
                    indexedPattern.append(c);
                    idx++;
                    break;

                case '\'':
                    if (braceCount == 0) {
                        inQuote = !inQuote;
                    }
                    indexedPattern.append(c);
                    idx++;
                    break;

                default:
                    if (checkForArgName) {
                        ParsePosition p = new ParsePosition(idx);
                        String argName;

                        argName = argNameConverter.readAndConvert(pattern, p);
                        if (p.getIndex() > idx) {
                            idx = p.getIndex();
                            indexedPattern.append(argName);
                        } else {
                            indexedPattern.append(c);
                            idx++;
                        }
                        checkForArgName = false;
                    } else {
                        indexedPattern.append(c);
                        idx++;
                    }
                    break;
            }
        }

        return indexedPattern.toString();
    }


    private int parseInt(String text, ParsePosition p) {
        return NumberFormat.getIntegerInstance(getLocale())
                .parse(text, p)
                .intValue();
    }


    private String parseName(String text, ParsePosition p) {
        final int startPos = p.getIndex();

        while (p.getIndex() < text.length()) {
            char c = text.charAt(p.getIndex());

            if (!CharUtils.isAsciiAlpha(c)) {
                break;
            }
            p.setIndex(p.getIndex() + 1);
        }

        return text.substring(startPos, p.getIndex());
    }


    public String format() {
        StringBuffer result = new StringBuffer();
        List<Object> args = new ArrayList<>();

        args.addAll(indexedArguments);
        nameToIndexMap.forEach((k, p) -> {
            Supplier<?> s = namedArgumentSuppliers.get(k);

            if (s != null) {
                addToArgList(args, s.get(), p);
            } else {
                addToArgList(args, null, p);
            }
        });

        result = format(args.toArray(), result, new FieldPosition(0));
        return result.toString();
    }


    private void updateDateFormatTimeZone() {
        final TimeZone tz = timeZone != null ? timeZone : TimeZone.getDefault();

        Arrays.asList(getFormats()).forEach(f -> {
            if (f instanceof SimpleDateFormat && ((SimpleDateFormat) f).toPattern().endsWith("'Z'")) {
                ((DateFormat) f).setTimeZone(TimeZone.getTimeZone("GMT"));
            } else if (f instanceof DateFormat) {
                ((DateFormat) f).setTimeZone(tz);
            }
        });
    }


    public MessageFormatNA setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
        updateDateFormatTimeZone();
        return this;
    }


    Map<String, Integer> getNameToIndexMap() {
        if (nameToIndexMap == null) {
            nameToIndexMap = new HashMap<>();
        }
        return nameToIndexMap;
    }


    private void addToArgList(List<Object> argList, Object arg, int pos) {
        ListUtil.fill(argList, pos + 1);
        argList.set(pos, arg);
    }

}
