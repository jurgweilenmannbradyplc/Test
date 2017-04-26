package ch.syseca.sstl.util.beanutil;


import java.net.URI;
import java.util.TimeZone;
import org.apache.commons.beanutils.ConvertUtilsBean2;
import org.apache.commons.beanutils.Converter;
import ch.syseca.sstl.util.beanutil.converter.EnumConverter;
import ch.syseca.sstl.util.beanutil.converter.TimeZoneConverter;
import ch.syseca.sstl.util.beanutil.converter.URIConverter;


/**
 * Erweiterte ConvertUtilsBean2-Klasse, die zusaetzlich Konverter fuer
 *
 * <ul>
 * <li>Enums</li>
 * <li>{@link TimeZone}</li>
 * <li>{@link URI}</li>
 * </ul>
 *
 * bereitstellt.
 */
public class ExtConvertUtilsBean extends ConvertUtilsBean2 {

    public ExtConvertUtilsBean() {
        super();
        register(new TimeZoneConverter(), TimeZone.class);
        register(new URIConverter(), URI.class);
        register(new EnumConverter(), Enum[].class);
        register(new EnumConverter(), Enum.class);
    }


    @SuppressWarnings("rawtypes")
    @Override
    public Converter lookup(Class clazz) {
        Converter result;

        result = super.lookup(clazz);
        if (result == null) {
            if (clazz.isEnum()) {
                result = super.lookup(Enum.class);
            } else if (clazz.isArray() && clazz.getComponentType().isEnum()) {
                result = super.lookup(Enum[].class);
            }
        }

        return result;
    }

}
