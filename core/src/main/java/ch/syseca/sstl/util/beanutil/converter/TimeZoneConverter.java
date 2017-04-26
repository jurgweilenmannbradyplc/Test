package ch.syseca.sstl.util.beanutil.converter;


import java.util.TimeZone;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;


/**
 * Converter zur Umwandlung eines Strings in ein TimeZone-Objekt. Der String wird mit <code>TimeZone.getTimeZone</code>
 * in eine Zeitzone gewandelt.
 *
 * @see  java.util.TimeZone
 */
public class TimeZoneConverter implements Converter {

    /**
     * Wandelt einen String in ein TimeZone Objekt um. Es k&ouml;nnen alle Texte umgewandelt werden, die von {@link
     * TimeZone#getTimeZone(String)} unterst&uuml;tzt sind.
     *
     * @param   type {@inheritDoc}
     * @param   value {@inheritDoc}
     *
     * @return  {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object convert(Class type, Object value) {
        TimeZone result;

        if (type != TimeZone.class) {
            throw new ConversionException("Can not convert from class " + value.getClass().getName() + " to "
                    + type.getName() + ".");
        }

        result = TimeZone.getTimeZone(value.toString());

        return result;
    }

}
