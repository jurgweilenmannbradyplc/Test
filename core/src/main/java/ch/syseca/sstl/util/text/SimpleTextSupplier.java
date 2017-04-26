package ch.syseca.sstl.util.text;


import java.util.Date;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;


@UtilityClass
public class SimpleTextSupplier {

    private static final String NULL = "null";


    public static Supplier<String> argMustNotBeNull(String argName) {
        return () -> "Argument [" + argName + "] must not be null.";
    }


    public static Supplier<String> dateTimeString(Date dateTime) {
        return () -> dateTime != null ? dateTime.toInstant().toString() : NULL;
    }

}
