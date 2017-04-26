package ch.syseca.sstl.util.beanutil.converter;


import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;


public class URIConverter implements Converter {

    @Override
    public Object convert(Class type, Object value) {
        URI result;

        if (type != URI.class) {
            throw new ConversionException("Can not convert from class " + value.getClass().getName() + " to "
                    + type.getName() + ".");
        }

        try {
            result = new URI(value.toString());
        } catch (URISyntaxException e) {
            throw new ConversionException("Can not convert " + value.toString() + " into an URI: " + e.getMessage());
        }

        return result;
    }

}
