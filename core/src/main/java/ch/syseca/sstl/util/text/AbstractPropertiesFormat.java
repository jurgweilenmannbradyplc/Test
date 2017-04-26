package ch.syseca.sstl.util.text;


import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.StringUtils;


/**
 * Klasse zum Konvertieren einer Property-Liste in einen String. Der String hat das Format
 * 
 * <pre>
 *     name1=value1;name2=value2.....
 * </pre>
 * 
 * <p>
 * Der Algorithmus verwendet die <i>char insertion</i> Methode, um die Spezialzeichen '=' und ';' zu kodieren.
 * </p>
 * 
 * <p>
 * Beispiel: Hat das Property 'p1' den Wert '1+1=2', so wird der Wert wiefolgt kodiert:
 * </p>
 * 
 * <pre>
 * d = 1 + 1 == 2;
 * </pre>
 */
abstract class AbstractPropertiesFormat<T extends Map> extends Format {

    private static final char KEY_VALUE_SEPARATOR = '=';

    private static final char PROPERTY_SEPARATOR = ';';

    private static final String SPEZIAL_CHARS = new String(new char[] { KEY_VALUE_SEPARATOR, PROPERTY_SEPARATOR });


    public String formatProperties(T properties) {
        return format(properties);
    }


    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        final Map<String, String> properties = convertToExportPropertySet((T) obj);

        for (Entry<String, String> e : properties.entrySet()) {
            encode(toAppendTo, e.getKey());
            toAppendTo.append(KEY_VALUE_SEPARATOR);
            if (e.getValue() != null) {
                encode(toAppendTo, e.getValue());
            }
            toAppendTo.append(PROPERTY_SEPARATOR);
        }

        return toAppendTo;
    }


    /**
     * Parsed den Text in <code>source</code> und erstellt daraus die Properties Liste. Ist <code>source</code> leer, enth&auml;lt nur
     * Leerzeichen oder ist <code>null</code>, wird eine leere Properties Liste (nicht <code>null</code>) zur&uuml;ckgegeben.
     * 
     * @param source Properties String, der geparsed wird.
     * 
     * @return Properties Liste.
     * 
     * @throws ParseException
     */
    public T parseProperties(String source) throws ParseException {
        T result;

        if (StringUtils.isNotBlank(source)) {
            result = (T) parseObject(source);
        } else {
            result = createNewPropertySet();
        }

        return result;
    }


    @Override
    public Object parseObject(String source, ParsePosition pos) {
        final int savedIndex = pos.getIndex();
        T result = createNewPropertySet();

        while (pos.getIndex() < source.length()) {
            final String key;
            String v = "";

            key = decode(source, pos, KEY_VALUE_SEPARATOR);
            if (pos.getErrorIndex() != -1) {

                // Formatfehler
                break;
            } else if (StringUtils.isBlank(key)) {
                if (pos.getIndex() == source.length()) {

                    // keine weiteren Daten
                    break;
                } else {

                    // Formatfehler
                    pos.setErrorIndex(pos.getIndex());
                    break;
                }
            }

            if (pos.getIndex() < source.length()) {
                v = decode(source, pos, PROPERTY_SEPARATOR);
                if (pos.getErrorIndex() != -1) {
                    break;
                }
            }

            result.put(key.trim(), v.trim());
        }

        if (pos.getErrorIndex() != -1) {
            result = null;
            pos.setIndex(savedIndex);
        }

        return result;
    }


    protected abstract T createNewPropertySet();


    protected abstract Map<String, String> convertToExportPropertySet(T source);


    /**
     * Kodiert den Wert in <code>v</code>, indem alle Spezialzeichen verdoppelt werden (<i>char insertion</i>) und f&uuml;gt den neuen Wert
     * dem Buffer <code>toAppendTo</code> an.
     * 
     * @param toAppendTo Buffer, dem der kodierte Wert angef&uuml;gt wird.
     * @param v Wert, der kodiert und dem Buffer <code>toAppenTo</code> angef&uuml;gt wird.
     */
    private void encode(StringBuffer toAppendTo, String v) {
        for (int i = 0; i < v.length(); i++) {
            final char c = v.charAt(i);

            toAppendTo.append(c);
            if (SPEZIAL_CHARS.indexOf(c) != -1) {
                toAppendTo.append(c);
            }
        }
    }


    /**
     * Extrahiert den n&auml;chsten Wert aus dem String und gibt ihn dekodiert zur&uuml;ck. Es werden alle Zeichen bis zum ersten Vorkommen
     * von <code>separator</code> gelesen. Verdoppelte Zeichen (<i>char insertion</i>) werden korrekt behandelt. Ist das erste gefundene
     * Spezialzeichen ungleich <code>separator</code>, wird <code>
     * pos.errorIndex</code> gesetzt und <code>null</code> zur&uuml;ckgegeben. ausgel&ouml;st.
     * 
     * @param source Quelle.
     * @param pos Parse Position
     * @param separator Erwartetes Spezialzeichen.
     * 
     * @return Dekodierter Text oder <code>null</code>, wenn ein Fehler aufgetreten ist.
     */
    private String decode(String source, ParsePosition pos, char separator) {
        final StringBuilder b = new StringBuilder();
        final int savedIndex = pos.getIndex();

        while (pos.getIndex() < source.length()) {
            char c = source.charAt(pos.getIndex());

            if (SPEZIAL_CHARS.indexOf(c) == -1) {

                // normales Zeichen
                b.append(c);
                pos.setIndex(pos.getIndex() + 1);
            } else if (isDoubleChar(source, pos)) {

                // char insertion
                b.append(c);
                pos.setIndex(pos.getIndex() + 2);
            } else if (c == separator) {

                // Separator gefunden
                pos.setIndex(pos.getIndex() + 1);
                break;
            } else {
                pos.setErrorIndex(pos.getIndex());
                pos.setIndex(savedIndex);
                break;
            }
        }

        if (pos.getErrorIndex() == -1) {
            return b.toString();
        } else {
            return null;
        }
    }


    /**
     * &Uuml;berpr&uuml;ft, ob die Bedeutung des Spezialzeichens an der aktuellen Position durch <i>char insertion</i> aufgehoben ist.
     * 
     * @param source String, dessen Zeichen an Position <code>pos</code> gepr&uuml;ft werden soll.
     * @param pos Aktuelle Position in <code>source</code>, die gepr&uuml;ft werden soll.
     * 
     * @return <code>true</code>, wenn die Bedeutung des Spezialzeichens durch <i>char insertion</i> aufgehoben wurde, <code>false</code>
     *         wenn nicht.
     */
    private boolean isDoubleChar(String source, ParsePosition pos) {
        final int idx = pos.getIndex() + 1;

        return idx < source.length()
                && source.charAt(pos.getIndex()) == source.charAt(idx);
    }
}
