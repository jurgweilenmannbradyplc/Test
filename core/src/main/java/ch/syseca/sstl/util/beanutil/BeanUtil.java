package ch.syseca.sstl.util.beanutil;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import ch.syseca.sstl.util.text.LinkedPropertiesFormat;


/**
 * Hilfsfunktionen zum Erzeugen und Initialisieren von Beans.
 */
public final class BeanUtil {

    /**
     * Klasse wird von normalen Programmen nicht instanziert.
     */
    private BeanUtil() {
    }


    /**
     * Erzeugt ein neues Bean der Klasse <code>clazz</code>.
     *
     * @param   <T>    Klasse des Beans.
     * @param   clazz  Klasse des Beans.
     *
     * @return  Bean.
     *
     * @throws  IllegalArgumentException  Wird geworfen, wenn das Bean nicht erzeugt werden konnte.
     */
    public static <T> T createBean(Class<T> clazz) throws IllegalArgumentException {
        T result;

        try {
            result = clazz.getConstructor().newInstance();
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

        return result;
    }


    /**
     * Erzeugt ein neues Bean der Klasse <code>className</code>. Das Bean wird mit den Werten in <code>properties</code>
     * initialisiert
     *
     * @param   <T>         Klasse des Beans.
     * @param   className   Name der Klasse.
     * @param   properties  Properties des Beans. Siehe {@link ch.syseca.sstl.util.text.PropertiesFormat}.
     *
     * @return  Bean.
     *
     * @throws  IllegalArgumentException  Wird geworfen, wenn das Bean nicht erzeugt werden konnte.
     */
    @SuppressWarnings("unchecked")
    public static <T> T createBean(String className, String properties) throws IllegalArgumentException {
        T result;
        Class<T> clazz;

        try {
            clazz = (Class<T>) Class.forName(className);
            result = createBean(clazz, properties);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

        return result;
    }


    /**
     * Erzeugt ein neues Bean der Klasse <code>clazz</code>. Das Bean wird mit den Werten in <code>properties</code>
     * initialisiert
     *
     * @param   <T>         Klasse des Beans.
     * @param   clazz       Klasse des Beans.
     * @param   properties  Properties des Beans. Siehe {@link ch.syseca.sstl.util.text.PropertiesFormat}.
     *
     * @return  Bean.
     *
     * @throws  IllegalArgumentException  Wird geworfen, wenn das Bean nicht erzeugt werden konnte.
     */
    public static <T> T createBean(Class<T> clazz, String properties) throws IllegalArgumentException {
        T result;
        Map<String, ?> p;

        try {
            p = new LinkedPropertiesFormat().parseProperties(properties);
            result = createBean(clazz, p);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

        return result;
    }


    /**
     * Erzeugt ein neues Bean der Klasse <code>clazz</code>. Das Bean wird mit den Werten in <code>properties</code>
     * initialisiert
     *
     * @param   <T>    Klasse des Beans.
     * @param   clazz  Klasse des Beans.
     * @param   p      Properties des Beans.
     *
     * @return  Bean.
     *
     * @throws  IllegalArgumentException  Wird geworfen, wenn das Bean nicht erzeugt werden konnte.
     */
    public static <T> T createBean(Class<T> clazz, Map<String, ?> p) throws IllegalArgumentException {
        T result;

        try {
            result = createBean(clazz);
            BeanUtilsBeanSingelton.get().populate(result, p);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

        return result;
    }


    /**
     * Gibt den Namen der Setter-Methode f&uuml;r ein Feld nach JavaBean Konvention.
     *
     * @param   fieldName  Name des Feldes
     *
     * @return  Name der Setter-Methode nach JavaBean Konvention
     */
    public static String getSetterName(String fieldName) {
        return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }


    /**
     * Testet, ob die Klasse eine <code>public</code> Methode mit dem angegebenen Namen besitzt. Anzahl Parameter und
     * R&uuml;ckgabetyp werden nicht ber&uuml;cksichtigt.
     *
     * @param   clazz       Klasse.
     * @param   methodName  Name der Methode.
     *
     * @return  <code>true</code>, wenn die Klasse eine Methode mit diesem Namen besitzt, sonst <code>false</code>.
     */
    public static boolean hasMethod(Class<?> clazz, String methodName) {
        boolean result = false;
        final Method[] methods = clazz.getMethods();

        for (Method m : methods) {
            if (m.getName().equals(methodName)) {
                result = true;
                break;
            }
        }

        return result;
    }


    /**
     * Wie {@link org.apache.commons.beanutils.BeanUtils#setProperty(java.lang.Object, java.lang.String, java.lang.Object)}.
     * Es wird jedoch zusaetzlich ueberprueft, ob es eine Setter-Methode gibt.
     * @param bean Bean, dessen Wert gesetzt werden soll.
     * @param property Name des Properties.
     * @param value Wert des Properties
     * @return {@code true}, wenn es eine entsprechende Setter-Methode gibt, sonst {@code false}.
     * @throws IllegalAccessException Siehe {@link org.apache.commons.beanutils.BeanUtils#setProperty(java.lang.Object, java.lang.String, java.lang.Object)}.
     * @throws InvocationTargetException {@link org.apache.commons.beanutils.BeanUtils#setProperty(java.lang.Object, java.lang.String, java.lang.Object)}.
     */
    public static boolean setProperty(Object bean, String property, Object value)
            throws IllegalAccessException, InvocationTargetException {
        final boolean methodFound = hasMethod(bean.getClass(), getSetterName(property));

        if (methodFound) {
            BeanUtilsBeanSingelton.get().setProperty(bean, property, value);
        }

        return methodFound;
    }

}
