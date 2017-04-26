package ch.syseca.sstl.util.beanutil;


import org.apache.commons.beanutils.BeanUtilsBean;
import ch.syseca.sstl.util.beanutil.expression.ExtResolver;


/**
 * Factory zum Erstellen einer einzigen {@link BeanUtilsBean}-Instanz. Als Convert-Utils wird die Klasse {@link
 * ExtConvertUtilsBean} verwendet.
 */
public final class BeanUtilsBeanSingelton {

    /** Singelton. */
    private static final BeanUtilsBean instance;

    static {
        instance = new BeanUtilsBean(new ExtConvertUtilsBean());
        instance.getPropertyUtils().setResolver(new ExtResolver());
    }


    /**
     * Klasse wird nicht Instanziert.
     */
    private BeanUtilsBeanSingelton() {
    }


    /**
     * Gibt die Instanz von <code>BeanUtilsBean</code> zur&uuml;ck.
     *
     * @return  Instanz von <code>BeanUtilsBean</code>.
     */
    public static BeanUtilsBean get() {
        return instance;
    }

}
