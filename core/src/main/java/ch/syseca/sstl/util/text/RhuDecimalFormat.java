package ch.syseca.sstl.util.text;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.FieldPosition;


/**
 * This class formats decimal values like <code>DecimalFormat</code> using half-up rounding (see {@link
 * java.math.BigDecimal#ROUND_HALF_UP}).
 *
 * @author  fischelo
 * @see     java.text.DecimalFormat
 * @see     java.math.BigDecimal#ROUND_HALF_UP ROUND_HALF_UP;
 */
public class RhuDecimalFormat extends DecimalFormat {

    /**
     * Creates an <code>SDecimalFormat</code> using the given pattern and the symbols for the default locale.
     *
     * @param  pattern  A non-localized pattern string.
     */
    public RhuDecimalFormat(String pattern) {
        super(pattern);
    }

    /**
     * Formats a double to produce a string using the class <code>BigDecimal</code> in order to accurately handle
     * floating point values.
     *
     * <h4>Rounding</h4>
     * <code>MwFormat</code> uses half-up rounding (see {@link java.math.BigDecimal#ROUND_HALF_UP}) for formatting.
     *
     * @param   number         The double to format
     * @param   result         where the text is to be appended
     * @param   fieldPosition  On input: an alignment field, if desired. On output: the offsets of the alignment field.
     *
     * @return  The formatted number string
     *
     * @see     java.text.FieldPosition
     */
    public StringBuffer format(double number, StringBuffer result,
                               FieldPosition fieldPosition) {
        BigDecimal tmpValue = new BigDecimal(String.valueOf(number));
        tmpValue = tmpValue.setScale(this.getMaximumFractionDigits(), BigDecimal.ROUND_HALF_UP);
        return super.format(tmpValue.doubleValue(), result, fieldPosition);
    }
}
