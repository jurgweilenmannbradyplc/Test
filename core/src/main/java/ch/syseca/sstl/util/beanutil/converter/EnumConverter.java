package ch.syseca.sstl.util.beanutil.converter;


import java.lang.reflect.Array;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.converters.AbstractConverter;
import org.apache.commons.lang3.StringUtils;


/**
 * Converter zum wandeln eines <code>String</code>s oder einem Array von Strings in einen Enumeration
 * oder einem Array von Enumerations.
 */
public class EnumConverter extends AbstractConverter {

    @Override
    protected Object convertToType(Class type, Object value) throws Throwable {
        if (type.isEnum()) {
            return convertToSingleValue(type, value);
        } else if (type.isArray()) {
            return convertToArray(type, value);
        } else {
            throw new ConversionException("Class " + type.getName()
                    + " is not an enumeration nor an array of. Can not convert.");
        }
    }


    private Object convertToSingleValue(Class type, Object value) {
        return Enum.valueOf(type, value.toString());
    }


    private Object convertToArray(Class type, Object value) {
        Object result;
        final String[] values = value.toString().split(",");

        result = Array.newInstance(type.getComponentType(), values.length);
        for (int i = 0; i < values.length; i++) {
            Array.set(result, i,
                    StringUtils.isNotBlank(values[i]) ? convertToSingleValue(type.getComponentType(), values[i].trim())
                            : null);
        }
        return result;
    }


    @Override
    protected Class getDefaultType() {
        return null;
    }

}
